package flagship.domain.utils.calculators.statedues;

import com.fasterxml.jackson.databind.ObjectMapper;
import flagship.domain.cases.entities.Case;
import flagship.domain.cases.entities.Port;
import flagship.domain.cases.entities.Ship;
import flagship.domain.cases.entities.enums.PortArea;
import flagship.domain.cases.entities.enums.ShipType;
import flagship.domain.utils.calculators.DueCalculatorTest;
import flagship.domain.utils.tariffs.stateduestariffs.CanalDueTariff;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;

import static flagship.domain.cases.entities.enums.PortArea.FIRST;
import static flagship.domain.cases.entities.enums.ShipType.CONTAINER;
import static flagship.domain.cases.entities.enums.ShipType.GENERAL;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("Canal due calculator tests")
class CanalDueCalculatorTest implements DueCalculatorTest {

    private static CanalDueTariff tariff;
    private final CanalDueCalculator canalDueCalculator = new CanalDueCalculator();
    private Case testCase;
    private BigDecimal grossTonnage;

    @BeforeAll
    public static void beforeClass() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        tariff = mapper.readValue(new File("src/main/resources/canalDueTariff.json"), CanalDueTariff.class);
    }

    @BeforeEach
    void setUp() {
        Port testPort = Port.builder().area(FIRST).build();
        Ship testShip = Ship.builder().grossTonnage(1650).type(GENERAL).build();
        testCase = Case.builder().ship(testShip).port(testPort).callCount(1).build();
        grossTonnage = BigDecimal.valueOf(testShip.getGrossTonnage());
    }

    @DisplayName("Canal due calculation tests")
    @Nested
    class DueCalculationTest {

        @DisplayName("Canal due by port area")
        @ParameterizedTest(name = "port area : {arguments}")
        @EnumSource(PortArea.class)
        void testDefaultCanalDue(PortArea portArea) {

            testCase.getPort().setArea(portArea);

            BigDecimal canalDue = tariff.getCanalDuesByPortArea().get(testCase.getPort().getArea());

            BigDecimal expected = canalDue.multiply(grossTonnage);
            BigDecimal result = canalDueCalculator.calculateDue(testCase, tariff);

            assertThat(result).isEqualByComparingTo(expected);
        }
    }

    @DisplayName("Discount coefficient evaluation tests")
    @Nested()
    class DiscountCoefficientTest {

        @DisplayName("Default discount coefficient by call count")
        @Test
        void testDefaultDiscountCoefficientByCallCount() {

            testCase.setCallCount(tariff.getCallCountThreshold());

            BigDecimal expected = tariff.getDefaultCallCountDiscountCoefficient();
            BigDecimal result = canalDueCalculator.evaluateDiscountCoefficient(testCase, tariff);

            assertThat(result).isEqualByComparingTo(expected);
        }

        @DisplayName("Discount coefficient by ship type")
        @ParameterizedTest(name = "ship type: {arguments}")
        @EnumSource(value = ShipType.class, names = {"PASSENGER"})
        void testDiscountCoefficientByShipType(ShipType shipType) {

            testCase.getShip().setType(shipType);

            BigDecimal expected = tariff.getDiscountCoefficientByShipType().get(testCase.getShip().getType());
            BigDecimal result = canalDueCalculator.evaluateDiscountCoefficient(testCase, tariff);

            assertThat(result).isEqualByComparingTo(expected);
        }

        @DisplayName("Should return biggest discount coefficient value")
        @ParameterizedTest(name = "ship type: {arguments}")
        @EnumSource(value = ShipType.class, names = {"PASSENGER"})
        void testDiscountCoefficientReturnsBiggestValue(ShipType shipType) {

            testCase.getShip().setType(shipType);
            testCase.setCallCount(tariff.getCallCountThreshold());

            BigDecimal callCountDiscountCoefficient = tariff.getDefaultCallCountDiscountCoefficient();
            BigDecimal shipTypeDiscountCoefficient = tariff.getDiscountCoefficientByShipType().get(shipType);

            BigDecimal expected = callCountDiscountCoefficient.max(shipTypeDiscountCoefficient);
            BigDecimal result = canalDueCalculator.evaluateDiscountCoefficient(testCase, tariff);

            assertThat(result).isEqualByComparingTo(expected);
        }

        @DisplayName("Discount coefficient by port area for containers")
        @ParameterizedTest(name = "port area : {arguments}")
        @EnumSource(PortArea.class)
        void testDiscountCoefficientByPortAreaForContainers(PortArea portArea) {

            testCase.getPort().setArea(portArea);
            testCase.getShip().setType(CONTAINER);

            BigDecimal expected = tariff.getDiscountCoefficientsByPortAreaForContainers().get(portArea);
            BigDecimal result = canalDueCalculator.evaluateDiscountCoefficient(testCase, tariff);

            assertThat(result).isEqualByComparingTo(expected);
        }

        @DisplayName("Discount coefficient by port area and call count for containers")
        @ParameterizedTest(name = "port area : {arguments}")
        @EnumSource(PortArea.class)
        void discountCoefficientsByPortAreaPerCallCountForContainers(PortArea portArea) {

            testCase.getPort().setArea(portArea);
            testCase.getShip().setType(CONTAINER);
            testCase.setCallCount(tariff.getCallCountThreshold());

            BigDecimal discountCoefficientByPortAreaPerCallCount =
                    tariff.getDiscountCoefficientsByPortAreaPerCallCountForContainers().get(testCase.getPort().getArea());

            BigDecimal discountCoefficientByPortArea =
                    tariff.getDiscountCoefficientsByPortAreaForContainers().get(testCase.getPort().getArea());

            BigDecimal expected = discountCoefficientByPortArea.max(discountCoefficientByPortAreaPerCallCount);
            BigDecimal result = canalDueCalculator.evaluateDiscountCoefficient(testCase, tariff);

            assertThat(result).isEqualByComparingTo(expected);
        }

        @DisplayName("Should return zero if ship type is not eligible for discount")
        @ParameterizedTest(name = "ship type: {arguments}")
        @EnumSource(value = ShipType.class, names = {"MILITARY"})
        void testDiscountCoefficientReturnsZeroByShipType(ShipType shipType) {

            testCase.getShip().setType(shipType);
            testCase.setCallCount(tariff.getCallCountThreshold());

            BigDecimal expected = BigDecimal.ZERO;
            BigDecimal result = canalDueCalculator.evaluateDiscountCoefficient(testCase, tariff);

            assertThat(result).isEqualByComparingTo(expected);
        }
    }
}