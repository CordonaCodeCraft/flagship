package flagship.domain.calculators.statedues;

import com.fasterxml.jackson.databind.ObjectMapper;
import flagship.domain.calculators.DueCalculatorTest;
import flagship.domain.calculators.tariffs.stateduestariffs.WharfDueTariff;
import flagship.domain.cases.entities.Case;
import flagship.domain.cases.entities.Ship;
import flagship.domain.cases.entities.enums.CallPurpose;
import flagship.domain.cases.entities.enums.ShipType;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.stream.Stream;

import static flagship.domain.cases.entities.enums.ShipType.GENERAL;
import static flagship.domain.cases.entities.enums.ShipType.MILITARY;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("Wharf due calculator tests")
class WharfDueCalculatorTest implements DueCalculatorTest {

    private static WharfDueTariff tariff;
    private final WharfDueCalculator wharfDueCalculator = new WharfDueCalculator();
    private Case testCase;

    @BeforeAll
    public static void beforeClass() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        tariff = mapper.readValue(new File("src/main/resources/wharfDueTariff.json"), WharfDueTariff.class);
    }

    @BeforeEach
    void setUp() {
        Ship testShip = Ship.builder().lengthOverall(195.05).type(GENERAL).build();
        testCase = Case.builder().ship(testShip).alongsideDaysExpected(5).build();
    }

    @DisplayName("Wharf due by default value")
    @Test
    void testWharfDueByDefaultValue() {

        ShipType shipTypeWithDefaultWharfDue = Arrays.stream(ShipType.values())
                .filter(type -> !tariff.getWharfDuesByShipType().containsKey(type))
                .findAny()
                .orElse(MILITARY);

        testCase.getShip().setType(shipTypeWithDefaultWharfDue);

        BigDecimal expected = calculateWharfDue();
        BigDecimal result = wharfDueCalculator.calculateFor(testCase, tariff);

        assertThat(result).isEqualByComparingTo(expected);
    }

    @DisplayName("Wharf due by ship type")
    @ParameterizedTest(name = "ship type : {arguments}")
    @MethodSource(value = "getShipTypesAffectingWharfDue")
    void testWharfDueByShipType(ShipType shipType) {

        testCase.getShip().setType(shipType);

        BigDecimal expected = calculateWharfDue();
        BigDecimal result = wharfDueCalculator.calculateFor(testCase, tariff);

        assertThat(result).isEqualByComparingTo(expected);
    }

    @DisplayName("Wharf due with discount by call purpose")
    @ParameterizedTest(name = "call purpose : {arguments}")
    @MethodSource(value = "getCallPurposesEligibleForDiscount")
    void testWharfDueWithDiscountByCallPurpose(CallPurpose callPurpose) {

        testCase.setCallPurpose(callPurpose);

        BigDecimal discountCoefficient = tariff.getDiscountCoefficientsByCallPurpose().get(testCase.getCallPurpose());

        BigDecimal expected = calculateDueAfterDiscount(discountCoefficient);
        BigDecimal result = wharfDueCalculator.calculateFor(testCase, tariff);

        assertThat(result).isEqualByComparingTo(expected);
    }

    @DisplayName("Wharf due without discount when ship type is not eligible for discount")
    @ParameterizedTest(name = "ship type : {arguments}")
    @MethodSource(value = "getShipTypesNotEligibleForDiscount")
    void testWharfDueWithZeroDiscountWhenShipTypeIsNotEligibleForDiscount(ShipType shipType) {

        testCase.getShip().setType(shipType);

        BigDecimal discountCoefficient = BigDecimal.ZERO;

        BigDecimal expected = calculateDueAfterDiscount(discountCoefficient);
        BigDecimal result = wharfDueCalculator.calculateFor(testCase, tariff);

        assertThat(result).isEqualByComparingTo(expected);
    }

    private static Stream<Arguments> getShipTypesAffectingWharfDue() {
        return tariff.getWharfDuesByShipType()
                .keySet()
                .stream()
                .map(Arguments::of);
    }

    private static Stream<Arguments> getCallPurposesEligibleForDiscount() {
        return tariff.getDiscountCoefficientsByCallPurpose()
                .keySet()
                .stream()
                .map(Arguments::of);
    }

    private static Stream<Arguments> getShipTypesNotEligibleForDiscount() {
        return tariff.getShipTypesNotEligibleForDiscount()
                .stream()
                .map(Arguments::of);
    }

    private BigDecimal calculateDueAfterDiscount(BigDecimal discountCoefficient) {
        BigDecimal wharfDue = calculateWharfDue();
        BigDecimal discount = wharfDue.multiply(discountCoefficient);
        return wharfDue.subtract(discount);
    }

    private BigDecimal calculateWharfDue() {
        final BigDecimal lengthOverall = BigDecimal.valueOf(Math.ceil(testCase.getShip().getLengthOverall()));
        final BigDecimal wharfDuePerMeter = tariff.getWharfDuesByShipType().getOrDefault(testCase.getShip().getType(), tariff.getDefaultWharfDue());
        final BigDecimal wharfDuePerLengthOverall = lengthOverall.multiply(wharfDuePerMeter);
        final BigDecimal alongsideHoursExpected = BigDecimal.valueOf(testCase.getAlongsideDaysExpected() * 24);
        return alongsideHoursExpected.multiply(wharfDuePerLengthOverall);
    }

}