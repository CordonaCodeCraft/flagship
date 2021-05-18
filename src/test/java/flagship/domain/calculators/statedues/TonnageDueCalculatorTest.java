package flagship.domain.calculators.statedues;

import flagship.domain.calculators.TariffsInitializer;
import flagship.domain.cases.dto.PdaCase;
import flagship.domain.cases.dto.PdaPort;
import flagship.domain.cases.dto.PdaShip;
import flagship.domain.cases.entities.enums.CallPurpose;
import flagship.domain.cases.entities.enums.ShipType;
import flagship.domain.tariffs.PortArea;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import static flagship.domain.calculators.BaseCalculatorTest.MAX_GT;
import static flagship.domain.calculators.BaseCalculatorTest.MIN_GT;
import static flagship.domain.cases.entities.enums.CallPurpose.LOADING;
import static flagship.domain.cases.entities.enums.ShipType.BULK_CARRIER;
import static flagship.domain.cases.entities.enums.ShipType.WORK_SHIP;
import static flagship.domain.tariffs.PortArea.FIRST;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("Tonnage due calculator tests")
class TonnageDueCalculatorTest extends TariffsInitializer {

  private final TonnageDueCalculator calculator = new TonnageDueCalculator();
  private PdaCase testCase;
  private BigDecimal grossTonnage;

  private static Stream<Arguments> getShipTypesAffectingTonnageDue() {
    return tonnageDueTariff.getTonnageDuesByShipType().keySet().stream().map(Arguments::of);
  }

  private static Stream<Arguments> getCallPurposesAffectingTonnageDue() {
    return tonnageDueTariff.getTonnageDuesByCallPurpose().keySet().stream().map(Arguments::of);
  }

  private static Stream<Arguments> getCallPurposesEligibleForDiscount() {
    return tonnageDueTariff.getDiscountCoefficientsByCallPurpose().keySet().stream()
        .map(Arguments::of);
  }

  private static Stream<Arguments> getShipTypesEligibleForDiscount() {
    return tonnageDueTariff.getDiscountCoefficientsByShipType().keySet().stream()
        .map(Arguments::of);
  }

  private static Stream<Arguments> getShipTypesNotEligibleForDiscount() {
    return tonnageDueTariff.getShipTypesNotEligibleForDiscount().stream().map(Arguments::of);
  }

  private static Stream<Arguments> getCallPurposesNotEligibleForDiscount() {
    return tonnageDueTariff.getCallPurposesNotEligibleForDiscount().stream().map(Arguments::of);
  }

  @BeforeEach
  void setUp() {
    BigDecimal randomGrossTonnage = getRandomGrossTonnage(MIN_GT, MAX_GT);
    PdaPort testPort = PdaPort.builder().portArea(FIRST).build();
    PdaShip testShip =
        PdaShip.builder().grossTonnage(randomGrossTonnage).type(BULK_CARRIER).build();
    testCase =
        PdaCase.builder()
            .ship(testShip)
            .port(testPort)
            .callPurpose(LOADING)
            .callCount(1)
            .arrivesFromBulgarianPort(false)
            .build();
    grossTonnage = testShip.getGrossTonnage();
  }

  @DisplayName("Should return tonnage due by port area")
  @ParameterizedTest(name = "port area : {arguments}")
  @EnumSource(PortArea.class)
  void testReturnsTonnageDueByPortArea(final PortArea portArea) {

    final ShipType shipType =
        Arrays.stream(ShipType.values())
            .filter(type -> !tonnageDueTariff.getTonnageDuesByShipType().containsKey(type))
            .findAny()
            .orElse(BULK_CARRIER);

    final CallPurpose callPurpose =
        Arrays.stream(CallPurpose.values())
            .filter(purpose -> !tonnageDueTariff.getTonnageDuesByCallPurpose().containsKey(purpose))
            .findAny()
            .orElse(LOADING);

    testCase.getShip().setType(shipType);
    testCase.setCallPurpose(callPurpose);
    testCase.getPort().setPortArea(portArea);

    calculator.set(testCase, tonnageDueTariff);

    final BigDecimal baseDue = tonnageDueTariff.getTonnageDuesByPortArea().get(portArea).getBase();

    final BigDecimal expected = grossTonnage.multiply(baseDue);
    final BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Should return tonnage due by ship type")
  @ParameterizedTest(name = "ship type : {arguments}")
  @MethodSource(value = "getShipTypesAffectingTonnageDue")
  void testReturnsTonnageDueByShipType(final ShipType shipType) {

    testCase.getShip().setType(shipType);

    calculator.set(testCase, tonnageDueTariff);

    final BigDecimal baseDue = tonnageDueTariff.getTonnageDuesByShipType().get(shipType).getBase();

    final BigDecimal expected = grossTonnage.multiply(baseDue);
    final BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Should return tonnage due for ship type 'Special'")
  @Test
  void testIncreasesTonnageDueByExpectedMonthCount() {

    testCase.getShip().setType(WORK_SHIP);
    testCase.setEstimatedDateOfArrival(LocalDate.of(2021, 1, 10));
    testCase.setEstimatedDateOfDeparture(LocalDate.of(2021, 1, 10));

    calculator.set(testCase, tonnageDueTariff);

    final BigDecimal baseDue = tonnageDueTariff.getTonnageDuesByShipType().get(WORK_SHIP).getBase();
    BigDecimal expected = grossTonnage.multiply(baseDue);

    if (testCase.getEstimatedDateOfArrival().getMonthValue()
        != testCase.getEstimatedDateOfDeparture().getMonthValue()) {
      final long multiplier =
          ChronoUnit.MONTHS.between(
                  YearMonth.from(testCase.getEstimatedDateOfArrival()),
                  YearMonth.from(testCase.getEstimatedDateOfDeparture()))
              + 1;
      expected = expected.multiply(BigDecimal.valueOf(multiplier));
    }

    final BigDecimal result = calculator.calculate();
    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Should return tonnage due by call purpose")
  @ParameterizedTest(name = "call purpose : {arguments}")
  @MethodSource(value = "getCallPurposesAffectingTonnageDue")
  void testReturnsTonnageDueByCallPurpose(final CallPurpose callPurpose) {

    testCase.setCallPurpose(callPurpose);

    calculator.set(testCase, tonnageDueTariff);

    final BigDecimal baseDue =
        tonnageDueTariff.getTonnageDuesByCallPurpose().get(testCase.getCallPurpose()).getBase();

    final BigDecimal expected = grossTonnage.multiply(baseDue);
    final BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Should return tonnage due with discount by port of arrival")
  @Test
  void testReturnsTonnageDueWithDiscountByPortOfArrival() {

    testCase.setArrivesFromBulgarianPort(true);

    calculator.set(testCase, tonnageDueTariff);

    final BigDecimal discountCoefficient =
        tonnageDueTariff.getDiscountCoefficientForPortOfArrival();

    final BigDecimal expected = calculateDueAfterDiscount(discountCoefficient);
    final BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Should return tonnage due with discount by call count")
  @Test
  void testReturnsTonnageDueWithDiscountByCallCount() {

    testCase.setCallCount(tonnageDueTariff.getCallCountThreshold());

    calculator.set(testCase, tonnageDueTariff);

    final BigDecimal discountCoefficient = tonnageDueTariff.getCallCountDiscountCoefficient();

    final BigDecimal expected = calculateDueAfterDiscount(discountCoefficient);
    final BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Should return due with discount by call purpose")
  @ParameterizedTest(name = "call purpose : {arguments}")
  @MethodSource(value = "getCallPurposesEligibleForDiscount")
  void testReturnsTonnageDueWithDiscountByCallPurpose(CallPurpose callPurpose) {

    testCase.setCallPurpose(callPurpose);

    calculator.set(testCase, tonnageDueTariff);

    final BigDecimal discountCoefficient =
        tonnageDueTariff.getDiscountCoefficientsByCallPurpose().get(callPurpose);

    final BigDecimal expected = calculateDueAfterDiscount(discountCoefficient);
    final BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Should return tonnage due with discount by ship type")
  @ParameterizedTest(name = "ship type : {arguments}")
  @MethodSource(value = "getShipTypesEligibleForDiscount")
  void testReturnsTonnageDueWithDiscountByShipType(final ShipType shipType) {

    testCase.getShip().setType(shipType);

    calculator.set(testCase, tonnageDueTariff);

    final BigDecimal discountCoefficient =
        tonnageDueTariff.getDiscountCoefficientsByShipType().get(testCase.getShip().getType());

    final BigDecimal expected = calculateDueAfterDiscount(discountCoefficient);
    final BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Should return tonnage due with biggest discount")
  @Test
  void testReturnsTonnageDueWithBiggestDiscount() {

    testCase.getShip().setType(getShipTypeEligibleForDiscount());
    testCase.setCallPurpose(getCallPurposeEligibleForDiscount());
    testCase.setCallCount(tonnageDueTariff.getCallCountThreshold());

    calculator.set(testCase, tonnageDueTariff);

    final List<BigDecimal> discountCoefficients =
        Arrays.asList(
            tonnageDueTariff.getDiscountCoefficientsByShipType().get(testCase.getShip().getType()),
            tonnageDueTariff.getDiscountCoefficientsByCallPurpose().get(testCase.getCallPurpose()),
            tonnageDueTariff.getCallCountDiscountCoefficient());

    final BigDecimal discountCoefficient =
        discountCoefficients.stream().max(Comparator.naturalOrder()).get();

    final BigDecimal expected = calculateDueAfterDiscount(discountCoefficient);
    final BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName(
      "Should return tonnage due without discount when ship type is not eligible for discount")
  @ParameterizedTest(name = "ship type : {arguments}")
  @MethodSource(value = "getShipTypesNotEligibleForDiscount")
  void testReturnsTonnageDueWithoutDiscountByShipType(final ShipType shipType) {

    testCase.getShip().setType(shipType);
    testCase.setCallPurpose(getCallPurposeEligibleForDiscount());
    testCase.setCallCount(tonnageDueTariff.getCallCountThreshold());

    calculator.set(testCase, tonnageDueTariff);

    final BigDecimal discountCoefficient = BigDecimal.ZERO;

    final BigDecimal expected = calculateDueAfterDiscount(discountCoefficient);
    final BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName(
      "Should return tonnage due without discount when call purpose is not eligible for discount")
  @ParameterizedTest(name = "call purpose : {arguments}")
  @MethodSource(value = "getCallPurposesNotEligibleForDiscount")
  void testReturnsTonnageDueWithoutDiscountByCallPurpose(final CallPurpose callPurpose) {

    testCase.getShip().setType(getShipTypeEligibleForDiscount());
    testCase.setCallPurpose(callPurpose);
    testCase.setCallCount(tonnageDueTariff.getCallCountThreshold());

    calculator.set(testCase, tonnageDueTariff);

    final BigDecimal discountCoefficient = BigDecimal.ZERO;

    final BigDecimal expected = calculateDueAfterDiscount(discountCoefficient);
    final BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }

  private ShipType getShipTypeEligibleForDiscount() {
    return tonnageDueTariff.getDiscountCoefficientsByShipType().keySet().stream().findAny().get();
  }

  private CallPurpose getCallPurposeEligibleForDiscount() {
    return tonnageDueTariff.getDiscountCoefficientsByCallPurpose().keySet().stream()
        .findAny()
        .get();
  }

  BigDecimal calculateDueAfterDiscount(final BigDecimal discountCoefficient) {

    BigDecimal duePerGrossTon;

    final boolean dueIsDependentOnShipType =
        tonnageDueTariff.getTonnageDuesByShipType().containsKey(testCase.getShip().getType());
    final boolean dueIsDependantOnCallPurpose =
        tonnageDueTariff.getTonnageDuesByCallPurpose().containsKey(testCase.getCallPurpose());

    if (dueIsDependentOnShipType) {
      duePerGrossTon =
          tonnageDueTariff.getTonnageDuesByShipType().get(testCase.getShip().getType()).getBase();
    } else if (dueIsDependantOnCallPurpose) {
      duePerGrossTon =
          tonnageDueTariff.getTonnageDuesByCallPurpose().get(testCase.getCallPurpose()).getBase();
    } else {
      duePerGrossTon =
          tonnageDueTariff
              .getTonnageDuesByPortArea()
              .get(testCase.getPort().getPortArea())
              .getBase();
    }

    BigDecimal dueTotal = grossTonnage.multiply(duePerGrossTon);

    return dueTotal.subtract(dueTotal.multiply(discountCoefficient));
  }

  protected BigDecimal getRandomGrossTonnage(final int min, final int max) {
    final Random random = new Random();
    return BigDecimal.valueOf(random.ints(min, max).findFirst().getAsInt());
  }
}
