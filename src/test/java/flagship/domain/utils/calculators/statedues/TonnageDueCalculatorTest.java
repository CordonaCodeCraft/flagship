package flagship.domain.utils.calculators.statedues;

import com.fasterxml.jackson.databind.ObjectMapper;
import flagship.domain.cases.entities.Case;
import flagship.domain.cases.entities.Port;
import flagship.domain.cases.entities.Ship;
import flagship.domain.cases.entities.enums.CallPurpose;
import flagship.domain.cases.entities.enums.PortArea;
import flagship.domain.cases.entities.enums.ShipType;
import flagship.domain.utils.calculators.DueCalculatorTest;
import flagship.domain.utils.tariffs.stateduestariffs.TonnageDueTariff;
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
        Port testPort = Port.builder().area(FIRST).build();
        Ship testShip = Ship.builder().grossTonnage(1650).type(GENERAL).build();
        testCase = Case.builder().ship(testShip).callPurpose(LOADING).port(testPort).callCount(1).build();
        grossTonnage = BigDecimal.valueOf(testShip.getGrossTonnage());
    }

    @DisplayName("Tonnage due calculation tests")
    @Nested
    class DueCalculationTest {

        @DisplayName("Tonnage due by port area")
        @ParameterizedTest(name = "port area : {arguments}")
        @EnumSource(PortArea.class)
        void testDueByPortArea(PortArea portArea) {

            testCase.getPort().setArea(portArea);

            ShipType shipTypeDependantOnPortArea = Arrays.stream(ShipType.values())
                    .filter(type -> {
                                Set<ShipType> shipTypesAffectingTonnageDue =
                                        new HashSet<>(tariff.getTonnageDuesByShipType().keySet());
                                return !shipTypesAffectingTonnageDue.contains(type);
                            }
                    )
                    .findAny().orElse(GENERAL);

            CallPurpose callPurposeDependantOnPortArea = Arrays.stream(CallPurpose.values())
                    .filter(purpose -> {
                                Set<CallPurpose> callPurposesAffectingTonnageDue =
                                        new HashSet<>(tariff.getTonnageDuesByCallPurpose().keySet());
                                return !callPurposesAffectingTonnageDue.contains(purpose);
                            }
                    )
                    .findAny().orElse(LOADING);

            testCase.getShip().setType(shipTypeDependantOnPortArea);
            testCase.setCallPurpose(callPurposeDependantOnPortArea);

            BigDecimal duePerTon = tariff.getTonnageDuesByPortArea().get(testCase.getPort().getArea());

            BigDecimal expected = grossTonnage.multiply(duePerTon);
            BigDecimal result = tonnageDueCalculator.calculateDue(testCase, tariff);

            assertThat(result).isEqualByComparingTo(expected);
        }

        @DisplayName("Tonnage due by ship type")
        @ParameterizedTest(name = "ship type : {arguments}")
        @EnumSource(value = ShipType.class, names = {"OIL_TANKER", "RECREATIONAL", "MILITARY", "SPECIAL"})
        void testDueByShipType(ShipType shipType) {

            testCase.getShip().setType(shipType);

            BigDecimal duePerTon = tariff.getTonnageDuesByShipType().get(testCase.getShip().getType());

            BigDecimal expected = grossTonnage.multiply(duePerTon);
            BigDecimal result = tonnageDueCalculator.calculateDue(testCase, tariff);

            assertThat(result).isEqualByComparingTo(expected);
        }

        @DisplayName("Tonnage due by call purpose")
        @ParameterizedTest(name = "call purpose : {arguments}")
        @EnumSource(value = CallPurpose.class, names = {"SPECIAL_PURPOSE_PORT_VISIT"})
        void testDueByCallPurpose(CallPurpose callPurpose) {

            testCase.setCallPurpose(callPurpose);

            BigDecimal duePerTon = tariff.getTonnageDuesByCallPurpose().get(testCase.getCallPurpose());

            BigDecimal expected = grossTonnage.multiply(duePerTon);
            BigDecimal result = tonnageDueCalculator.calculateDue(testCase, tariff);

            assertThat(result).isEqualByComparingTo(expected);
        }

    }

    @DisplayName("Discount coefficient evaluation tests")
    @Nested
    class DiscountCoefficientEvaluationTest {

        @DisplayName("Discount coefficient by call count")
        @Test
        void testDiscountCoefficientByCallCount() {

            testCase.setCallCount(tariff.getCallCountThreshold());

            BigDecimal expected = tariff.getCallCountDiscountCoefficient();
            BigDecimal result = tonnageDueCalculator.evaluateDiscountCoefficient(testCase, tariff);

            assertThat(result).isEqualByComparingTo(expected);
        }

        @DisplayName("Discount coefficient by call purpose")
        @ParameterizedTest(name = "call purpose : {arguments}")
        @EnumSource(value = CallPurpose.class, names = {"RESUPPLY", "RECRUITMENT", "POSTAL", "REPAIR"})
        void testDiscountCoefficientByCallPurpose(CallPurpose callPurpose) {

            testCase.setCallPurpose(callPurpose);

            BigDecimal expected = tariff.getDiscountCoefficientsByCallPurpose().get(testCase.getCallPurpose());
            BigDecimal result = tonnageDueCalculator.evaluateDiscountCoefficient(testCase, tariff);

            assertThat(result).isEqualByComparingTo(expected);
        }

        @DisplayName("Discount coefficient by ship type")
        @ParameterizedTest(name = "ship type : {arguments}")
        @EnumSource(value = ShipType.class, names = {"REEFER", "CONTAINER", "PASSENGER"})
        void testDiscountCoefficientByShipType(ShipType shipType) {

            testCase.getShip().setType(shipType);

            BigDecimal expected = tariff.getDiscountCoefficientsByShipType().get(testCase.getShip().getType());
            BigDecimal result = tonnageDueCalculator.evaluateDiscountCoefficient(testCase, tariff);

            assertThat(result).isEqualByComparingTo(expected);
        }

        @DisplayName("Should return biggest discount coefficient value")
        @Test
        void testDiscountCoefficientReturnsBiggestValue() {

            ShipType[] shipTypesAffectingDiscountCoefficient =
                    tariff.getDiscountCoefficientsByShipType().keySet().toArray(new ShipType[0]);
            CallPurpose[] callPurposesAffectingDiscountCoefficient =
                    tariff.getDiscountCoefficientsByCallPurpose().keySet().toArray(new CallPurpose[0]);

            testCase.setCallCount(tariff.getCallCountThreshold());
            testCase.getShip().setType(shipTypesAffectingDiscountCoefficient[0]);
            testCase.setCallPurpose(callPurposesAffectingDiscountCoefficient[0]);

            List<BigDecimal> discountCoefficients = Arrays.asList(
                    tariff.getCallCountDiscountCoefficient(),
                    tariff.getDiscountCoefficientsByShipType().get(shipTypesAffectingDiscountCoefficient[0]),
                    tariff.getDiscountCoefficientsByCallPurpose().get(callPurposesAffectingDiscountCoefficient[0]));

            BigDecimal expected = discountCoefficients.stream().max(Comparator.naturalOrder()).get();
            BigDecimal result = tonnageDueCalculator.evaluateDiscountCoefficient(testCase, tariff);

            assertThat(result).isEqualByComparingTo(expected);
        }

        @DisplayName("Should return zero if ship type is not eligible for discount")
        @ParameterizedTest(name = "ship type : {arguments}")
        @EnumSource(value = ShipType.class, names = {"RECREATIONAL", "MILITARY", "SPECIAL"})
        void testDiscountCoefficientReturnsZeroByShipType(ShipType shipType) {

            testCase.getShip().setType(shipType);
            testCase.setCallPurpose(tariff.getDiscountCoefficientsByCallPurpose().keySet().stream().findAny().get());
            testCase.setCallCount(tariff.getCallCountThreshold());

            BigDecimal expected = BigDecimal.ZERO;
            BigDecimal result = tonnageDueCalculator.evaluateDiscountCoefficient(testCase, tariff);

            assertThat(result).isEqualByComparingTo(expected);
        }

        @DisplayName("Should return zero if call purpose is not eligible for discount")
        @ParameterizedTest(name = "call purpose : {arguments}")
        @EnumSource(value = CallPurpose.class, names = {"SPECIAL_PURPOSE_PORT_VISIT"})
        void testDiscountCoefficientReturnsZeroByCallPurpose(CallPurpose callPurpose) {

            testCase.setCallPurpose(callPurpose);
            testCase.getShip().setType(tariff.getDiscountCoefficientsByShipType().keySet().stream().findAny().get());
            testCase.setCallCount(tariff.getCallCountThreshold());

            BigDecimal expected = BigDecimal.ZERO;
            BigDecimal result = tonnageDueCalculator.evaluateDiscountCoefficient(testCase, tariff);

            assertThat(result).isEqualByComparingTo(expected);
        }

    }
}