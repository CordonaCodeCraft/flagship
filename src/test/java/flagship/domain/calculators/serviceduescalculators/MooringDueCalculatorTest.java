package flagship.domain.calculators.serviceduescalculators;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import flagship.domain.tariffs.serviceduestariffs.MooringDueTariff;
import flagship.domain.tariffs.serviceduestariffs.MooringDueTariff.MooringServiceProvider;
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

import static flagship.domain.tariffs.serviceduestariffs.MooringDueTariff.MooringServiceProvider.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("Mooring due calculator tests")
class MooringDueCalculatorTest {

  private static MooringDueTariff tariff;
  private final MooringDueCalculator calculator = new MooringDueCalculator();
  private PdaCase testCase;

  @BeforeAll
  public static void beforeClass() throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    tariff =
        mapper.readValue(
            new File("src/main/resources/mooringDueTariff.json"), MooringDueTariff.class);
  }

  @BeforeEach
  void setUp() {
    PdaShip testShip = PdaShip.builder().grossTonnage(BigDecimal.valueOf(1650)).build();
    PdaPort testPort = PdaPort.builder().mooringServiceProvider(VTC).build();
    testCase = PdaCase.builder().ship(testShip).port(testPort).build();
  }

  @DisplayName("Should calculate fixed mooring due for VTC and PortFleet within threshold")
  @ParameterizedTest(name = "provider : {arguments}")
  @EnumSource(
      value = MooringServiceProvider.class,
      names = {"VTC", "PORTFLEET"})
  void testCalculateFixedMooringDueForVtcAndPortFleet(MooringServiceProvider provider) {

    BigDecimal grossTonnage =
        getRandomGrossTonnage(150, tariff.getVtcGrossTonnageThreshold().intValue());

    testCase.getPort().setMooringServiceProvider(provider);
    testCase.getShip().setGrossTonnage(grossTonnage);

    calculator.set(testCase, tariff);

    BigDecimal fixedMooringDue = getFixedMooringDue(testCase.getPort().getMooringServiceProvider());

    BigDecimal expected = fixedMooringDue.multiply(BigDecimal.valueOf(2.0));
    BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Should calculate increased mooring due for VTC and PortFleet")
  @ParameterizedTest(name = "provider : {arguments}")
  @EnumSource(
      value = MooringServiceProvider.class,
      names = {"VTC", "PORTFLEET"})
  void testCalculateIncreasedMooringDueForVtcAndPortFleet(MooringServiceProvider provider) {

    BigDecimal grossTonnage =
        getRandomGrossTonnage(tariff.getVtcGrossTonnageThreshold().intValue(), 200000);

    testCase.getPort().setMooringServiceProvider(provider);
    testCase.getShip().setGrossTonnage(grossTonnage);

    calculator.set(testCase, tariff);

    BigDecimal fixedMooringDue = getFixedMooringDue(testCase.getPort().getMooringServiceProvider());

    BigDecimal additionalValue = getAdditionalValue();

    BigDecimal multiplier = getMultiplier(tariff.getVtcGrossTonnageThreshold());

    BigDecimal additionalDue = additionalValue.multiply(multiplier);

    BigDecimal expected = fixedMooringDue.add(additionalDue).multiply(BigDecimal.valueOf(2));
    BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Should calculate fixed mooring due for Lesport within threshold")
  @Test
  void testCalculateFixedMooringDueForLesport() {

    BigDecimal grossTonnage =
        getRandomGrossTonnage(150, tariff.getLesportGrossTonnageThreshold().intValue());

    testCase.getPort().setMooringServiceProvider(LESPORT);
    testCase.getShip().setGrossTonnage(grossTonnage);

    calculator.set(testCase, tariff);

    BigDecimal fixedMooringDue = getFixedMooringDue(testCase.getPort().getMooringServiceProvider());

    BigDecimal expected = fixedMooringDue.multiply(BigDecimal.valueOf(2.0));
    BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Should calculate increased mooring due for Lesport")
  @Test
  void testCalculateIncreasedMooringDueForLesport() {

    BigDecimal grossTonnage =
        getRandomGrossTonnage(tariff.getLesportGrossTonnageThreshold().intValue(), 200000);

    testCase.getPort().setMooringServiceProvider(LESPORT);
    testCase.getShip().setGrossTonnage(grossTonnage);

    calculator.set(testCase, tariff);

    BigDecimal fixedMooringDue = getFixedMooringDue(testCase.getPort().getMooringServiceProvider());

    BigDecimal additionalValue = getAdditionalValue();

    BigDecimal multiplier = getMultiplier(tariff.getLesportGrossTonnageThreshold());

    BigDecimal additionalDue = additionalValue.multiply(multiplier);

    BigDecimal expected = fixedMooringDue.add(additionalDue).multiply(BigDecimal.valueOf(2));
    BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Should calculate fixed mooring due for Port Balchik within threshold")
  @Test
  void testCalculateMooringDueForBalchikWithinThreshold() {

    BigDecimal grossTonnage =
        getRandomGrossTonnage(150, tariff.getBalchikGrossTonnageThreshold().intValue());

    testCase.getPort().setMooringServiceProvider(BALCHIK);
    testCase.getShip().setGrossTonnage(grossTonnage);

    calculator.set(testCase, tariff);

    BigDecimal expected =
        getFixedMooringDue(testCase.getPort().getMooringServiceProvider())
            .multiply(BigDecimal.valueOf(2));

    BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Should return fixed maximum mooring due for Port Balchik over threshold")
  @Test
  void testCalculateMooringDueForBalchikOverThreshold() {
    BigDecimal grossTonnage =
        getRandomGrossTonnage(tariff.getBalchikGrossTonnageThreshold().intValue(), 200000);

    testCase.getPort().setMooringServiceProvider(BALCHIK);
    testCase.getShip().setGrossTonnage(grossTonnage);

    calculator.set(testCase, tariff);

    BigDecimal expected = getAdditionalValue().multiply(BigDecimal.valueOf(2));
    BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Should calculate fixed mooring due for Odessos PBM within threshold")
  @Test
  void testCalculateMooringDueForOdessosWithinThreshold() {

    BigDecimal grossTonnage =
        getRandomGrossTonnage(150, tariff.getOdessosGrossTonnageThreshold().intValue());

    testCase.getPort().setMooringServiceProvider(ODESSOS);
    testCase.getShip().setGrossTonnage(grossTonnage);

    calculator.set(testCase, tariff);

    BigDecimal expected = getFixedMooringDue(testCase.getPort().getMooringServiceProvider());
    BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Should return fixed maximum mooring due for Odessos PBM over threshold")
  @Test
  void testCalculateMooringDueForOdessosOverThreshold() {
    BigDecimal grossTonnage =
        getRandomGrossTonnage(tariff.getOdessosGrossTonnageThreshold().intValue(), 200000);

    testCase.getPort().setMooringServiceProvider(ODESSOS);
    testCase.getShip().setGrossTonnage(grossTonnage);

    calculator.set(testCase, tariff);

    BigDecimal expected = getAdditionalValue();
    BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }

  private BigDecimal getFixedMooringDue(MooringServiceProvider mooringServiceProvider) {
    return tariff.getMooringDuesByProvider().get(mooringServiceProvider).entrySet().stream()
        .filter(entry -> shipGrossTonnageIsInRange(testCase, entry))
        .map(Map.Entry::getKey)
        .findFirst()
        .orElse(getBiggestFixedMooringDue(testCase));
  }

  private BigDecimal getAdditionalValue() {
    return BigDecimal.valueOf(
        tariff
            .getMooringDuesByProvider()
            .get(testCase.getPort().getMooringServiceProvider())
            .get(getBiggestFixedMooringDue(testCase))[2]);
  }

  private BigDecimal getBiggestFixedMooringDue(PdaCase testCase) {
    return tariff
        .getMooringDuesByProvider()
        .get(testCase.getPort().getMooringServiceProvider())
        .keySet()
        .stream()
        .max(Comparator.naturalOrder())
        .get();
  }

  private boolean shipGrossTonnageIsInRange(
      PdaCase testCase, Map.Entry<BigDecimal, Integer[]> entry) {
    return testCase.getShip().getGrossTonnage().intValue() >= entry.getValue()[0]
        && testCase.getShip().getGrossTonnage().intValue() <= entry.getValue()[1];
  }

  private BigDecimal getMultiplier(BigDecimal threshold) {
    double a =
        (testCase.getShip().getGrossTonnage().doubleValue() - threshold.doubleValue()) / 1000;
    double b = a - Math.floor(a) > 0 ? 1 : 0;

    return BigDecimal.valueOf((int) a + b);
  }

  private BigDecimal getRandomGrossTonnage(int min, int max) {
    Random random = new Random();
    return BigDecimal.valueOf(random.ints(min, max).findFirst().getAsInt());
  }
}
