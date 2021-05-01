package flagship.domain.calculators.statedues;

import com.fasterxml.jackson.databind.ObjectMapper;
import flagship.domain.calculators.DueCalculatorTest;
import flagship.domain.calculators.tariffs.enums.PortArea;
import flagship.domain.calculators.tariffs.stateduestariffs.TonnageDueTariff;
import flagship.domain.cases.dto.PdaCase;
import flagship.domain.cases.dto.PdaPort;
import flagship.domain.cases.dto.PdaShip;
import flagship.domain.cases.entities.enums.CallPurpose;
import flagship.domain.cases.entities.enums.ShipType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import static flagship.domain.calculators.tariffs.enums.PortArea.FIRST;
import static flagship.domain.cases.entities.enums.CallPurpose.LOADING;
import static flagship.domain.cases.entities.enums.ShipType.GENERAL;
import static flagship.domain.cases.entities.enums.ShipType.SPECIAL;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("Tonnage due calculator tests")
class TonnageDueCalculatorTest implements DueCalculatorTest {

  private static TonnageDueTariff tariff;
  private final TonnageDueCalculator calculator = new TonnageDueCalculator();
  private PdaCase testCase;
  private BigDecimal grossTonnage;

  @BeforeAll
  public static void beforeClass() throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    tariff =
        mapper.readValue(
            new File("src/main/resources/tonnageDueTariff.json"), TonnageDueTariff.class);
  }

  private static Stream<Arguments> getShipTypesAffectingTonnageDue() {
    return tariff.getTonnageDuesByShipType().keySet().stream().map(Arguments::of);
  }

  private static Stream<Arguments> getCallPurposesAffectingTonnageDue() {
    return tariff.getTonnageDuesByCallPurpose().keySet().stream().map(Arguments::of);
  }

  private static Stream<Arguments> getCallPurposesEligibleForDiscount() {
    return tariff.getDiscountCoefficientsByCallPurpose().keySet().stream().map(Arguments::of);
  }

  private static Stream<Arguments> getShipTypesEligibleForDiscount() {
    return tariff.getDiscountCoefficientsByShipType().keySet().stream().map(Arguments::of);
  }

  private static Stream<Arguments> getShipTypesNotEligibleForDiscount() {
    return tariff.getShipTypesNotEligibleForDiscount().stream().map(Arguments::of);
  }

  private static Stream<Arguments> getCallPurposesNotEligibleForDiscount() {
    return tariff.getCallPurposesNotEligibleForDiscount().stream().map(Arguments::of);
  }

  @BeforeEach
  void setUp() {
    BigDecimal randomGrossTonnage = getRandomGrossTonnage();
    PdaPort testPort = PdaPort.builder().area(FIRST).build();
    PdaShip testShip = PdaShip.builder().grossTonnage(randomGrossTonnage).type(GENERAL).build();
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

  @DisplayName("Tonnage due by port area")
  @ParameterizedTest(name = "port area : {arguments}")
  @EnumSource(PortArea.class)
  void testTonnageDueByPortArea(PortArea portArea) {

    ShipType shipTypeTaxedByPortArea =
        Arrays.stream(ShipType.values())
            .filter(type -> !tariff.getTonnageDuesByShipType().containsKey(type))
            .findAny()
            .orElse(GENERAL);

    CallPurpose callPurposeTaxedByPortArea =
        Arrays.stream(CallPurpose.values())
            .filter(purpose -> !tariff.getTonnageDuesByCallPurpose().containsKey(purpose))
            .findAny()
            .orElse(LOADING);

    testCase.getShip().setType(shipTypeTaxedByPortArea);
    testCase.setCallPurpose(callPurposeTaxedByPortArea);
    testCase.getPort().setArea(portArea);

    calculator.set(testCase, tariff);

    BigDecimal duePerTon = tariff.getTonnageDuesByPortArea().get(testCase.getPort().getArea());

    BigDecimal expected = grossTonnage.multiply(duePerTon);
    BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Tonnage due by ship type")
  @ParameterizedTest(name = "ship type : {arguments}")
  @MethodSource(value = "getShipTypesAffectingTonnageDue")
  void testTonnageDueByShipType(ShipType shipType) {

    testCase.getShip().setType(shipType);

    calculator.set(testCase, tariff);

    BigDecimal duePerTon = tariff.getTonnageDuesByShipType().get(testCase.getShip().getType());

    BigDecimal expected = grossTonnage.multiply(duePerTon);
    BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Tonnage due for ship type 'Special'")
  @Test
  void shouldIncreaseTonnageDueByExpectedMonthCount() {

    testCase.getShip().setType(SPECIAL);
    testCase.setEstimatedDateOfArrival(LocalDate.of(2021, 1, 10));
    testCase.setEstimatedDateOfDeparture(LocalDate.of(2021, 1, 10));

    calculator.set(testCase, tariff);

    BigDecimal duePerTon = tariff.getTonnageDuesByShipType().get(SPECIAL);
    BigDecimal expected = grossTonnage.multiply(duePerTon);

    if (testCase.getEstimatedDateOfArrival().getMonthValue()
        != testCase.getEstimatedDateOfDeparture().getMonthValue()) {
      long multiplier =
          ChronoUnit.MONTHS.between(
                  YearMonth.from(testCase.getEstimatedDateOfArrival()),
                  YearMonth.from(testCase.getEstimatedDateOfDeparture()))
              + 1;
      expected = expected.multiply(BigDecimal.valueOf(multiplier));
    }

    BigDecimal result = calculator.calculate();
    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Tonnage due by call purpose")
  @ParameterizedTest(name = "call purpose : {arguments}")
  @MethodSource(value = "getCallPurposesAffectingTonnageDue")
  void testTonnageDueByCallPurpose(CallPurpose callPurpose) {

    testCase.setCallPurpose(callPurpose);

    calculator.set(testCase, tariff);

    BigDecimal duePerTon = tariff.getTonnageDuesByCallPurpose().get(testCase.getCallPurpose());

    BigDecimal expected = grossTonnage.multiply(duePerTon);
    BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Tonnage due with discount by port of arrival")
  @Test
  void testTonnageDueWithDiscountByPortOfArrival() {

    testCase.setArrivesFromBulgarianPort(true);

    calculator.set(testCase, tariff);

    BigDecimal discountCoefficient = tariff.getDiscountCoefficientForPortOfArrival();

    BigDecimal expected = calculateDueAfterDiscount(discountCoefficient);
    BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Tonnage due with discount by call count")
  @Test
  void testTonnageDueWithDiscountByCallCount() {

    testCase.setCallCount(tariff.getCallCountThreshold());

    calculator.set(testCase, tariff);

    BigDecimal discountCoefficient = tariff.getCallCountDiscountCoefficient();

    BigDecimal expected = calculateDueAfterDiscount(discountCoefficient);
    BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Tonnage due with discount by call purpose")
  @ParameterizedTest(name = "call purpose : {arguments}")
  @MethodSource(value = "getCallPurposesEligibleForDiscount")
  void testTonnageDueWithDiscountByCallPurpose(CallPurpose callPurpose) {

    testCase.setCallPurpose(callPurpose);

    calculator.set(testCase, tariff);

    BigDecimal discountCoefficient = tariff.getDiscountCoefficientsByCallPurpose().get(callPurpose);

    BigDecimal expected = calculateDueAfterDiscount(discountCoefficient);
    BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Tonnage due with discount by ship type")
  @ParameterizedTest(name = "ship type : {arguments}")
  @MethodSource(value = "getShipTypesEligibleForDiscount")
  void testTonnageDueWithDiscountByShipType(ShipType shipType) {

    testCase.getShip().setType(shipType);

    calculator.set(testCase, tariff);

    BigDecimal discountCoefficient =
        tariff.getDiscountCoefficientsByShipType().get(testCase.getShip().getType());

    BigDecimal expected = calculateDueAfterDiscount(discountCoefficient);
    BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Tonnage due with biggest discount")
  @Test
  void testTonnageDueWithBiggestDiscount() {

    testCase.getShip().setType(getShipTypeEligibleForDiscount());
    testCase.setCallPurpose(getCallPurposeEligibleForDiscount());
    testCase.setCallCount(tariff.getCallCountThreshold());

    calculator.set(testCase, tariff);

    List<BigDecimal> discountCoefficients =
        Arrays.asList(
            tariff.getDiscountCoefficientsByShipType().get(testCase.getShip().getType()),
            tariff.getDiscountCoefficientsByCallPurpose().get(testCase.getCallPurpose()),
            tariff.getCallCountDiscountCoefficient());

    BigDecimal discountCoefficient =
        discountCoefficients.stream().max(Comparator.naturalOrder()).get();

    BigDecimal expected = calculateDueAfterDiscount(discountCoefficient);
    BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Tonnage due without discount when ship type is not eligible for discount")
  @ParameterizedTest(name = "ship type : {arguments}")
  @MethodSource(value = "getShipTypesNotEligibleForDiscount")
  void testTonnageDueWithZeroDiscountByShipType(ShipType shipType) {

    testCase.getShip().setType(shipType);
    testCase.setCallPurpose(getCallPurposeEligibleForDiscount());
    testCase.setCallCount(tariff.getCallCountThreshold());

    calculator.set(testCase, tariff);

    BigDecimal discountCoefficient = BigDecimal.ZERO;

    BigDecimal expected = calculateDueAfterDiscount(discountCoefficient);
    BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Tonnage due without discount when call purpose is not eligible for discount")
  @ParameterizedTest(name = "call purpose : {arguments}")
  @MethodSource(value = "getCallPurposesNotEligibleForDiscount")
  void testTonnageDueWithZeroDiscountByCallPurpose(CallPurpose callPurpose) {

    testCase.getShip().setType(getShipTypeEligibleForDiscount());
    testCase.setCallPurpose(callPurpose);
    testCase.setCallCount(tariff.getCallCountThreshold());

    calculator.set(testCase, tariff);

    BigDecimal discountCoefficient = BigDecimal.ZERO;

    BigDecimal expected = calculateDueAfterDiscount(discountCoefficient);
    BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }

  private ShipType getShipTypeEligibleForDiscount() {
    return tariff.getDiscountCoefficientsByShipType().keySet().stream().findAny().get();
  }

  private CallPurpose getCallPurposeEligibleForDiscount() {
    return tariff.getDiscountCoefficientsByCallPurpose().keySet().stream().findAny().get();
  }

  BigDecimal calculateDueAfterDiscount(BigDecimal discountCoefficient) {

    BigDecimal duePerGrossTon;

    final boolean dueIsDependentOnShipType =
        tariff.getTonnageDuesByShipType().containsKey(testCase.getShip().getType());
    final boolean dueIsDependantOnCallPurpose =
        tariff.getTonnageDuesByCallPurpose().containsKey(testCase.getCallPurpose());

    if (dueIsDependentOnShipType) {
      duePerGrossTon = tariff.getTonnageDuesByShipType().get(testCase.getShip().getType());
    } else if (dueIsDependantOnCallPurpose) {
      duePerGrossTon = tariff.getTonnageDuesByCallPurpose().get(testCase.getCallPurpose());
    } else {
      duePerGrossTon = tariff.getTonnageDuesByPortArea().get(testCase.getPort().getArea());
    }

    BigDecimal dueTotal = grossTonnage.multiply(duePerGrossTon);
    BigDecimal discount = dueTotal.multiply(discountCoefficient);

    return dueTotal.subtract(discount);
  }

  private BigDecimal getRandomGrossTonnage() {
    Random random = new Random();
    return BigDecimal.valueOf(random.ints(500, 200000).findFirst().getAsInt());
  }
}
