package flagship.domain.calculators.servicedues;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import flagship.domain.calculators.DueCalculatorTest;
import flagship.domain.calculators.tariffs.serviceduestariffs.PilotageDueTariff;
import flagship.domain.cases.dto.PdaCase;
import flagship.domain.cases.dto.PdaPort;
import flagship.domain.cases.dto.PdaShip;
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
import java.util.Map;
import java.util.Random;

import static flagship.domain.calculators.tariffs.enums.PdaWarning.PILOT;
import static flagship.domain.calculators.tariffs.serviceduestariffs.PilotageDueTariff.PilotageArea;
import static flagship.domain.calculators.tariffs.serviceduestariffs.PilotageDueTariff.PilotageArea.VARNA_FIRST;
import static flagship.domain.cases.entities.enums.CargoType.*;
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
    PdaShip testShip =
        PdaShip.builder()
            .grossTonnage(BigDecimal.valueOf(1650))
            .requiresSpecialPilot(false)
            .build();
    testCase = PdaCase.builder().port(testPort).ship(testShip).cargoType(REGULAR).build();
  }

  @DisplayName("Should calculate correct fixed pilotage due by pilotage area")
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

  @DisplayName("Should calculate correct increased pilotage due")
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

    testCase.setCargoType(HAZARDOUS);

    calculator.set(testCase, tariff);

    BigDecimal pilotageDue = getFixedPilotageDuePerGrossTonnage(testCase);
    BigDecimal increaseCoefficient = tariff.getIncreaseCoefficientsByCargoType().get(HAZARDOUS);

    BigDecimal expected = pilotageDue.multiply(increaseCoefficient);
    BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Should increase total pilotage due by 100 percent if contains special cargo")
  @Test
  void shouldIncreaseTotalPilotageDueBy100Percent() {

    testCase.setCargoType(SPECIAL);

    calculator.set(testCase, tariff);

    BigDecimal pilotageDue = getFixedPilotageDuePerGrossTonnage(testCase);
    BigDecimal increaseCoefficient = tariff.getIncreaseCoefficientsByCargoType().get(SPECIAL);

    BigDecimal expected = pilotageDue.multiply(increaseCoefficient);
    BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Should increase total pilotage due by 50 percent if requires special pilot")
  @Test
  void shouldIncreaseTotalPilotageDueBy50PercentIfRequiresSpecialPilot() {

    testCase.getShip().setRequiresSpecialPilot(true);

    calculator.set(testCase, tariff);

    BigDecimal pilotageDue = getFixedPilotageDuePerGrossTonnage(testCase);
    BigDecimal increaseCoefficient = tariff.getIncreaseCoefficientsByWarningType().get(PILOT);

    BigDecimal expected = pilotageDue.multiply(increaseCoefficient);
    BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Should increase total pilotage due by 150 percent")
  @Test
  void shouldIncreaseTotalPilotageDueBy150Percent() {

    testCase.setCargoType(SPECIAL);
    testCase.getShip().setRequiresSpecialPilot(true);

    calculator.set(testCase, tariff);

    BigDecimal pilotageDue = getFixedPilotageDuePerGrossTonnage(testCase);
    BigDecimal increaseCoefficient =
        tariff
            .getIncreaseCoefficientsByCargoType()
            .get(SPECIAL)
            .add(tariff.getIncreaseCoefficientsByWarningType().get(PILOT));

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
