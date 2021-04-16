package flagship.domain.utils.calculators.statedues;

import com.fasterxml.jackson.databind.ObjectMapper;
import flagship.domain.cases.entities.Case;
import flagship.domain.cases.entities.Ship;
import flagship.domain.cases.entities.enums.CallPurpose;
import flagship.domain.cases.entities.enums.ShipType;
import flagship.domain.utils.tariffs.WharfDueTariff;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static flagship.domain.cases.entities.enums.CallPurpose.LOADING;
import static flagship.domain.cases.entities.enums.CallPurpose.RESUPPLY;
import static flagship.domain.cases.entities.enums.ShipType.GENERAL;
import static flagship.domain.cases.entities.enums.ShipType.MILITARY;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("Wharf due calculator tests")
class WharfDueCalculatorTest implements DueCalculatorTest {

    private static WharfDueTariff tariff;
    private final WharfDueCalculator wharfDueCalculator = new WharfDueCalculator();
    private Case testCase;
    private BigDecimal lengthOverAll;
    private BigDecimal alongSideHours;


    @BeforeAll
    public static void beforeClass() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        tariff = mapper.readValue(new File("src/main/resources/wharfDueTariff.json"), WharfDueTariff.class);
    }

    @BeforeEach
    void setUp() {
        Ship testShip = Ship.builder().lengthOverall(195.05).type(GENERAL).build();
        testCase = Case.builder().ship(testShip).callPurpose(LOADING).callCount(1).alongsideDaysExpected(5).build();
        lengthOverAll = BigDecimal.valueOf(Math.ceil(testCase.getShip().getLengthOverall()));
        alongSideHours = BigDecimal.valueOf(testCase.getAlongsideDaysExpected() * 24);
    }

    @DisplayName("Wharf due calculation tests")
    @Nested
    class DueTotalCalculation {

        @DisplayName("Wharf due by default value")
        @Test
        void testReturnsDefaultWharfDue() {

            ShipType shipTypeWithDefaultWharfDue = Arrays.stream(ShipType.values())
                    .filter(type -> {
                        Set<ShipType> shipTypesAffectingWharfDue = new HashSet<>(tariff.getWharfDuesByShipType().keySet());
                        return !shipTypesAffectingWharfDue.contains(type);
                    })
                    .findAny().get();

            testCase.getShip().setType(shipTypeWithDefaultWharfDue);

            BigDecimal wharfDuePerMeter = tariff.getDefaultWharfDue();
            BigDecimal wharfDuePerLengthOverall = lengthOverAll.multiply(wharfDuePerMeter);

            BigDecimal expected = alongSideHours.multiply(wharfDuePerLengthOverall);
            BigDecimal result = wharfDueCalculator.calculateDue(testCase, tariff);

            assertThat(result).isEqualByComparingTo(expected);
        }

        @DisplayName("Wharf due by ship type")
        @ParameterizedTest(name = "ship type : {arguments}")
        @EnumSource(value = ShipType.class, names = {"MILITARY"})
        void testWharfDueByShipType(ShipType shipType) {

            testCase.getShip().setType(shipType);

            BigDecimal wharfDuePerMeter = tariff.getWharfDuesByShipType().get(shipType);
            BigDecimal wharfDuePerMeterTotal = lengthOverAll.multiply(wharfDuePerMeter);

            BigDecimal expected = alongSideHours.multiply(wharfDuePerMeterTotal);
            BigDecimal result = wharfDueCalculator.calculateDue(testCase, tariff);

            assertThat(result).isEqualByComparingTo(expected);
        }
    }

    @DisplayName("Discount coefficient evaluation tests")
    @Nested()
    class DiscountCoefficientTests {

        @DisplayName("Discount coefficient by call purpose")
        @ParameterizedTest(name = "call purpose : {arguments}")
        @EnumSource(value = CallPurpose.class, names = {"RESUPPLY", "RECRUITMENT", "POSTAL", "REPAIR"})
        void testDiscountCoefficientByCallPurpose(CallPurpose callPurpose) {

            testCase.setCallPurpose(callPurpose);

            BigDecimal expected = tariff.getDiscountCoefficientsByCallPurpose().get(testCase.getCallPurpose());
            BigDecimal result = wharfDueCalculator.evaluateDiscountCoefficient(testCase, tariff);

            assertThat(result).isEqualByComparingTo(expected);
        }

        @DisplayName("Should return zero if ship type not eligible for discount")
        @ParameterizedTest(name = "ship type : {arguments}")
        @EnumSource(value = ShipType.class, names = {"MILITARY"})
        void testDiscountCoefficientShouldReturnZeroByShipType(ShipType shipType) {

            testCase.getShip().setType(shipType);

            BigDecimal expected = BigDecimal.ZERO;
            BigDecimal result = wharfDueCalculator.evaluateDiscountCoefficient(testCase, tariff);

            assertThat(result).isEqualByComparingTo(expected);
        }
    }


}