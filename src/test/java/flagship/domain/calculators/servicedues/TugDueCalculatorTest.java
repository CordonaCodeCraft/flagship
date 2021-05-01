package flagship.domain.calculators.servicedues;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import flagship.domain.calculators.tariffs.serviceduestariffs.TugDueTariff;
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
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Map;
import java.util.Random;

import static flagship.domain.calculators.tariffs.enums.PdaWarning.HOLIDAY;
import static flagship.domain.calculators.tariffs.serviceduestariffs.TugDueTariff.TugArea;
import static flagship.domain.calculators.tariffs.serviceduestariffs.TugDueTariff.TugArea.VTC_FIRST;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class TugDueCalculatorTest {

  private static TugDueTariff tariff;
  private final TugDueCalculator calculator = new TugDueCalculator();
  private PdaCase testCase;

  @BeforeAll
  public static void beforeClass() throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    tariff = mapper.readValue(new File("src/main/resources/tugDueTariff.json"), TugDueTariff.class);
  }

  @BeforeEach
  void setUp() {
    PdaShip testShip =
        PdaShip.builder()
            .grossTonnage(BigDecimal.valueOf(1650))
            .hasIncreasedManeuverability(false)
            .transportsDangerousCargo(false)
            .build();
    PdaPort testPort = PdaPort.builder().tugArea(VTC_FIRST).build();
    testCase = PdaCase.builder().ship(testShip).port(testPort).build();
  }

  @DisplayName("Should calculate correct fixed tug due within threshold")
  @ParameterizedTest(name = "tug area : {arguments}")
  @EnumSource(TugArea.class)
  void testTugDueCalculationWithinThreshold(TugArea tugArea) {

    BigDecimal grossTonnage =
        getRandomGrossTonnage(150, tariff.getGrossTonnageThreshold().intValue());

    testCase.getShip().setGrossTonnage(grossTonnage);
    testCase.getPort().setTugArea(tugArea);

    calculator.set(testCase, tariff);

    BigDecimal fixedTugDuePerGrossTonnage = getFixedTugDuePerGrossTonnage(testCase);
    BigDecimal tugCount = getTugCount(testCase);

    BigDecimal expected = fixedTugDuePerGrossTonnage.multiply(tugCount);
    BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Should calculate correct increased tug due")
  @ParameterizedTest(name = "tug area : {arguments}")
  @EnumSource(TugArea.class)
  void testCalculateIncreasedTugDue(TugArea tugArea) {

    BigDecimal grossTonnage =
        getRandomGrossTonnage(tariff.getGrossTonnageThreshold().intValue(), 200000);

    testCase.getShip().setGrossTonnage(grossTonnage);
    testCase.getPort().setTugArea(tugArea);

    calculator.set(testCase, tariff);

    BigDecimal fixedTugDuePerGrossTonnage = getFixedTugDuePerGrossTonnage(testCase);
    BigDecimal totalIncrease =
        getIncreaseValue(testCase).multiply(getMultiplier(testCase.getShip().getGrossTonnage()));
    BigDecimal tugCount = getTugCount(testCase);

    BigDecimal expected = fixedTugDuePerGrossTonnage.add(totalIncrease).multiply(tugCount);
    BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Should reduce tug count if ship has increased maneuverability")
  @Test
  void testReduceTugCountByOne() {

    testCase.getShip().setGrossTonnage(BigDecimal.valueOf(9500));
    testCase.getShip().setHasIncreasedManeuverability(true);

    calculator.set(testCase, tariff);

    BigDecimal fixedTugDuePerGrossTonnage = getFixedTugDuePerGrossTonnage(testCase);
    BigDecimal tugCount = getTugCount(testCase).subtract(BigDecimal.valueOf(1));

    BigDecimal expected = fixedTugDuePerGrossTonnage.multiply(tugCount);
    BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Should reduce tug count for eligible ship with dangerous cargo")
  @Test
  void testReduceTugCountByOneForShipWithDangerousCargo() {

    testCase
        .getShip()
        .setGrossTonnage(
            BigDecimal.valueOf(tariff.getGrossTonnageThresholdForTugCountReduce().intValue() + 1));
    testCase.getShip().setTransportsDangerousCargo(true);
    testCase.getShip().setHasIncreasedManeuverability(true);

    calculator.set(testCase, tariff);

    BigDecimal fixedTugDuePerGrossTonnage = getFixedTugDuePerGrossTonnage(testCase);
    BigDecimal totalIncrease =
        getIncreaseValue(testCase).multiply(getMultiplier(testCase.getShip().getGrossTonnage()));
    BigDecimal tugDueTotal = fixedTugDuePerGrossTonnage.add(totalIncrease);
    BigDecimal tugCount = getTugCount(testCase).subtract(BigDecimal.valueOf(1));

    BigDecimal expected = tugDueTotal.multiply(tugCount);
    BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Should not reduce tug count for non eligible ship with dangerous cargo")
  @Test
  void testNoReduceTugCountIfShipIsNotEligible() {

    testCase
        .getShip()
        .setGrossTonnage(
            BigDecimal.valueOf(tariff.getGrossTonnageThresholdForTugCountReduce().intValue() - 1));
    testCase.getShip().setTransportsDangerousCargo(true);
    testCase.getShip().setHasIncreasedManeuverability(true);

    calculator.set(testCase, tariff);

    BigDecimal fixedTugDuePerGrossTonnage = getFixedTugDuePerGrossTonnage(testCase);
    BigDecimal totalIncrease =
        getIncreaseValue(testCase).multiply(getMultiplier(testCase.getShip().getGrossTonnage()));
    BigDecimal tugDueTotal = fixedTugDuePerGrossTonnage.add(totalIncrease);
    BigDecimal tugCount = getTugCount(testCase);

    BigDecimal expected = tugDueTotal.multiply(tugCount);
    BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Should increase tug due by 100 percent if ETA is holiday")
  @Test
  void testIncreaseTugDueBy100percentIfEtaIsHoliday() {

    LocalDate estimatedDateOfArrival = tariff.getHolidayCalendar().stream().findFirst().get();

    testCase.setEstimatedDateOfArrival(estimatedDateOfArrival);
    testCase.setEstimatedDateOfDeparture(LocalDate.of(LocalDate.now().getYear(), 1, 5));

    calculator.set(testCase, tariff);

    BigDecimal fixedTugDuePerGrossTonnage = getFixedTugDuePerGrossTonnage(testCase);
    BigDecimal tugCount = getTugCount(testCase);

    BigDecimal expected =
        fixedTugDuePerGrossTonnage
            .multiply(tugCount)
            .multiply(tariff.getIncreaseCoefficientsByWarningType().get(HOLIDAY));
    BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Should increase tug due by 100 percent if ETD is holiday")
  @Test
  void testIncreaseTugDueBy100percentIfEtdIsHoliday() {

    LocalDate estimatedDateOfDeparture = tariff.getHolidayCalendar().stream().findFirst().get();

    testCase.setEstimatedDateOfDeparture(estimatedDateOfDeparture);
    testCase.setEstimatedDateOfArrival(LocalDate.of(LocalDate.now().getYear(), 1, 5));

    calculator.set(testCase, tariff);

    BigDecimal fixedTugDuePerGrossTonnage = getFixedTugDuePerGrossTonnage(testCase);
    BigDecimal tugCount = getTugCount(testCase);

    BigDecimal expected =
        fixedTugDuePerGrossTonnage
            .multiply(tugCount)
            .multiply(tariff.getIncreaseCoefficientsByWarningType().get(HOLIDAY));
    BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Should increase tug due by 200 percent if ETA and ETD are holiday")
  @Test
  void testIncreaseTugDueBy100percentIfEtaAndEtdAreHoliday() {

    LocalDate estimatedDateOfArrival = tariff.getHolidayCalendar().stream().findFirst().get();
    LocalDate estimatedDateOfDeparture = tariff.getHolidayCalendar().stream().findFirst().get();

    testCase.setEstimatedDateOfArrival(estimatedDateOfArrival);
    testCase.setEstimatedDateOfDeparture(estimatedDateOfDeparture);

    calculator.set(testCase, tariff);

    BigDecimal fixedTugDuePerGrossTonnage = getFixedTugDuePerGrossTonnage(testCase);
    BigDecimal tugCount = getTugCount(testCase);

    BigDecimal expected =
        fixedTugDuePerGrossTonnage
            .multiply(tugCount)
            .multiply(
                tariff
                    .getIncreaseCoefficientsByWarningType()
                    .get(HOLIDAY)
                    .multiply(BigDecimal.valueOf(2)));
    BigDecimal result = calculator.calculate();
    assertThat(result).isEqualByComparingTo(expected);
  }

  private BigDecimal getMultiplier(BigDecimal grossTonnage) {
    double a =
        (grossTonnage.doubleValue() - tariff.getGrossTonnageThreshold().doubleValue()) / 1000;
    double b = a - Math.floor(a) > 0 ? 1 : 0;

    return BigDecimal.valueOf((int) a + b);
  }

  private BigDecimal getIncreaseValue(PdaCase testCase) {
    return BigDecimal.valueOf(
        tariff
            .getTugDuesByArea()
            .get(testCase.getPort().getTugArea())
            .get(getBiggestFixedTugDue(testCase))[2]);
  }

  private BigDecimal getFixedTugDuePerGrossTonnage(PdaCase testCase) {
    return tariff.getTugDuesByArea().get(testCase.getPort().getTugArea()).entrySet().stream()
        .filter(entry -> shipGrossTonnageIsInRange(testCase, entry))
        .map(Map.Entry::getKey)
        .findFirst()
        .orElse(getBiggestFixedTugDue(testCase));
  }

  private BigDecimal getBiggestFixedTugDue(PdaCase testCase) {
    return tariff.getTugDuesByArea().get(testCase.getPort().getTugArea()).keySet().stream()
        .max(Comparator.naturalOrder())
        .get();
  }

  private BigDecimal getTugCount(PdaCase testCase) {
    return tariff.getTugCountByGrossTonnage().entrySet().stream()
        .filter(entry -> shipGrossTonnageIsInRange(testCase, entry))
        .map(Map.Entry::getKey)
        .findFirst()
        .orElse(tariff.getMaximumTugCount());
  }

  private BigDecimal getRandomGrossTonnage(int min, int max) {
    Random random = new Random();
    return BigDecimal.valueOf(random.ints(min, max).findFirst().getAsInt());
  }

  private boolean shipGrossTonnageIsInRange(
      PdaCase testCase, Map.Entry<BigDecimal, Integer[]> entry) {
    return testCase.getShip().getGrossTonnage().intValue() >= entry.getValue()[0]
        && testCase.getShip().getGrossTonnage().intValue() <= entry.getValue()[1];
  }

}
