package flagship.domain.calculators.statedues;

import com.fasterxml.jackson.databind.ObjectMapper;
import flagship.domain.calculators.DueCalculatorTest;
import flagship.domain.calculators.tariffs.stateduestariffs.TonnageDueTariff;
import flagship.domain.cases.entities.Case;
import flagship.domain.cases.entities.Port;
import flagship.domain.cases.entities.Ship;
import flagship.domain.cases.entities.enums.CallPurpose;
import flagship.domain.cases.entities.enums.PortArea;
import flagship.domain.cases.entities.enums.ShipType;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import static flagship.domain.cases.entities.enums.CallPurpose.LOADING;
import static flagship.domain.cases.entities.enums.PortArea.FIRST;
import static flagship.domain.cases.entities.enums.ShipType.GENERAL;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("Tonnage due calculator tests")
class TonnageDueCalculatorTest implements DueCalculatorTest {

    private static TonnageDueTariff tariff;
    private final TonnageDueCalculator tonnageDueCalculator = new TonnageDueCalculator();
    private Case testCase;
    private BigDecimal grossTonnage;

    @BeforeAll
    public static void beforeClass() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        tariff = mapper.readValue(new File("src/main/resources/tonnageDueTariff.json"), TonnageDueTariff.class);
    }

    @BeforeEach
    void setUp() {
        Integer randomGrossTonnage = getRandomGrossTonnage();
        Port testPort = Port.builder().area(FIRST).build();
        Ship testShip = Ship.builder().grossTonnage(randomGrossTonnage).type(GENERAL).build();
        testCase = Case.builder().ship(testShip).callPurpose(LOADING).port(testPort).callCount(1).build();
        grossTonnage = BigDecimal.valueOf(testShip.getGrossTonnage());
    }

    @DisplayName("Tonnage due by port area")
    @ParameterizedTest(name = "port area : {arguments}")
    @EnumSource(PortArea.class)
    void testTonnageDueByPortArea(PortArea portArea) {

        ShipType shipTypeDependantOnPortArea = Arrays
                .stream(ShipType.values())
                .filter(type -> !tariff.getTonnageDuesByShipType().containsKey(type))
                .findAny()
                .orElse(GENERAL);

        CallPurpose callPurposeDependantOnPortArea = Arrays
                .stream(CallPurpose.values())
                .filter(purpose -> !tariff.getTonnageDuesByCallPurpose().containsKey(purpose))
                .findAny()
                .orElse(LOADING);

        testCase.getShip().setType(shipTypeDependantOnPortArea);
        testCase.setCallPurpose(callPurposeDependantOnPortArea);
        testCase.getPort().setArea(portArea);

        BigDecimal duePerTon = tariff.getTonnageDuesByPortArea().get(testCase.getPort().getArea());

        BigDecimal expected = grossTonnage.multiply(duePerTon);
        BigDecimal result = tonnageDueCalculator.calculateFor(testCase, tariff);

        assertThat(result).isEqualByComparingTo(expected);
    }

    @DisplayName("Tonnage due by ship type")
    @ParameterizedTest(name = "ship type : {arguments}")
    @MethodSource(value = "getShipTypesAffectingTonnageDue")
    void testTonnageDueByShipType(ShipType shipType) {

        testCase.getShip().setType(shipType);

        BigDecimal duePerTon = tariff.getTonnageDuesByShipType().get(testCase.getShip().getType());

        BigDecimal expected = grossTonnage.multiply(duePerTon);
        BigDecimal result = tonnageDueCalculator.calculateFor(testCase, tariff);

        assertThat(result).isEqualByComparingTo(expected);
    }

    @DisplayName("Tonnage due by call purpose")
    @ParameterizedTest(name = "call purpose : {arguments}")
    @MethodSource(value = "getCallPurposesAffectingTonnageDue")
    void testTonnageDueByCallPurpose(CallPurpose callPurpose) {

        testCase.setCallPurpose(callPurpose);

        BigDecimal duePerTon = tariff.getTonnageDuesByCallPurpose().get(testCase.getCallPurpose());

        BigDecimal expected = grossTonnage.multiply(duePerTon);
        BigDecimal result = tonnageDueCalculator.calculateFor(testCase, tariff);

        assertThat(result).isEqualByComparingTo(expected);
    }

    @DisplayName("Tonnage due with discount by call count")
    @Test
    void testTonnageDueWithDiscountByCallCount() {

        testCase.setCallCount(tariff.getCallCountThreshold());

        BigDecimal discountCoefficient = tariff.getCallCountDiscountCoefficient();

        BigDecimal expected = calculateDueAfterDiscount(discountCoefficient);
        BigDecimal result = tonnageDueCalculator.calculateFor(testCase, tariff);

        assertThat(result).isEqualByComparingTo(expected);
    }

    @DisplayName("Tonnage due with discount by call purpose")
    @ParameterizedTest(name = "call purpose : {arguments}")
    @MethodSource(value = "getCallPurposesEligibleForDiscount")
    void testTonnageDueWithDiscountByCallPurpose(CallPurpose callPurpose) {

        testCase.setCallPurpose(callPurpose);

        BigDecimal discountCoefficient = tariff.getDiscountCoefficientsByCallPurpose().get(callPurpose);

        BigDecimal expected = calculateDueAfterDiscount(discountCoefficient);
        BigDecimal result = tonnageDueCalculator.calculateFor(testCase, tariff);

        assertThat(result).isEqualByComparingTo(expected);
    }

    @DisplayName("Tonnage due with discount by ship type")
    @ParameterizedTest(name = "ship type : {arguments}")
    @MethodSource(value = "getShipTypesEligibleForDiscount")
    void testTonnageDueWithDiscountByShipType(ShipType shipType) {

        testCase.getShip().setType(shipType);

        BigDecimal discountCoefficient = tariff.getDiscountCoefficientsByShipType().get(testCase.getShip().getType());

        BigDecimal expected = calculateDueAfterDiscount(discountCoefficient);
        BigDecimal result = tonnageDueCalculator.calculateFor(testCase, tariff);

        assertThat(result).isEqualByComparingTo(expected);
    }

    @DisplayName("Tonnage due with biggest discount")
    @Test
    void testTonnageDueWithBiggestDiscount() {

        ShipType shipType = getShipTypeEligibleForDiscount();
        CallPurpose callPurpose = getCallPurposeEligibleForDiscount();

        testCase.getShip().setType(shipType);
        testCase.setCallPurpose(callPurpose);
        testCase.setCallCount(tariff.getCallCountThreshold());

        List<BigDecimal> discountCoefficients = Arrays.asList(
                tariff.getDiscountCoefficientsByShipType().get(testCase.getShip().getType()),
                tariff.getDiscountCoefficientsByCallPurpose().get(testCase.getCallPurpose()),
                tariff.getCallCountDiscountCoefficient()
        );

        BigDecimal discountCoefficient = discountCoefficients.stream().max(Comparator.naturalOrder()).get();

        BigDecimal expected = calculateDueAfterDiscount(discountCoefficient);
        BigDecimal result = tonnageDueCalculator.calculateFor(testCase, tariff);

        assertThat(result).isEqualByComparingTo(expected);
    }

    @DisplayName("Tonnage due without discount when ship type is not eligible for discount")
    @ParameterizedTest(name = "ship type : {arguments}")
    @MethodSource(value = "getShipTypesNotEligibleForDiscount")
    void testTonnageDueWithZeroDiscountByShipType(ShipType shipType) {

        testCase.getShip().setType(shipType);
        testCase.setCallPurpose(getCallPurposeEligibleForDiscount());
        testCase.setCallCount(tariff.getCallCountThreshold());

        BigDecimal discountCoefficient = BigDecimal.ZERO;

        BigDecimal expected = calculateDueAfterDiscount(discountCoefficient);
        BigDecimal result = tonnageDueCalculator.calculateFor(testCase, tariff);

        assertThat(result).isEqualByComparingTo(expected);
    }

    @DisplayName("Tonnage due without discount when call purpose is not eligible for discount")
    @ParameterizedTest(name = "call purpose : {arguments}")
    @MethodSource(value = "getCallPurposesNotEligibleForDiscount")
    void testTonnageDueWithZeroDiscountByCallPurpose(CallPurpose callPurpose) {

        testCase.getShip().setType(getShipTypeEligibleForDiscount());
        testCase.setCallPurpose(callPurpose);
        testCase.setCallCount(tariff.getCallCountThreshold());

        BigDecimal discountCoefficient = BigDecimal.ZERO;

        BigDecimal expected = calculateDueAfterDiscount(discountCoefficient);
        BigDecimal result = tonnageDueCalculator.calculateFor(testCase, tariff);

        assertThat(result).isEqualByComparingTo(expected);
    }

    private static Stream<Arguments> getShipTypesAffectingTonnageDue() {
        return tariff.getTonnageDuesByShipType()
                .keySet()
                .stream()
                .map(Arguments::of);
    }

    private static Stream<Arguments> getCallPurposesAffectingTonnageDue() {
        return tariff.getTonnageDuesByCallPurpose()
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

    private static Stream<Arguments> getShipTypesEligibleForDiscount() {
        return tariff.getDiscountCoefficientsByShipType()
                .keySet()
                .stream()
                .map(Arguments::of);
    }

    private static Stream<Arguments> getShipTypesNotEligibleForDiscount() {
        return tariff.getShipTypesNotEligibleForDiscount()
                .stream()
                .map(Arguments::of);
    }

    private static Stream<Arguments> getCallPurposesNotEligibleForDiscount() {
        return tariff.getCallPurposesNotEligibleForDiscount()
                .stream()
                .map(Arguments::of);
    }

    private ShipType getShipTypeEligibleForDiscount() {
        return tariff.getDiscountCoefficientsByShipType().keySet().stream().findAny().get();
    }

    private CallPurpose getCallPurposeEligibleForDiscount() {
        return tariff.getDiscountCoefficientsByCallPurpose().keySet().stream().findAny().get();
    }

    BigDecimal calculateDueAfterDiscount(BigDecimal discountCoefficient) {

        BigDecimal duePerGrossTon;

        final boolean dueIsDependentOnShipType =
                tariff.getTonnageDuesByShipType().containsKey(testCase.getShip().getType());
        final boolean dueIsDependantOnCallPurpose =
                tariff.getTonnageDuesByCallPurpose().containsKey(testCase.getCallPurpose());

        if (dueIsDependentOnShipType) {
            duePerGrossTon = tariff.getTonnageDuesByShipType().get(testCase.getShip().getType());
        } else if (dueIsDependantOnCallPurpose) {
            duePerGrossTon = tariff.getTonnageDuesByCallPurpose().get(testCase.getCallPurpose());
        } else {
            duePerGrossTon = tariff.getTonnageDuesByPortArea().get(testCase.getPort().getArea());
        }

        BigDecimal dueTotal = grossTonnage.multiply(duePerGrossTon);
        BigDecimal discount = dueTotal.multiply(discountCoefficient);

        return dueTotal.subtract(discount);
    }

    private Integer getRandomGrossTonnage() {
        Random random = new Random();
        return random.ints(500, 200000).findFirst().getAsInt();
    }

}
