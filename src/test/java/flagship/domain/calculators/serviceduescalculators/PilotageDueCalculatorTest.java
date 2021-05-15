package flagship.domain.calculators.serviceduescalculators;

import flagship.domain.calculators.BaseCalculatorTest;
import flagship.domain.cases.dto.PdaCase;
import flagship.domain.cases.dto.PdaPort;
import flagship.domain.cases.dto.PdaShip;
import flagship.domain.tariffs.Tariff;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.math.BigDecimal;
import java.util.HashSet;

import static flagship.domain.PdaWarningsGenerator.WarningType.*;
import static flagship.domain.tariffs.PilotageDueTariff.PilotageArea;
import static flagship.domain.tariffs.PilotageDueTariff.PilotageArea.VARNA_FIRST;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("Pilotage due calculator tests")
public class PilotageDueCalculatorTest extends BaseCalculatorTest {

  private final PilotageDueCalculator calculator = new PilotageDueCalculator();

  @BeforeEach
  void setUp() {
    PdaPort testPort = PdaPort.builder().pilotageArea(VARNA_FIRST).build();
    PdaShip testShip = PdaShip.builder().grossTonnage(BigDecimal.valueOf(MIN_GT)).build();
    testCase = PdaCase.builder().port(testPort).ship(testShip).warningTypes(new HashSet<>()).build();
  }

  @DisplayName("Should return fixed pilotage due by pilotage area")
  @ParameterizedTest(name = "pilotage area: {arguments}")
  @EnumSource(PilotageArea.class)
  void testReturnsPilotageDueCalculationWithinThreshold(PilotageArea pilotageArea) {

    testCase
        .getShip()
        .setGrossTonnage(
            getRandomGrossTonnage(
                Tariff.MIN_GT, pilotageDueTariff.getGrossTonnageThreshold().intValue()));

    testCase.getPort().setPilotageArea(pilotageArea);

    calculator.set(testCase, pilotageDueTariff);

    final BigDecimal expected =
        getDueByRange(pilotageDueTariff.getPilotageDuesByArea().get(pilotageArea));
    final BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Should return increased pilotage due")
  @ParameterizedTest(name = "pilotage area: {arguments}")
  @EnumSource(PilotageArea.class)
  void testReturnsCalculateIncreasedPilotageDue(PilotageArea pilotageArea) {

    testCase
        .getShip()
        .setGrossTonnage(
            getRandomGrossTonnage(
                pilotageDueTariff.getGrossTonnageThreshold().intValue() + 1, Tariff.MAX_GT));

    testCase.getPort().setPilotageArea(pilotageArea);

    calculator.set(testCase, pilotageDueTariff);

    final BigDecimal fixedDue =
        getDueByRange(pilotageDueTariff.getPilotageDuesByArea().get(pilotageArea));
    final BigDecimal addition =
        getAddition(pilotageDueTariff.getPilotageDuesByArea().get(pilotageArea));
    final BigDecimal multiplier = getMultiplier();

    final BigDecimal expected = fixedDue.add(addition.multiply(multiplier));
    final BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Should increase pilotage due by 20 percent if contains hazardous cargo")
  @Test
  void testIncreasesTotalPilotageDueBy20Percent() {

    testCase.getWarningTypes().add(HAZARDOUS_PILOTAGE_CARGO);

    calculator.set(testCase, pilotageDueTariff);

    final BigDecimal fixedDue =
        getDueByRange(pilotageDueTariff.getPilotageDuesByArea().get(VARNA_FIRST));
    final BigDecimal increaseCoefficient =
        pilotageDueTariff.getIncreaseCoefficientsByWarningType().get(HAZARDOUS_PILOTAGE_CARGO);

    final BigDecimal expected = fixedDue.add(fixedDue.multiply(increaseCoefficient));
    final BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Should increase pilotage due by 100 percent if contains special cargo")
  @Test
  void testIncreasesTotalPilotageDueBy100Percent() {

    testCase.getWarningTypes().add(SPECIAL_PILOTAGE_CARGO);

    calculator.set(testCase, pilotageDueTariff);

    final BigDecimal fixedDue =
        getDueByRange(pilotageDueTariff.getPilotageDuesByArea().get(VARNA_FIRST));
    final BigDecimal increaseCoefficient =
        pilotageDueTariff.getIncreaseCoefficientsByWarningType().get(SPECIAL_PILOTAGE_CARGO);

    final BigDecimal expected = fixedDue.add(fixedDue.multiply(increaseCoefficient));
    final BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Should increase pilotage due by 50 percent if requires special pilot")
  @Test
  void testIncreasesTotalPilotageDueBy50PercentIfRequiresSpecialPilot() {

    testCase.getWarningTypes().add(SPECIAL_PILOT);

    calculator.set(testCase, pilotageDueTariff);

    final BigDecimal fixedDue =
        getDueByRange(pilotageDueTariff.getPilotageDuesByArea().get(VARNA_FIRST));
    final BigDecimal increaseCoefficient =
        pilotageDueTariff.getIncreaseCoefficientsByWarningType().get(SPECIAL_PILOT);

    final BigDecimal expected = fixedDue.add(fixedDue.multiply(increaseCoefficient));
    final BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Should increase pilotage due by 150 percent")
  @Test
  void testIncreasesTotalPilotageDueBy150Percent() {

    testCase.getWarningTypes().add(SPECIAL_PILOTAGE_CARGO);
    testCase.getWarningTypes().add(SPECIAL_PILOT);

    calculator.set(testCase, pilotageDueTariff);

    final BigDecimal fixedDue =
        getDueByRange(pilotageDueTariff.getPilotageDuesByArea().get(VARNA_FIRST));
    final BigDecimal increaseCoefficient =
        pilotageDueTariff
            .getIncreaseCoefficientsByWarningType()
            .get(SPECIAL_PILOTAGE_CARGO)
            .add(pilotageDueTariff.getIncreaseCoefficientsByWarningType().get(SPECIAL_PILOT));

    final BigDecimal expected = fixedDue.add(fixedDue.multiply(increaseCoefficient));
    final BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }

  private BigDecimal getMultiplier() {

    final double grossTonnage = testCase.getShip().getGrossTonnage().doubleValue();
    final double grossTonnageThreshold = pilotageDueTariff.getGrossTonnageThreshold().doubleValue();

    final double a = (grossTonnage - grossTonnageThreshold) / 1000;
    final double b = (int) a;
    final double c = a - Math.floor(a) > 0 ? 1 : 0;
    final double multiplier = b + c == 0 ? 1 : b + c;

    return BigDecimal.valueOf(multiplier);
  }
}
