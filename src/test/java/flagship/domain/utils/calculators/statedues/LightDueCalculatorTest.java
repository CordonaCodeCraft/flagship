package flagship.domain.utils.calculators.statedues;

import com.fasterxml.jackson.databind.ObjectMapper;
import flagship.domain.cases.entities.Case;
import flagship.domain.cases.entities.Ship;
import flagship.domain.cases.entities.enums.ShipType;
import flagship.domain.utils.calculators.DueCalculatorTest;
import flagship.domain.utils.tariffs.stateduestariffs.LightDueTariff;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static flagship.domain.cases.entities.enums.ShipType.GENERAL;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("Light due calculator tests")
public class LightDueCalculatorTest implements DueCalculatorTest {

    private static LightDueTariff tariff;
    private final LightDueCalculator lightDueCalculator = new LightDueCalculator();
    private Case testCase;
    private Integer grossTonnage;

    @BeforeAll
    public static void beforeClass() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        tariff = mapper.readValue(new File("src/main/resources/lightDueTariff.json"), LightDueTariff.class);
    }

    @BeforeEach
    void setUp() {
        Ship testShip = Ship.builder().grossTonnage(1650).type(GENERAL).build();
        testCase = Case.builder().ship(testShip).callCount(1).build();
        grossTonnage = testShip.getGrossTonnage();
    }

    @DisplayName("Light due calculation tests")
    @Nested
    class DueCalculationTest {

        @DisplayName("Light due by gross tonnage")
        @Test
        void testLightDueByGrossTonnage() {

            ShipType shipType = Arrays.stream(ShipType.values())
                    .filter(type -> {
                                Set<ShipType> shipTypesAffectingLightDue =
                                        new HashSet<>(tariff.getLightDuesPerTonByShipType().keySet());
                                return !shipTypesAffectingLightDue.contains(type);
                            }
                    )
                    .findAny().orElse(GENERAL);

            testCase.getShip().setType(shipType);

            BigDecimal expected = tariff.getLightDuesByGrossTonnage()
                    .entrySet()
                    .stream()
                    .filter(entry -> grossTonnage >= entry.getValue()[0] && grossTonnage <= entry.getValue()[1])
                    .findFirst()
                    .map(Map.Entry::getKey)
                    .orElse(tariff.getLightDueMaximumValue());

            BigDecimal result = lightDueCalculator.calculateDue(testCase, tariff);

            assertThat(result).isEqualByComparingTo(expected);
        }

        @DisplayName("Light due by ship type")
        @ParameterizedTest(name = "ship type: {arguments}")
        @EnumSource(value = ShipType.class, names = {"MILITARY"})
        void testLightDuePerShipType(ShipType shipType) {

            testCase.getShip().setType(shipType);

            BigDecimal lightDuePerTon = tariff.getLightDuesPerTonByShipType().get(shipType);
            BigDecimal lightDueTotal = lightDuePerTon.multiply(BigDecimal.valueOf(grossTonnage));

            BigDecimal expected = lightDueTotal.doubleValue() <= 150 ? lightDueTotal : BigDecimal.valueOf(150);
            BigDecimal result = lightDueCalculator.calculateDue(testCase, tariff);

            assertThat(result).isEqualByComparingTo(expected);
        }
    }

    @DisplayName("Discount coefficient evaluation tests")
    @Nested
    class DiscountCoefficientEvaluationTest {

        @DisplayName("Discount coefficient by ship type")
        @ParameterizedTest(name = "ship type: {arguments}")
        @EnumSource(value = ShipType.class, names = {"PASSENGER"})
        void testDiscountCoefficientByShipType(ShipType shipType) {

            testCase.getShip().setType(shipType);

            BigDecimal expected = tariff.getDiscountCoefficientsByShipType().get(shipType);
            BigDecimal result = lightDueCalculator.evaluateDiscountCoefficient(testCase, tariff);

            assertThat(result).isEqualByComparingTo(expected);
        }


        @DisplayName("Discount coefficient by call count")
        @Test
        void testDiscountCoefficientByCallCount() {

            testCase.setCallCount(tariff.getCallCountThreshold());

            BigDecimal expected = tariff.getCallCountDiscountCoefficient();
            BigDecimal result = lightDueCalculator.evaluateDiscountCoefficient(testCase, tariff);

            assertThat(result).isEqualByComparingTo(expected);
        }

        @DisplayName("Should return biggest discount coefficient value")
        @Test
        void testDiscountCoefficientReturnsBiggestValue() {

            ShipType shipType = tariff.getDiscountCoefficientsByShipType().keySet().stream().findAny().get();

            testCase.getShip().setType(shipType);
            testCase.setCallCount(tariff.getCallCountThreshold());

            BigDecimal callCountDiscountCoefficient =
                    tariff.getCallCountDiscountCoefficient();
            BigDecimal shipTypeDiscountCoefficient =
                    tariff.getDiscountCoefficientsByShipType().get(testCase.getShip().getType());

            BigDecimal expected = callCountDiscountCoefficient.max(shipTypeDiscountCoefficient);
            BigDecimal result = lightDueCalculator.evaluateDiscountCoefficient(testCase, tariff);

            assertThat(result).isEqualByComparingTo(expected);
        }

        @DisplayName("Should return zero if ship type is not eligible for discount")
        @ParameterizedTest(name = "ship type : {arguments}")
        @EnumSource(value = ShipType.class, names = {"MILITARY"})
        void testDiscountCoefficientReturnsZeroByShipType(ShipType shipType) {

            testCase.getShip().setType(shipType);
            testCase.setCallCount(tariff.getCallCountThreshold());

            BigDecimal expected = BigDecimal.ZERO;
            BigDecimal result = lightDueCalculator.evaluateDiscountCoefficient(testCase, tariff);

            assertThat(result).isEqualByComparingTo(expected);
        }
    }


}
