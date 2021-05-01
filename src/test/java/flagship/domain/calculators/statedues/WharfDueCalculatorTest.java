package flagship.domain.calculators.statedues;

import com.fasterxml.jackson.databind.ObjectMapper;
import flagship.domain.calculators.DueCalculatorTest;
import flagship.domain.calculators.tariffs.stateduestariffs.WharfDueTariff;
import flagship.domain.cases.dto.PdaCase;
import flagship.domain.cases.dto.PdaShip;
import flagship.domain.cases.entities.enums.CallPurpose;
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
import java.util.stream.Stream;

import static flagship.domain.cases.entities.enums.ShipType.GENERAL;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("Wharf due calculator tests")
class WharfDueCalculatorTest implements DueCalculatorTest {

  private static WharfDueTariff tariff;
  private final WharfDueCalculator calculator = new WharfDueCalculator();
  private PdaCase testCase;

  @BeforeAll
  public static void beforeClass() throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    tariff =
        mapper.readValue(new File("src/main/resources/wharfDueTariff.json"), WharfDueTariff.class);
  }

  private static Stream<Arguments> getShipTypesAffectingWharfDue() {
    return tariff.getWharfDuesByShipType().keySet().stream().map(Arguments::of);
  }

  private static Stream<Arguments> getCallPurposesEligibleForDiscount() {
    return tariff.getDiscountCoefficientsByCallPurpose().keySet().stream().map(Arguments::of);
  }

  private static Stream<Arguments> getShipTypesNotEligibleForDiscount() {
    return tariff.getShipTypesNotEligibleForDiscount().stream().map(Arguments::of);
  }

  @BeforeEach
  void setUp() {
    PdaShip testShip =
        PdaShip.builder().lengthOverall(BigDecimal.valueOf(195.05)).type(GENERAL).build();
    testCase = PdaCase.builder().ship(testShip).alongsideDaysExpected(5).build();
  }

  @DisplayName("Wharf due by default value")
  @Test
  void testWharfDueByDefaultValue() {

    ShipType shipTypeWithDefaultWharfDue =
        Arrays.stream(ShipType.values())
            .filter(type -> !tariff.getWharfDuesByShipType().containsKey(type))
            .findAny()
            .orElse(GENERAL);

    testCase.getShip().setType(shipTypeWithDefaultWharfDue);

    calculator.set(testCase, tariff);

    BigDecimal expected = calculateWharfDue();
    BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Wharf due by ship type")
  @ParameterizedTest(name = "ship type : {arguments}")
  @MethodSource(value = "getShipTypesAffectingWharfDue")
  void testWharfDueByShipType(ShipType shipType) {

    testCase.getShip().setType(shipType);

    calculator.set(testCase, tariff);

    BigDecimal expected = calculateWharfDue();
    BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Wharf due with discount by call purpose")
  @ParameterizedTest(name = "call purpose : {arguments}")
  @MethodSource(value = "getCallPurposesEligibleForDiscount")
  void testWharfDueWithDiscountByCallPurpose(CallPurpose callPurpose) {

    testCase.setCallPurpose(callPurpose);

    calculator.set(testCase, tariff);

    BigDecimal discountCoefficient =
        tariff.getDiscountCoefficientsByCallPurpose().get(testCase.getCallPurpose());

    BigDecimal expected = calculateDueAfterDiscount(discountCoefficient);
    BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Wharf due without discount when ship type is not eligible for discount")
  @ParameterizedTest(name = "ship type : {arguments}")
  @MethodSource(value = "getShipTypesNotEligibleForDiscount")
  void testWharfDueWithZeroDiscountWhenShipTypeIsNotEligibleForDiscount(ShipType shipType) {

    testCase.getShip().setType(shipType);

    calculator.set(testCase, tariff);

    BigDecimal discountCoefficient = BigDecimal.ZERO;

    BigDecimal expected = calculateDueAfterDiscount(discountCoefficient);
    BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }

  private BigDecimal calculateDueAfterDiscount(BigDecimal discountCoefficient) {
    BigDecimal wharfDue = calculateWharfDue();
    BigDecimal discount = wharfDue.multiply(discountCoefficient);
    return wharfDue.subtract(discount);
  }

  private BigDecimal calculateWharfDue() {
    final BigDecimal lengthOverall =
        BigDecimal.valueOf(Math.ceil(testCase.getShip().getLengthOverall().doubleValue()));
    final BigDecimal wharfDuePerMeter =
        tariff
            .getWharfDuesByShipType()
            .getOrDefault(testCase.getShip().getType(), tariff.getDefaultWharfDue());
    final BigDecimal wharfDuePerLengthOverall = lengthOverall.multiply(wharfDuePerMeter);
    final BigDecimal alongsideHoursExpected =
        BigDecimal.valueOf(testCase.getAlongsideDaysExpected() * 24);
    return alongsideHoursExpected.multiply(wharfDuePerLengthOverall);
  }
}
