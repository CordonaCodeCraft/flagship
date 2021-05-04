package flagship.domain.calculators.serviceduescalculators;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import flagship.domain.calculators.DueCalculatorTest;
import flagship.domain.cases.dto.PdaCase;
import flagship.domain.cases.dto.PdaPort;
import flagship.domain.cases.dto.PdaShip;
import flagship.domain.tariffs.serviceduestariffs.PilotageDueTariff;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;

import static flagship.domain.tariffs.PdaWarningsGenerator.PdaWarning.*;
import static flagship.domain.tariffs.serviceduestariffs.PilotageDueTariff.PilotageArea;
import static flagship.domain.tariffs.serviceduestariffs.PilotageDueTariff.PilotageArea.VARNA_FIRST;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("Pilotage due calculator tests")
public class PilotageDueCalculatorTest implements DueCalculatorTest {

  private static PilotageDueTariff tariff;
  private final PilotageDueCalculator calculator = new PilotageDueCalculator();
  private PdaCase testCase;

  @BeforeAll
  public static void beforeClass() throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    tariff =
        mapper.readValue(
            new File("src/main/resources/pilotageDueTariff.json"), PilotageDueTariff.class);
  }

  @BeforeEach
  void setUp() {
    PdaPort testPort = PdaPort.builder().pilotageArea(VARNA_FIRST).build();
    PdaShip testShip = PdaShip.builder().grossTonnage(BigDecimal.valueOf(1650)).build();
    testCase = PdaCase.builder().port(testPort).ship(testShip).warnings(new HashSet<>()).build();
  }

  @DisplayName("Should calculate fixed pilotage due by pilotage area")
  @ParameterizedTest(name = "pilotage area: {arguments}")
  @EnumSource(PilotageArea.class)
  void testPilotageDueCalculationWithinThreshold(PilotageArea pilotageArea) {

    BigDecimal grossTonnage =
        getRandomGrossTonnage(0, tariff.getGrossTonnageThreshold().intValue());
    testCase.getShip().setGrossTonnage(grossTonnage);
    testCase.getPort().setPilotageArea(pilotageArea);

    calculator.set(testCase, tariff);

    BigDecimal expected = getFixedPilotageDuePerGrossTonnage(testCase);
    BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Should calculate increased pilotage due")
  @ParameterizedTest(name = "pilotage area: {arguments}")
  @EnumSource(PilotageArea.class)
  void testCalculateIncreasedPilotageDue(PilotageArea pilotageArea) {

    BigDecimal grossTonnage =
        getRandomGrossTonnage(tariff.getGrossTonnageThreshold().intValue() + 1, 200000);
    testCase.getShip().setGrossTonnage(grossTonnage);
    testCase.getPort().setPilotageArea(pilotageArea);

    calculator.set(testCase, tariff);

    BigDecimal fixedPilotageDue = getFixedPilotageDuePerGrossTonnage(testCase);
    BigDecimal increaseValue = getIncreaseValue(testCase);
    BigDecimal multiplier = getMultiplier(testCase.getShip().getGrossTonnage());
    BigDecimal totalIncrease = increaseValue.multiply(multiplier);

    BigDecimal expected = fixedPilotageDue.add(totalIncrease);
    BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Should increase total pilotage due by 20 percent if contains hazardous cargo")
  @Test
  void shouldIncreaseTotalPilotageDueBy20Percent() {

    testCase.getWarnings().add(HAZARDOUS_PILOTAGE_CARGO);

    calculator.set(testCase, tariff);

    BigDecimal pilotageDue = getFixedPilotageDuePerGrossTonnage(testCase);
    BigDecimal increaseCoefficient =
        tariff.getIncreaseCoefficientsByPdaWarning().get(HAZARDOUS_PILOTAGE_CARGO);

    BigDecimal expected = pilotageDue.multiply(increaseCoefficient);
    BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Should increase total pilotage due by 100 percent if contains special cargo")
  @Test
  void shouldIncreaseTotalPilotageDueBy100Percent() {

    testCase.getWarnings().add(SPECIAL_PILOTAGE_CARGO);

    calculator.set(testCase, tariff);

    BigDecimal pilotageDue = getFixedPilotageDuePerGrossTonnage(testCase);
    BigDecimal increaseCoefficient =
        tariff.getIncreaseCoefficientsByPdaWarning().get(SPECIAL_PILOTAGE_CARGO);

    BigDecimal expected = pilotageDue.multiply(increaseCoefficient);
    BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Should increase total pilotage due by 50 percent if requires special pilot")
  @Test
  void shouldIncreaseTotalPilotageDueBy50PercentIfRequiresSpecialPilot() {

    testCase.getWarnings().add(SPECIAL_PILOT);

    calculator.set(testCase, tariff);

    BigDecimal pilotageDue = getFixedPilotageDuePerGrossTonnage(testCase);
    BigDecimal increaseCoefficient =
        tariff.getIncreaseCoefficientsByPdaWarning().get(SPECIAL_PILOT);

    BigDecimal expected = pilotageDue.multiply(increaseCoefficient);
    BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Should increase total pilotage due by 150 percent")
  @Test
  void shouldIncreaseTotalPilotageDueBy150Percent() {

    testCase.getWarnings().add(SPECIAL_PILOTAGE_CARGO);
    testCase.getWarnings().add(SPECIAL_PILOT);

    calculator.set(testCase, tariff);

    BigDecimal pilotageDue = getFixedPilotageDuePerGrossTonnage(testCase);
    BigDecimal increaseCoefficient =
        tariff
            .getIncreaseCoefficientsByPdaWarning()
            .get(SPECIAL_PILOTAGE_CARGO)
            .add(tariff.getIncreaseCoefficientsByPdaWarning().get(SPECIAL_PILOT));

    BigDecimal expected = pilotageDue.multiply(increaseCoefficient);
    BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }

  private BigDecimal getFixedPilotageDuePerGrossTonnage(PdaCase testCase) {
    return tariff
        .getPilotageDuesByArea()
        .get(testCase.getPort().getPilotageArea())
        .entrySet()
        .stream()
        .filter(
            entry ->
                testCase.getShip().getGrossTonnage().intValue() >= entry.getValue()[0]
                    && testCase.getShip().getGrossTonnage().intValue() <= entry.getValue()[1])
        .map(Map.Entry::getKey)
        .findFirst()
        .orElse(getBiggestFixedPilotageDue(testCase));
  }

  private BigDecimal getBiggestFixedPilotageDue(PdaCase testCase) {
    return tariff
        .getPilotageDuesByArea()
        .get(testCase.getPort().getPilotageArea())
        .keySet()
        .stream()
        .max(Comparator.naturalOrder())
        .get();
  }

  private BigDecimal getIncreaseValue(PdaCase testCase) {
    return tariff
        .getPilotageDuesByArea()
        .get(testCase.getPort().getPilotageArea())
        .values()
        .stream()
        .findFirst()
        .map(array -> BigDecimal.valueOf(array[2]))
        .get();
  }

  private BigDecimal getMultiplier(BigDecimal grossTonnage) {

    double a =
        (grossTonnage.doubleValue() - tariff.getGrossTonnageThreshold().doubleValue()) / 1000;
    double b = (int) a;
    double c = a - Math.floor(a) > 0 ? 1 : 0;
    double multiplier = b + c == 0 ? 1 : b + c;

    return BigDecimal.valueOf(multiplier);
  }

  private BigDecimal getRandomGrossTonnage(int min, int max) {
    Random random = new Random();
    return BigDecimal.valueOf(random.ints(min, max).findFirst().getAsInt());
  }
}
