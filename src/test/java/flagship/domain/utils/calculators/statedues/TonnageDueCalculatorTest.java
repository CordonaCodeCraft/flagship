package flagship.domain.utils.calculators.statedues;

import com.fasterxml.jackson.databind.ObjectMapper;
import flagship.domain.cases.entities.Case;
import flagship.domain.cases.entities.Port;
import flagship.domain.cases.entities.Ship;
import flagship.domain.cases.entities.enums.CallPurpose;
import flagship.domain.cases.entities.enums.PortArea;
import flagship.domain.cases.entities.enums.ShipType;
import flagship.domain.utils.tariffs.TonnageDueTariff;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

import static flagship.domain.cases.entities.enums.CallPurpose.LOADING;
import static flagship.domain.cases.entities.enums.PortArea.FIRST;
import static flagship.domain.cases.entities.enums.ShipType.GENERAL;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Tag("calculators")
@DisplayName("Tonnage due calculator tests")
class TonnageDueCalculatorTest {

    private Case testCase;
    private Ship testShip;

    private final TonnageDueCalculator tonnageDueCalculator = new TonnageDueCalculator();

    private static TonnageDueTariff tariff;

    @BeforeAll
    public static void beforeClass() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        tariff = mapper.readValue(new File("src/main/resources/tonnageDueTariff.json"), TonnageDueTariff.class);
    }


    @BeforeEach
    void setUp() {
        Port testPort = Port.builder().area(FIRST).build();
        testShip = Ship.builder().grossTonnage(1650).type(GENERAL).build();
        testCase = Case.builder().ship(testShip).callPurpose(LOADING).port(testPort).callCount(1).build();
    }

    @DisplayName("Final tonnage due calculation")
    @Test()
    void testTonnageDueCalculation() {

        BigDecimal tonnageDue = tonnageDueCalculator.calculateDueTotal(testCase, tariff);
        BigDecimal discountCoefficient = tonnageDueCalculator.evaluateDiscountCoefficient(testCase, tariff);
        BigDecimal expected = tonnageDue;

        if (discountCoefficient.doubleValue() > 0) {
            BigDecimal discount = tonnageDue.multiply(discountCoefficient);
            expected = tonnageDue.subtract(discount);
        }

        BigDecimal result = tonnageDueCalculator.calculate(testCase, tariff);

        assertThat(expected).isEqualByComparingTo(result);
    }

    @DisplayName("Total tonnage due calculation tests")
    @Nested
    class DueTotalCalculation {

        @DisplayName("- By ship type")
        @ParameterizedTest(name = "as : {arguments}")
        @EnumSource(value = ShipType.class, names = {"OIL_TANKER", "RECREATIONAL", "MILITARY", "SPECIAL"})
        void testDueTotalByShipType(ShipType shipType) {

            testCase.getShip().setType(shipType);

            BigDecimal grossTonnage = BigDecimal.valueOf(testShip.getGrossTonnage());
            BigDecimal duePerTon = tariff.getTonnageDuesByShipType().get(testShip.getType());

            BigDecimal expected = grossTonnage.multiply(duePerTon);
            BigDecimal result = tonnageDueCalculator.calculateDueTotal(testCase, tariff);

            assertThat(expected).isEqualByComparingTo(result);
        }

        @DisplayName("- by call purpose")
        @ParameterizedTest(name = "as : {arguments}")
        @EnumSource(value = CallPurpose.class, names = {"SPECIAL_PURPOSE_PORT_VISIT"})
        void testDueTotalByCallPurpose(CallPurpose callPurpose) {

            testCase.setCallPurpose(callPurpose);

            BigDecimal grossTonnage = BigDecimal.valueOf(testShip.getGrossTonnage());
            BigDecimal duePerTon = tariff.getTonnageDuesByCallPurpose().get(testCase.getCallPurpose());

            BigDecimal expected = grossTonnage.multiply(duePerTon);
            BigDecimal result = tonnageDueCalculator.calculateDueTotal(testCase, tariff);

            assertThat(expected).isEqualByComparingTo(result);
        }

        @DisplayName("- by port area")
        @ParameterizedTest(name = "as : {arguments}")
        @EnumSource(value = PortArea.class, names = {"FIRST", "SECOND", "THIRD", "FOURTH"})
        void testDueTotalByPortArea(PortArea portArea) {

            testCase.getPort().setArea(portArea);

            ShipType shipTypeDependantOnPortArea = Arrays.stream(ShipType.values())
                    .filter(type -> {
                                Set<ShipType> shipTypesAffectingTonnageDue = new HashSet<>(tariff.getTonnageDuesByShipType().keySet());
                                return !shipTypesAffectingTonnageDue.contains(type);
                            }
                    ).findAny().get();

            CallPurpose callPurposeDependantOnPortArea = Arrays.stream(CallPurpose.values())
                    .filter(purpose -> {
                                Set<CallPurpose> callPurposesAffectingTonnageDue = new HashSet<>(tariff.getTonnageDuesByCallPurpose().keySet());
                                return !callPurposesAffectingTonnageDue.contains(purpose);
                            }
                    ).findAny().get();

            testCase.getShip().setType(shipTypeDependantOnPortArea);
            testCase.setCallPurpose(callPurposeDependantOnPortArea);

            BigDecimal grossTonnage = BigDecimal.valueOf(testShip.getGrossTonnage());
            BigDecimal duePerTon = tariff.getTonnageDuesByPortArea().get(testCase.getPort().getArea());

            BigDecimal expected = grossTonnage.multiply(duePerTon);
            BigDecimal result = tonnageDueCalculator.calculateDueTotal(testCase, tariff);

            assertThat(expected).isEqualByComparingTo(result);
        }

    }

    @DisplayName("Discount coefficient evaluation tests")
    @Nested
    class DiscountCoefficientEvaluation {

        @DisplayName("- by call purpose")
        @ParameterizedTest(name = "as : {arguments}")
        @EnumSource(value = CallPurpose.class, names = {"RESUPPLY", "RECRUITMENT", "POSTAL", "REPAIR"})
        void testDiscountCoefficientByCallPurpose(CallPurpose callPurpose) {

            testCase.setCallPurpose(callPurpose);

            BigDecimal expected = tariff.getDiscountCoefficientsByCallPurpose().get(testCase.getCallPurpose());
            BigDecimal result = tonnageDueCalculator.evaluateDiscountCoefficient(testCase, tariff);

            assertThat(expected).isEqualByComparingTo(result);
        }

        @DisplayName("- by ship type")
        @ParameterizedTest(name = "as : {arguments}")
        @EnumSource(value = ShipType.class, names = {"REEFER", "CONTAINER", "PASSENGER"})
        void testDiscountCoefficientByShipType(ShipType shipType) {

            testCase.getShip().setType(shipType);

            BigDecimal expected = tariff.getDiscountCoefficientsByShipType().get(testCase.getShip().getType());
            BigDecimal result = tonnageDueCalculator.evaluateDiscountCoefficient(testCase, tariff);

            assertThat(expected).isEqualByComparingTo(result);
        }

        @DisplayName("- by call count")
        @Test
        void testDiscountCoefficientByCallCount() {

            testCase.setCallCount(tariff.getCallCountThreshold());

            BigDecimal expected = tariff.getCallCountDiscountCoefficient();
            BigDecimal result = tonnageDueCalculator.evaluateDiscountCoefficient(testCase, tariff);

            assertThat(expected).isEqualByComparingTo(result);
        }

        @DisplayName("- should return zero by ship type not eligible for discount")
        @ParameterizedTest(name = "as : {arguments}")
        @EnumSource(value = ShipType.class, names = {"RECREATIONAL", "MILITARY", "SPECIAL"})
        void testDiscountCoefficientShouldReturnZeroByShipType(ShipType shipType) {

            testCase.getShip().setType(shipType);
            BigDecimal expected = BigDecimal.ZERO;
            BigDecimal result = tonnageDueCalculator.evaluateDiscountCoefficient(testCase, tariff);

            assertThat(expected).isEqualByComparingTo(result);
        }

        @DisplayName("- should return zero by call purpose not eligible for discount")
        @ParameterizedTest(name = "as : {arguments}")
        @EnumSource(value = CallPurpose.class, names = {"SPECIAL_PURPOSE_PORT_VISIT"})
        void testDiscountCoefficientShouldReturnZeroByCallPurpose(CallPurpose callPurpose) {

            testCase.setCallPurpose(callPurpose);

            BigDecimal expected = BigDecimal.ZERO;
            BigDecimal result = tonnageDueCalculator.evaluateDiscountCoefficient(testCase, tariff);

            assertThat(expected).isEqualByComparingTo(result);
        }

        @DisplayName("- should return biggest value")
        @Test
        void testDiscountCoefficientShouldReturnBiggestValue() {

            ShipType[] shipTypes = tariff.getDiscountCoefficientsByShipType().keySet().toArray(new ShipType[0]);
            CallPurpose[] callPurposes = tariff.getDiscountCoefficientsByCallPurpose().keySet().toArray(new CallPurpose[0]);

            testCase.setCallCount(tariff.getCallCountThreshold());
            testCase.getShip().setType(shipTypes[0]);
            testCase.setCallPurpose(callPurposes[0]);

            List<BigDecimal> discountCoefficients = Arrays.asList(
                    tariff.getCallCountDiscountCoefficient(),
                    tariff.getDiscountCoefficientsByShipType().get(shipTypes[0]),
                    tariff.getDiscountCoefficientsByCallPurpose().get(callPurposes[0]));

            BigDecimal expected = discountCoefficients.stream().max(Comparator.naturalOrder()).get();
            BigDecimal result = tonnageDueCalculator.evaluateDiscountCoefficient(testCase, tariff);

            assertThat(expected).isEqualByComparingTo(result);
        }
    }

    @DisplayName(value = "Tonnage due calculation after discount tests")
    @Nested
    class DueCalculationAfterDiscount {

        @DisplayName("- should return total due if discount is zero")
        @Test
        void testFinalDueIsEqualToTotalDue() {

            BigDecimal totalDue = BigDecimal.valueOf(25.50);
            BigDecimal discountCoefficient = BigDecimal.ZERO;

            BigDecimal result = tonnageDueCalculator.calculateDueAfterDiscount(totalDue, discountCoefficient);

            assertThat(totalDue).isEqualByComparingTo(result);
        }

        @DisplayName("- should return reduced total due decreased by discount")
        @Test
        void testFinalDueIsDecreasedByDiscount() {

            BigDecimal totalDue = BigDecimal.valueOf(25.50);
            BigDecimal discountCoefficient = BigDecimal.valueOf(0.7);
            BigDecimal discount = totalDue.multiply(discountCoefficient);

            BigDecimal expected = totalDue.subtract(discount);
            BigDecimal result = tonnageDueCalculator.calculateDueAfterDiscount(totalDue, discountCoefficient);

            assertThat(expected).isEqualByComparingTo(result);
        }
    }

}