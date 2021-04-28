package flagship.domain.calculators.servicedues;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import flagship.domain.calculators.DueCalculatorTest;
import flagship.domain.calculators.tariffs.serviceduestariffs.PilotageDueTariff;
import flagship.domain.cases.dto.PdaCase;
import flagship.domain.cases.dto.PdaPort;
import flagship.domain.cases.dto.PdaShip;
import flagship.domain.calculators.tariffs.enums.PilotageArea;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Map;
import java.util.Random;

import static flagship.domain.cases.entities.enums.CargoType.*;
import static flagship.domain.calculators.tariffs.enums.PdaWarning.HOLIDAY;
import static flagship.domain.calculators.tariffs.enums.PdaWarning.PILOT;
import static flagship.domain.calculators.tariffs.enums.PilotageArea.VARNA_FIRST;
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

    BigDecimal expected = getFixedPilotageDuePerGrossTonnage(testCase);
    BigDecimal result = calculator.calculateFor(testCase, tariff);

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

    BigDecimal fixedPilotageDue = getFixedPilotageDuePerGrossTonnage(testCase);
    BigDecimal increaseValue = getIncreaseValue(testCase);
    BigDecimal multiplier = evaluateMultiplier(grossTonnage);
    BigDecimal totalIncrease = increaseValue.multiply(multiplier);

    BigDecimal expected = fixedPilotageDue.add(totalIncrease);
    BigDecimal result = calculator.calculateFor(testCase, tariff);

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Should increase total pilotage due by 20 percent if contains hazardous cargo")
  @Test
  void shouldIncreaseTotalPilotageDueBy20Percent() {

    testCase.setCargoType(HAZARDOUS);

    BigDecimal pilotageDue = getFixedPilotageDuePerGrossTonnage(testCase);
    BigDecimal increase =
        pilotageDue.multiply(tariff.getIncreaseCoefficientsByCargoType().get(HAZARDOUS));

    BigDecimal expected = pilotageDue.add(increase);
    BigDecimal result = calculator.calculateFor(testCase, tariff);

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Should increase total pilotage due by 100 percent if contains special cargo")
  @Test
  void shouldIncreaseTotalPilotageDueBy100Percent() {

    testCase.setCargoType(SPECIAL);

    BigDecimal pilotageDue = getFixedPilotageDuePerGrossTonnage(testCase);
    BigDecimal increase =
        pilotageDue.multiply(tariff.getIncreaseCoefficientsByCargoType().get(SPECIAL));

    BigDecimal expected = pilotageDue.add(increase);
    BigDecimal result = calculator.calculateFor(testCase, tariff);

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Should increase total pilotage due by 50 percent if requires special pilot")
  @Test
  void shouldIncreaseTotalPilotageDueBy50PercentIfRequiresSpecialPilot() {

    testCase.getShip().setRequiresSpecialPilot(true);

    BigDecimal pilotageDue = getFixedPilotageDuePerGrossTonnage(testCase);
    BigDecimal increase =
        pilotageDue.multiply(tariff.getIncreaseCoefficientsByWarningType().get(PILOT));

    BigDecimal expected = pilotageDue.add(increase);
    BigDecimal result = calculator.calculateFor(testCase, tariff);

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Should increase total pilotage due by 50 percent if ETA is holiday")
  @Test
  void shouldIncreaseTotalPilotageDueBy50PercentIfEtaIsHoliday() {

    LocalDate estimatedDateOfArrival = tariff.getHolidayCalendar().stream().findFirst().get();

    testCase.setEstimatedDateOfArrival(estimatedDateOfArrival);
    testCase.setEstimatedDateOfDeparture(LocalDate.of(LocalDate.now().getYear(), 1, 5));

    BigDecimal pilotageDue = getFixedPilotageDuePerGrossTonnage(testCase);
    BigDecimal increase =
        pilotageDue.multiply(tariff.getIncreaseCoefficientsByWarningType().get(HOLIDAY));

    BigDecimal expected = pilotageDue.add(increase);
    BigDecimal result = calculator.calculateFor(testCase, tariff);

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Should increase total pilotage due by 50 percent if ETD is holiday")
  @Test
  void shouldIncreaseTotalPilotageDueBy50PercentIfEtdIsHoliday() {

    LocalDate estimatedDateOfDeparture = tariff.getHolidayCalendar().stream().findFirst().get();

    testCase.setEstimatedDateOfArrival(LocalDate.of(LocalDate.now().getYear(), 1, 5));
    testCase.setEstimatedDateOfDeparture(estimatedDateOfDeparture);

    BigDecimal pilotageDue = getFixedPilotageDuePerGrossTonnage(testCase);
    BigDecimal increase =
        pilotageDue.multiply(tariff.getIncreaseCoefficientsByWarningType().get(HOLIDAY));

    BigDecimal expected = pilotageDue.add(increase);
    BigDecimal result = calculator.calculateFor(testCase, tariff);

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Should increase total pilotage due by 100 percent if ETA and ETD are holidays")
  @Test
  void shouldIncreaseTotalPilotageDueBy100PercentIfEtaAndEtdAreHoliday() {

    LocalDate estimatedDateOfArrival = tariff.getHolidayCalendar().stream().findFirst().get();
    LocalDate estimatedDateOfDeparture = tariff.getHolidayCalendar().stream().findFirst().get();

    testCase.setEstimatedDateOfArrival(estimatedDateOfArrival);
    testCase.setEstimatedDateOfDeparture(estimatedDateOfDeparture);

    BigDecimal pilotageDue = getFixedPilotageDuePerGrossTonnage(testCase);
    BigDecimal increase =
        pilotageDue.multiply(
            tariff
                .getIncreaseCoefficientsByWarningType()
                .get(HOLIDAY)
                .multiply(BigDecimal.valueOf(2)));

    BigDecimal expected = pilotageDue.add(increase);
    BigDecimal result = calculator.calculateFor(testCase, tariff);

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Should increase total pilotage due by 150 percent")
  @Test
  void shouldIncreaseTotalPilotageDueBy150Percent() {

    LocalDate estimatedDateOfArrival = tariff.getHolidayCalendar().stream().findFirst().get();
    LocalDate estimatedDateOfDeparture = tariff.getHolidayCalendar().stream().findFirst().get();

    testCase.setEstimatedDateOfArrival(estimatedDateOfArrival);
    testCase.setEstimatedDateOfDeparture(estimatedDateOfDeparture);
    testCase.getShip().setRequiresSpecialPilot(true);

    BigDecimal pilotageDue = getFixedPilotageDuePerGrossTonnage(testCase);
    BigDecimal increase =
        pilotageDue.multiply(
            tariff
                .getIncreaseCoefficientsByWarningType()
                .get(HOLIDAY)
                .multiply(BigDecimal.valueOf(3)));

    BigDecimal expected = pilotageDue.add(increase);
    BigDecimal result = calculator.calculateFor(testCase, tariff);

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

  private BigDecimal evaluateMultiplier(BigDecimal grossTonnage) {

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
