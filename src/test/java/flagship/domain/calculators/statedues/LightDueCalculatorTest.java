package flagship.domain.calculators.statedues;

import com.fasterxml.jackson.databind.ObjectMapper;
import flagship.domain.calculators.DueCalculatorTest;
import flagship.domain.calculators.tariffs.stateduestariffs.LightDueTariff;
import flagship.domain.cases.dto.PdaCase;
import flagship.domain.cases.dto.PdaShip;
import flagship.domain.cases.entities.enums.ShipType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;
import java.util.Random;
import java.util.stream.Stream;

import static flagship.domain.cases.entities.enums.ShipType.GENERAL;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("Light due calculator tests")
public class LightDueCalculatorTest implements DueCalculatorTest {

  private static LightDueTariff tariff;
  private final LightDueCalculator lightDueCalculator = new LightDueCalculator();
  private PdaCase testCase;

  @BeforeAll
  public static void beforeClass() throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    tariff =
        mapper.readValue(new File("src/main/resources/lightDueTariff.json"), LightDueTariff.class);
  }

  @BeforeEach
  void setUp() {
    BigDecimal randomGrossTonnage = getRandomGrossTonnage();
    PdaShip testShip = PdaShip.builder().grossTonnage(randomGrossTonnage).type(GENERAL).build();
    testCase = PdaCase.builder().ship(testShip).callCount(1).build();
  }

  @DisplayName("Light due by gross tonnage")
  @Test
  void testLightDueByGrossTonnage() {

    ShipType shipType =
        Arrays.stream(ShipType.values())
            .filter(type -> !tariff.getLightDuesPerTonByShipType().containsKey(type))
            .findAny()
            .orElse(GENERAL);

    testCase.getShip().setType(shipType);

    BigDecimal expected = getLightDue();

    BigDecimal result = lightDueCalculator.calculateFor(testCase, tariff);

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Light due by ship type")
  @ParameterizedTest(name = "ship type: {arguments}")
  @MethodSource(value = "getShipTypesAffectingLightDue")
  void testLightDuePerShipType(ShipType shipType) {

    testCase.getShip().setType(shipType);

    BigDecimal expected = getLightDue();

    BigDecimal result = lightDueCalculator.calculateFor(testCase, tariff);

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Light due with discount by ship type")
  @ParameterizedTest(name = "ship type: {arguments}")
  @MethodSource(value = "getShipTypesEligibleForDiscount")
  void testLightDueWithDiscountByShipType(ShipType shipType) {

    testCase.getShip().setType(shipType);

    BigDecimal discountCoefficient =
        tariff.getDiscountCoefficientsByShipType().get(testCase.getShip().getType());

    BigDecimal expected = calculateDueAfterDiscount(discountCoefficient);
    BigDecimal result = lightDueCalculator.calculateFor(testCase, tariff);

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Light due with discount by call count")
  @Test
  void testLightDueWithDiscountByCallCount() {

    testCase.setCallCount(tariff.getCallCountThreshold());

    BigDecimal discountCoefficient = tariff.getCallCountDiscountCoefficient();

    BigDecimal expected = calculateDueAfterDiscount(discountCoefficient);
    BigDecimal result = lightDueCalculator.calculateFor(testCase, tariff);

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Light due with biggest discount")
  @Test
  void testLightDueWithBiggestDiscount() {

    ShipType shipType =
        tariff.getDiscountCoefficientsByShipType().keySet().stream().findAny().get();

    testCase.getShip().setType(shipType);
    testCase.setCallCount(tariff.getCallCountThreshold());

    BigDecimal callCountDiscountCoefficient = tariff.getCallCountDiscountCoefficient();
    BigDecimal shipTypeDiscountCoefficient =
        tariff.getDiscountCoefficientsByShipType().get(testCase.getShip().getType());

    BigDecimal discountCoefficient = callCountDiscountCoefficient.max(shipTypeDiscountCoefficient);

    BigDecimal expected = calculateDueAfterDiscount(discountCoefficient);
    BigDecimal result = lightDueCalculator.calculateFor(testCase, tariff);

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Light due without discount when ship type is not eligible for discount")
  @ParameterizedTest(name = "ship type : {arguments}")
  @MethodSource(value = "GetShipTypesNotEligibleForDiscount")
  void testLightDueWithZeroDiscountWhenShipTypeIsNotEligibleForDiscount(ShipType shipType) {

    testCase.getShip().setType(shipType);
    testCase.setCallCount(tariff.getCallCountThreshold());

    BigDecimal discountCoefficient = BigDecimal.ZERO;

    BigDecimal expected = calculateDueAfterDiscount(discountCoefficient);
    BigDecimal result = lightDueCalculator.calculateFor(testCase, tariff);

    assertThat(result).isEqualByComparingTo(expected);
  }

  private static Stream<Arguments> getShipTypesAffectingLightDue() {
    return tariff.getLightDuesPerTonByShipType().keySet().stream().map(Arguments::of);
  }

  private static Stream<Arguments> getShipTypesEligibleForDiscount() {
    return tariff.getDiscountCoefficientsByShipType().keySet().stream().map(Arguments::of);
  }

  private static Stream<Arguments> GetShipTypesNotEligibleForDiscount() {
    return tariff.getShipTypesNotEligibleForDiscount().stream().map(Arguments::of);
  }

  private BigDecimal calculateDueAfterDiscount(BigDecimal discountCoefficient) {
    BigDecimal lightDue = getLightDue();
    BigDecimal discount = lightDue.multiply(discountCoefficient);
    return lightDue.subtract(discount);
  }

  private BigDecimal getLightDue() {

    BigDecimal lightDue;

    if (tariff.getLightDuesPerTonByShipType().containsKey(testCase.getShip().getType())) {
      lightDue =
          testCase
              .getShip()
              .getGrossTonnage()
              .multiply(tariff.getLightDuesPerTonByShipType().get(testCase.getShip().getType()));
    } else {
      lightDue =
          tariff.getLightDuesByGrossTonnage().entrySet().stream()
              .filter(
                  entry ->
                      testCase.getShip().getGrossTonnage().intValue() >= entry.getValue()[0]
                          && testCase.getShip().getGrossTonnage().intValue() <= entry.getValue()[1])
              .findFirst()
              .map(Map.Entry::getKey)
              .orElse(tariff.getLightDueMaximumValue());
    }

    return lightDue.doubleValue() <= tariff.getLightDueMaximumValue().doubleValue()
        ? lightDue
        : BigDecimal.valueOf(tariff.getLightDueMaximumValue().doubleValue());
  }

  private BigDecimal getRandomGrossTonnage() {
    Random random = new Random();
    return BigDecimal.valueOf(random.ints(500, 200000).findFirst().getAsInt());
  }
}
