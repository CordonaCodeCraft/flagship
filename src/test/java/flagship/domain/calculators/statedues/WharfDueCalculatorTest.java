package flagship.domain.calculators.statedues;

import flagship.domain.calculators.TariffsInitializer;
import flagship.domain.cases.dto.PdaCase;
import flagship.domain.cases.dto.PdaShip;
import flagship.domain.cases.entities.Case;
import flagship.domain.cases.entities.Ship;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.stream.Stream;

import static flagship.domain.cases.entities.Ship.ShipType.BULK_CARRIER;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("Wharf due calculator tests")
class WharfDueCalculatorTest extends TariffsInitializer {

  private final WharfDueCalculator calculator = new WharfDueCalculator();
  private PdaCase testCase;

  private static Stream<Arguments> getShipTypesAffectingWharfDue() {
    return wharfDueTariff.getWharfDuesByShipType().keySet().stream().map(Arguments::of);
  }

  private static Stream<Arguments> getCallPurposesEligibleForDiscount() {
    return wharfDueTariff.getDiscountCoefficientsByCallPurpose().keySet().stream()
        .map(Arguments::of);
  }

  private static Stream<Arguments> getShipTypesNotEligibleForDiscount() {
    return wharfDueTariff.getShipTypesNotEligibleForDiscount().stream().map(Arguments::of);
  }

  @BeforeEach
  void setUp() {
    PdaShip testShip =
        PdaShip.builder().lengthOverall(BigDecimal.valueOf(195.05)).type(BULK_CARRIER).build();
    testCase = PdaCase.builder().ship(testShip).alongsideDaysExpected(5).build();
  }

  @DisplayName("Should return default wharf due")
  @Test
  void testReturnsDefaultWharfDue() {

    final Ship.ShipType shipTypeWithDefaultWharfDue =
        Arrays.stream(Ship.ShipType.values())
            .filter(type -> !wharfDueTariff.getWharfDuesByShipType().containsKey(type))
            .findAny()
            .orElse(BULK_CARRIER);

    testCase.getShip().setType(shipTypeWithDefaultWharfDue);

    calculator.set(testCase, wharfDueTariff);

    final BigDecimal expected = calculateWharfDue();
    final BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Should return wharf due by ship type")
  @ParameterizedTest(name = "ship type : {arguments}")
  @MethodSource(value = "getShipTypesAffectingWharfDue")
  void testReturnsWharfDueByShipType(Ship.ShipType shipType) {

    testCase.getShip().setType(shipType);

    calculator.set(testCase, wharfDueTariff);

    final BigDecimal expected = calculateWharfDue();
    final BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Should return Wharf due with discount by call purpose")
  @ParameterizedTest(name = "call purpose : {arguments}")
  @MethodSource(value = "getCallPurposesEligibleForDiscount")
  void testReturnsWharfDueWithDiscountByCallPurpose(Case.CallPurpose callPurpose) {

    testCase.setCallPurpose(callPurpose);

    calculator.set(testCase, wharfDueTariff);

    final BigDecimal discountCoefficient =
        wharfDueTariff.getDiscountCoefficientsByCallPurpose().get(testCase.getCallPurpose());

    final BigDecimal expected = calculateDueAfterDiscount(discountCoefficient);
    final BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName(
      "Should return wharf due without discount when ship type is not eligible for discount")
  @ParameterizedTest(name = "ship type : {arguments}")
  @MethodSource(value = "getShipTypesNotEligibleForDiscount")
  void testReturnsWharfDueWithZeroDiscountWhenShipTypeIsNotEligibleForDiscount(Ship.ShipType shipType) {

    testCase.getShip().setType(shipType);

    calculator.set(testCase, wharfDueTariff);

    final BigDecimal discountCoefficient = BigDecimal.ZERO;

    final BigDecimal expected = calculateDueAfterDiscount(discountCoefficient);
    final BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }

  private BigDecimal calculateDueAfterDiscount(BigDecimal discountCoefficient) {
    final BigDecimal wharfDue = calculateWharfDue();
    final BigDecimal discount = wharfDue.multiply(discountCoefficient);
    return wharfDue.subtract(discount);
  }

  private BigDecimal calculateWharfDue() {

    final BigDecimal lengthOverall =
        BigDecimal.valueOf(Math.ceil(testCase.getShip().getLengthOverall().doubleValue()));

    final BigDecimal wharfDuePerMeter =
        wharfDueTariff
            .getWharfDuesByShipType()
            .getOrDefault(testCase.getShip().getType(), wharfDueTariff.getDefaultWharfDue())
            .getBase();

    final BigDecimal wharfDuePerLengthOverall = lengthOverall.multiply(wharfDuePerMeter);

    final BigDecimal alongsideHoursExpected =
        BigDecimal.valueOf(testCase.getAlongsideDaysExpected() * 24);

    return alongsideHoursExpected.multiply(wharfDuePerLengthOverall);
  }
}
