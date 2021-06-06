package flagship.domain.calculation.calculators.state;

import flagship.domain.calculation.calculators.TariffsInitializer;
import flagship.domain.pda.model.PdaCase;
import flagship.domain.port.entity.Port;
import flagship.domain.port.model.PdaPort;
import flagship.domain.ship.entity.Ship;
import flagship.domain.ship.model.PdaShip;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.Random;
import java.util.stream.Stream;

import static flagship.domain.port.entity.Port.PortArea.FIRST;
import static flagship.domain.ship.entity.Ship.ShipType.BULK_CARRIER;
import static flagship.domain.ship.entity.Ship.ShipType.CONTAINER;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("Canal due calculator tests")
class CanalDueCalculatorTest extends TariffsInitializer {

  private final CanalDueCalculator calculator = new CanalDueCalculator();
  private PdaCase testCase;
  private BigDecimal grossTonnage;

  private static Stream<Arguments> getShipTypesEligibleForDiscount() {
    return canalDueTariff.getDiscountCoefficientByShipType().keySet().stream().map(Arguments::of);
  }

  private static Stream<Arguments> getShipTypesNotEligibleForDiscount() {
    return canalDueTariff.getShipTypesNotEligibleForDiscount().stream().map(Arguments::of);
  }

  @BeforeEach
  void setUp() {
    BigDecimal randomGrossTonnage = getRandomGrossTonnage();
    PdaPort testPort = PdaPort.builder().portArea(FIRST).build();
    PdaShip testShip =
        PdaShip.builder().grossTonnage(randomGrossTonnage).type(BULK_CARRIER).build();
    testCase = PdaCase.builder().ship(testShip).port(testPort).callCount(1).build();
    grossTonnage = testShip.getGrossTonnage();
  }

  @DisplayName("Should return canal due by port area")
  @ParameterizedTest(name = "port area : {arguments}")
  @EnumSource(Port.PortArea.class)
  void testReturnsDefaultCanalDue(final Port.PortArea portArea) {

    testCase.getPort().setPortArea(portArea);

    calculator.set(testCase, canalDueTariff);

    final BigDecimal baseDue =
        canalDueTariff.getCanalDuesByPortArea().get(testCase.getPort().getPortArea()).getBase();

    final BigDecimal expected = baseDue.multiply(grossTonnage);
    final BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Should return canal due with default discount by call count")
  @Test
  void testReturnsCanalDueWithDefaultDiscountByCallCount() {

    testCase.setCallCount(canalDueTariff.getCallCountThreshold());

    calculator.set(testCase, canalDueTariff);

    final BigDecimal discountCoefficient = canalDueTariff.getDefaultCallCountDiscountCoefficient();

    final BigDecimal expected = calculateDueAfterDiscount(discountCoefficient);
    final BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Should return canal due with discount by ship type")
  @ParameterizedTest(name = "ship type: {arguments}")
  @MethodSource(value = "getShipTypesEligibleForDiscount")
  void testReturnsCanalDueWithDiscountByShipType(final Ship.ShipType shipType) {

    testCase.getShip().setType(shipType);

    calculator.set(testCase, canalDueTariff);

    final BigDecimal discountCoefficient =
        canalDueTariff.getDiscountCoefficientByShipType().get(testCase.getShip().getType());

    final BigDecimal expected = calculateDueAfterDiscount(discountCoefficient);
    final BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Should return canal due with biggest discount")
  @ParameterizedTest(name = "ship type: {arguments}")
  @MethodSource(value = "getShipTypesEligibleForDiscount")
  void testReturnsCanalDueWithBiggestDiscount(final Ship.ShipType shipType) {

    testCase.getShip().setType(shipType);
    testCase.setCallCount(canalDueTariff.getCallCountThreshold());

    calculator.set(testCase, canalDueTariff);

    final BigDecimal first = canalDueTariff.getDefaultCallCountDiscountCoefficient();
    final BigDecimal second = canalDueTariff.getDiscountCoefficientByShipType().get(shipType);

    final BigDecimal discountCoefficient = first.max(second);

    final BigDecimal expected = calculateDueAfterDiscount(discountCoefficient);
    final BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Should return canal due for containers with discount by port area")
  @ParameterizedTest(name = "port area : {arguments}")
  @EnumSource(Port.PortArea.class)
  void testReturnsCanalDueForContainersWithDiscountByPortArea(final Port.PortArea portArea) {

    testCase.getPort().setPortArea(portArea);
    testCase.getShip().setType(CONTAINER);

    calculator.set(testCase, canalDueTariff);

    BigDecimal discountCoefficient =
        canalDueTariff.getDiscountCoefficientsByPortAreaForContainers().get(portArea);

    BigDecimal expected = calculateDueAfterDiscount(discountCoefficient);
    BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Should return canal due for containers with discount by port area and call count")
  @ParameterizedTest(name = "port area : {arguments}")
  @EnumSource(Port.PortArea.class)
  void testReturnsCanalDueForContainersWithDiscountByPortAreaAndCallCount(Port.PortArea portArea) {

    testCase.getPort().setPortArea(portArea);
    testCase.getShip().setType(CONTAINER);
    testCase.setCallCount(canalDueTariff.getCallCountThreshold());

    calculator.set(testCase, canalDueTariff);

    BigDecimal discountCoefficientByPortArea =
        canalDueTariff
            .getDiscountCoefficientsByPortAreaForContainers()
            .get(testCase.getPort().getPortArea());

    BigDecimal discountCoefficientByPortAreaPerCallCount =
        canalDueTariff
            .getDiscountCoefficientsByPortAreaPerCallCountForContainers()
            .get(testCase.getPort().getPortArea());

    BigDecimal discountCoefficient =
        discountCoefficientByPortArea.max(discountCoefficientByPortAreaPerCallCount);

    BigDecimal expected = calculateDueAfterDiscount(discountCoefficient);
    BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName(
      "Should return canal due without discount when ship type is not eligible for discount")
  @ParameterizedTest(name = "ship type: {arguments}")
  @MethodSource(value = "getShipTypesNotEligibleForDiscount")
  void testReturnsCanalDueWithoutDiscountWhenShipTypeIsNotEligibleForDiscount(
      Ship.ShipType shipType) {

    testCase.getShip().setType(shipType);
    testCase.setCallCount(canalDueTariff.getCallCountThreshold());

    calculator.set(testCase, canalDueTariff);

    final BigDecimal discountCoefficient = BigDecimal.ZERO;

    final BigDecimal expected = calculateDueAfterDiscount(discountCoefficient);
    final BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }

  BigDecimal calculateDueAfterDiscount(BigDecimal discountCoefficient) {
    BigDecimal duePerGrossTon =
        canalDueTariff.getCanalDuesByPortArea().get(testCase.getPort().getPortArea()).getBase();
    BigDecimal dueTotal = testCase.getShip().getGrossTonnage().multiply(duePerGrossTon);
    BigDecimal discount = dueTotal.multiply(discountCoefficient);
    return dueTotal.subtract(discount);
  }

  private BigDecimal getRandomGrossTonnage() {
    Random random = new Random();
    return BigDecimal.valueOf(random.ints(500, 200000).findFirst().getAsInt());
  }
}
