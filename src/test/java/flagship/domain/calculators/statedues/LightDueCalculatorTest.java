package flagship.domain.calculators.statedues;

import flagship.domain.calculators.BaseCalculatorTest;
import flagship.domain.cases.dto.PdaCase;
import flagship.domain.cases.dto.PdaShip;
import flagship.domain.cases.entities.enums.ShipType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.stream.Stream;

import static flagship.domain.cases.entities.enums.ShipType.BULK_CARRIER;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("Light due calculator tests")
public class LightDueCalculatorTest extends BaseCalculatorTest {

  private final LightDueCalculator calculator = new LightDueCalculator();

  private static Stream<Arguments> getShipTypesAffectingLightDue() {
    return lightDueTariff.getLightDuesPerTonByShipType().keySet().stream().map(Arguments::of);
  }

  private static Stream<Arguments> getShipTypesEligibleForDiscount() {
    return lightDueTariff.getDiscountCoefficientsByShipType().keySet().stream().map(Arguments::of);
  }

  private static Stream<Arguments> GetShipTypesNotEligibleForDiscount() {
    return lightDueTariff.getShipTypesNotEligibleForDiscount().stream().map(Arguments::of);
  }

  @BeforeEach
  void setUp() {
    PdaShip testShip =
        PdaShip.builder()
            .grossTonnage(getRandomGrossTonnage(MIN_GT, MAX_GT))
            .type(BULK_CARRIER)
            .build();
    testCase = PdaCase.builder().ship(testShip).callCount(1).build();
  }

  @DisplayName("Should return light due by gross tonnage")
  @Test
  void testLightDueByGrossTonnage() {

    final ShipType shipType =
        Arrays.stream(ShipType.values())
            .filter(type -> !lightDueTariff.getLightDuesPerTonByShipType().containsKey(type))
            .findAny()
            .get();

    testCase.getShip().setType(shipType);

    calculator.set(testCase, lightDueTariff);

    final BigDecimal expected = getDueByRange(lightDueTariff.getLightDuesByGrossTonnage());
    final BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Should return light due by ship type")
  @ParameterizedTest(name = "ship type: {arguments}")
  @MethodSource(value = "getShipTypesAffectingLightDue")
  void testLightDuePerShipType(ShipType shipType) {

    testCase.getShip().setType(shipType);

    calculator.set(testCase, lightDueTariff);

    final BigDecimal baseDue =
        getDueByValue(shipType, lightDueTariff.getLightDuesPerTonByShipType());
    final BigDecimal lightDueTotal = baseDue.multiply(testCase.getShip().getGrossTonnage());

    final BigDecimal expected =
        lightDueTotal.doubleValue() <= lightDueTariff.getLightDueMaximumValue().doubleValue()
            ? lightDueTotal
            : lightDueTariff.getLightDueMaximumValue();

    final BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Should return light due with discount by ship type")
  @ParameterizedTest(name = "ship type: {arguments}")
  @MethodSource(value = "getShipTypesEligibleForDiscount")
  void testLightDueWithDiscountByShipType(ShipType shipType) {

    testCase.getShip().setType(shipType);

    calculator.set(testCase, lightDueTariff);

    final BigDecimal baseDue = getDueByRange(lightDueTariff.getLightDuesByGrossTonnage());
    final BigDecimal coefficient = lightDueTariff.getDiscountCoefficientsByShipType().get(shipType);

    final BigDecimal expected = baseDue.subtract(baseDue.multiply(coefficient));
    final BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Should return light due with discount by call count")
  @Test
  void testLightDueWithDiscountByCallCount() {

    testCase.setCallCount(lightDueTariff.getCallCountThreshold());

    calculator.set(testCase, lightDueTariff);

    final BigDecimal baseDue = getDueByRange(lightDueTariff.getLightDuesByGrossTonnage());
    final BigDecimal coefficient = lightDueTariff.getCallCountDiscountCoefficient();

    final BigDecimal expected = baseDue.subtract(baseDue.multiply(coefficient));
    final BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Should return light due with biggest discount")
  @Test
  void testLightDueWithBiggestDiscount() {

    ShipType shipType =
        lightDueTariff.getDiscountCoefficientsByShipType().keySet().stream().findAny().get();

    testCase.getShip().setType(shipType);
    testCase.setCallCount(lightDueTariff.getCallCountThreshold());

    calculator.set(testCase, lightDueTariff);

    final BigDecimal baseDue = getDueByRange(lightDueTariff.getLightDuesByGrossTonnage());

    final BigDecimal firstCoefficient = lightDueTariff.getCallCountDiscountCoefficient();
    final BigDecimal secondCoefficient =
        lightDueTariff.getDiscountCoefficientsByShipType().get(shipType);
    final BigDecimal finalCoefficient = firstCoefficient.max(secondCoefficient);

    final BigDecimal expected = baseDue.subtract(baseDue.multiply(finalCoefficient));
    final BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName(
      "Should return light due without discount when ship type is not eligible for discount")
  @ParameterizedTest(name = "ship type : {arguments}")
  @MethodSource(value = "GetShipTypesNotEligibleForDiscount")
  void testLightDueWithZeroDiscountWhenShipTypeIsNotEligibleForDiscount(ShipType shipType) {

    testCase.getShip().setType(shipType);
    testCase.setCallCount(lightDueTariff.getCallCountThreshold());

    calculator.set(testCase, lightDueTariff);

    final BigDecimal expected = getDueByRange(lightDueTariff.getLightDuesByGrossTonnage());
    final BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }
}
