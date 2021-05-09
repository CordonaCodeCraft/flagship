package flagship.domain.calculators.serviceduescalculators;

import flagship.domain.calculators.BaseCalculatorTest;
import flagship.domain.cases.dto.PdaCase;
import flagship.domain.cases.dto.PdaPort;
import flagship.domain.cases.dto.PdaShip;
import flagship.domain.tariffs.Range;
import flagship.domain.tariffs.Tariff;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Map;

import static flagship.domain.tariffs.PdaWarningsGenerator.PdaWarning.DANGEROUS_TUG_CARGO;
import static flagship.domain.tariffs.serviceduestariffs.TugDueTariff.TugArea;
import static flagship.domain.tariffs.serviceduestariffs.TugDueTariff.TugArea.VTC_FIRST;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("Tug due calculator tests")
class TugDueCalculatorTest extends BaseCalculatorTest {

  private final TugDueCalculator calculator = new TugDueCalculator();

  @BeforeEach
  void setUp() {
    PdaShip testShip =
        PdaShip.builder()
            .grossTonnage(BigDecimal.valueOf(MIN_GT))
            .hasIncreasedManeuverability(false)
            .build();
    PdaPort testPort = PdaPort.builder().tugArea(VTC_FIRST).build();
    testCase = PdaCase.builder().ship(testShip).port(testPort).warnings(new HashSet<>()).build();
  }

  @DisplayName("Should return fixed tug due")
  @ParameterizedTest(name = "tug area : {arguments}")
  @EnumSource(TugArea.class)
  void testReturnsTugDueCalculationWithinThreshold(TugArea tugArea) {

    testCase
        .getShip()
        .setGrossTonnage(
            getRandomGrossTonnage(
                Tariff.MIN_GT, tugDueTariff.getGrossTonnageThreshold().intValue()));

    testCase.getPort().setTugArea(tugArea);

    calculator.set(testCase, tugDueTariff);

    final BigDecimal fixedDue = getDueByRange(tugDueTariff.getTugDuesByArea().get(tugArea));
    final BigDecimal tugCount = getTugCount();

    final BigDecimal expected = fixedDue.multiply(tugCount);
    final BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Should return increased tug due")
  @ParameterizedTest(name = "tug area : {arguments}")
  @EnumSource(TugArea.class)
  void testReturnsCalculateIncreasedTugDue(TugArea tugArea) {

    testCase
        .getShip()
        .setGrossTonnage(
            getRandomGrossTonnage(tugDueTariff.getGrossTonnageThreshold().intValue() + 1, MAX_GT));

    testCase.getPort().setTugArea(tugArea);

    calculator.set(testCase, tugDueTariff);

    final BigDecimal fixedDue = getDueByRange(tugDueTariff.getTugDuesByArea().get(tugArea));
    final BigDecimal addition = getAddition(tugDueTariff.getTugDuesByArea().get(tugArea));
    final BigDecimal multiplier = getMultiplier(tugDueTariff.getGrossTonnageThreshold());
    final BigDecimal tugCount = getTugCount();

    final BigDecimal expected = fixedDue.add(addition.multiply(multiplier)).multiply(tugCount);
    final BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Should reduce tug count if ship has increased maneuverability")
  @Test
  void testReducesTugCountByOne() {

    testCase.getShip().setGrossTonnage(BigDecimal.valueOf(10000));
    testCase.getShip().setHasIncreasedManeuverability(true);

    calculator.set(testCase, tugDueTariff);

    final BigDecimal fixedDue = getDueByRange(tugDueTariff.getTugDuesByArea().get(VTC_FIRST));
    final BigDecimal tugCount = getTugCount().subtract(BigDecimal.valueOf(1));

    final BigDecimal expected = fixedDue.multiply(tugCount);
    final BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Should reduce tug count for eligible ship with dangerous cargo")
  @Test
  void testReducesTugCountByOneForShipWithDangerousCargo() {

    testCase
        .getShip()
        .setGrossTonnage(
            BigDecimal.valueOf(
                tugDueTariff.getGrossTonnageThresholdForTugCountReduce().intValue() + 1));

    testCase.getWarnings().add(DANGEROUS_TUG_CARGO);
    testCase.getShip().setHasIncreasedManeuverability(true);

    calculator.set(testCase, tugDueTariff);

    final BigDecimal fixedDue = getDueByRange(tugDueTariff.getTugDuesByArea().get(VTC_FIRST));
    final BigDecimal addition = getAddition(tugDueTariff.getTugDuesByArea().get(VTC_FIRST));
    final BigDecimal multiplier = getMultiplier(tugDueTariff.getGrossTonnageThreshold());
    final BigDecimal tugCount = getTugCount().subtract(BigDecimal.valueOf(1));

    final BigDecimal expected = fixedDue.add(addition.multiply(multiplier)).multiply(tugCount);
    final BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Should not reduce tug count for non eligible ship with dangerous cargo")
  @Test
  void testNoReduceTugCountIfShipIsNotEligible() {

    testCase
        .getShip()
        .setGrossTonnage(
            BigDecimal.valueOf(
                tugDueTariff.getGrossTonnageThresholdForTugCountReduce().intValue() - 1));

    testCase.getWarnings().add(DANGEROUS_TUG_CARGO);
    testCase.getShip().setHasIncreasedManeuverability(true);

    calculator.set(testCase, tugDueTariff);

    final BigDecimal fixedDue = getDueByRange(tugDueTariff.getTugDuesByArea().get(VTC_FIRST));
    final BigDecimal addition = getAddition(tugDueTariff.getTugDuesByArea().get(VTC_FIRST));
    final BigDecimal multiplier = getMultiplier(tugDueTariff.getGrossTonnageThreshold());
    final BigDecimal tugCount = getTugCount();

    final BigDecimal expected = fixedDue.add(addition.multiply(multiplier)).multiply(tugCount);
    final BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }

  private BigDecimal getTugCount() {
    return tugDueTariff.getTugCountByGrossTonnage().entrySet().stream()
        .filter(this::grossTonnageInRange)
        .map(Map.Entry::getValue)
        .findFirst()
        .get();
  }

  private boolean grossTonnageInRange(final Map.Entry<Range, BigDecimal> entry) {
    return testCase.getShip().getGrossTonnage().intValue() >= entry.getKey().getMin()
        && testCase.getShip().getGrossTonnage().intValue() <= entry.getKey().getMax();
  }
}
