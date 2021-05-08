package flagship.domain.calculators.stateduescalculators;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import flagship.config.serialization.RangeDeserializer;
import flagship.domain.cases.dto.PdaCase;
import flagship.domain.cases.dto.PdaPort;
import flagship.domain.cases.dto.PdaShip;
import flagship.domain.tariffs.Range;
import flagship.domain.tariffs.stateduestariffs.MarpolDueTariff;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;
import java.util.Random;

import static flagship.domain.tariffs.PortName.ODESSOS_PBM;
import static flagship.domain.tariffs.PortName.VARNA_WEST;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("Marpol due calculator tests")
class MarpolDueCalculatorTest {

  private static MarpolDueTariff tariff;
  private final MarpolDueCalculator calculator = new MarpolDueCalculator();
  private PdaCase testCase;

  @BeforeAll
  public static void beforeClass() throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    SimpleModule simpleModule = new SimpleModule();
    simpleModule.addKeyDeserializer(Range.class, new RangeDeserializer());
    mapper.registerModule(simpleModule);
    tariff =
        mapper.readValue(
            new File("src/main/resources/marpolDueTariff.json"), MarpolDueTariff.class);
  }

  @BeforeEach
  void setUp() {
    PdaPort testPort = PdaPort.builder().name(VARNA_WEST.name).build();
    PdaShip testShip = PdaShip.builder().grossTonnage(BigDecimal.valueOf(1650.00)).build();
    testCase = PdaCase.builder().port(testPort).ship(testShip).build();
  }

  @DisplayName("Should calculate marpol due within gross tonnage threshold")
  @Test
  void testCalculateMarpolDueWithinThreshold() {

    BigDecimal randomGrossTonnage = getRandomGrossTonnage(500, getMarpolDueGrossTonnageThreshold());

    testCase.getShip().setGrossTonnage(randomGrossTonnage);

    calculator.set(testCase, tariff);

    BigDecimal expected =
        tariff.getMarpolDuePerGrossTonnage().entrySet().stream()
            .filter(this::grossTonnageIsWithinMarpolDueRange)
            .flatMap(entry -> Arrays.stream(entry.getValue()))
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Should return maximum marpol due value if ship gross tonnage exceeds threshold")
  @Test
  void testReturnsMaximumMarpolDue() {

    BigDecimal randomGrossTonnage =
        getRandomGrossTonnage(getMarpolDueGrossTonnageThreshold(), 200000);

    testCase.getShip().setGrossTonnage(randomGrossTonnage);

    calculator.set(testCase, tariff);

    BigDecimal expected =
        Arrays.stream(tariff.getMaximumMarpolDueValues()).reduce(BigDecimal.ZERO, BigDecimal::add);

    BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Should return Odessos fixed Marpol due")
  @Test
  void testReturnsOdessosFixedMarpolDue() {

    PdaPort port = PdaPort.builder().name(ODESSOS_PBM.name).build();
    testCase.setPort(port);

    calculator.set(testCase, tariff);

    BigDecimal expected = tariff.getOdessosFixedMarpolDue();
    BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Should return free sewage disposal quantity")
  @Test
  void testReturnsFreeSewageDisposalPerGrossTonnage() {

    BigDecimal grossTonnage =
        getRandomGrossTonnage(500, getFreeSewageDisposalGrossTonnageThreshold());

    testCase.getShip().setGrossTonnage(grossTonnage);

    calculator.set(testCase, tariff);

    BigDecimal expected =
        tariff.getFreeSewageDisposalQuantitiesPerGrossTonnage().entrySet().stream()
            .filter(this::grossTonnageIsWithinFreeWasteRange)
            .map(Map.Entry::getValue)
            .findFirst()
            .orElse(tariff.getMaximumFreeSewageDisposalQuantity());

    BigDecimal result = calculator.getFreeSewageDisposalQuantity();

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Should return maximum free sewage disposal quantity")
  @Test
  void testReturnsMaximumFreeSewageDisposalPerGrossTonnage() {

    BigDecimal grossTonnage =
        getRandomGrossTonnage(getFreeSewageDisposalGrossTonnageThreshold(), 200000);

    testCase.getShip().setGrossTonnage(grossTonnage);

    calculator.set(testCase, tariff);

    BigDecimal expected = tariff.getMaximumFreeSewageDisposalQuantity();
    BigDecimal result = calculator.getFreeSewageDisposalQuantity();

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Should return free garbage disposal quantity")
  @Test
  void testReturnsFreeGarbageDisposalPerGrossTonnage() {

    BigDecimal grossTonnage =
        getRandomGrossTonnage(500, getFreeGarbageDisposalGrossTonnageThreshold());

    testCase.getShip().setGrossTonnage(grossTonnage);

    calculator.set(testCase, tariff);

    BigDecimal expected =
        tariff.getFreeGarbageDisposalQuantitiesPerGrossTonnage().entrySet().stream()
            .filter(this::grossTonnageIsWithinFreeWasteRange)
            .map(Map.Entry::getValue)
            .findFirst()
            .orElse(tariff.getMaximumFreeSewageDisposalQuantity());

    BigDecimal result = calculator.getFreeGarbageDisposalQuantity();

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Should return maximum free garbage disposal quantity")
  @Test
  void testReturnsMaximumFreeGarbageDisposalPerGrossTonnage() {

    BigDecimal grossTonnage =
            getRandomGrossTonnage(getFreeGarbageDisposalGrossTonnageThreshold(), 200000);

    testCase.getShip().setGrossTonnage(grossTonnage);

    calculator.set(testCase, tariff);

    BigDecimal expected = tariff.getMaximumFreeGarbageDisposalQuantity();
    BigDecimal result = calculator.getFreeGarbageDisposalQuantity();

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Should return Odessos maximum free sewage value")
  @Test
  void testReturnsOdessosMaximumFreeSewageValue() {

    testCase.getPort().setName(ODESSOS_PBM.name);

    calculator.set(testCase, tariff);

    BigDecimal expected = tariff.getOdessosFreeSewageDisposalQuantity();
    BigDecimal result = calculator.getFreeSewageDisposalQuantity();

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Should return Odessos maximum free garbage value")
  @Test
  void testReturnsOdessosMaximumFreeGarbageValue() {

    testCase.getPort().setName(ODESSOS_PBM.name);

    calculator.set(testCase, tariff);

    BigDecimal expected = tariff.getOdessosFreeGarbageDisposalQuantity();
    BigDecimal result = calculator.getFreeGarbageDisposalQuantity();

    assertThat(result).isEqualByComparingTo(expected);
  }

  private int getMarpolDueGrossTonnageThreshold() {
    return tariff.getMarpolDuePerGrossTonnage().keySet().stream()
        .mapToInt(Range::getMax)
        .max()
        .getAsInt();
  }

  private int getFreeSewageDisposalGrossTonnageThreshold() {
    return tariff.getFreeSewageDisposalQuantitiesPerGrossTonnage().keySet().stream()
        .mapToInt(Range::getMax)
        .max()
        .getAsInt();
  }

  private int getFreeGarbageDisposalGrossTonnageThreshold() {
    return tariff.getFreeGarbageDisposalQuantitiesPerGrossTonnage().keySet().stream()
        .mapToInt(Range::getMax)
        .max()
        .getAsInt();
  }

  private boolean grossTonnageIsWithinMarpolDueRange(Map.Entry<Range, BigDecimal[]> entry) {
    return testCase.getShip().getGrossTonnage().intValue() >= entry.getKey().getMin()
        && testCase.getShip().getGrossTonnage().intValue() <= entry.getKey().getMax();
  }

  private boolean grossTonnageIsWithinFreeWasteRange(Map.Entry<Range, BigDecimal> entry) {
    return testCase.getShip().getGrossTonnage().intValue() >= entry.getKey().getMin()
        && testCase.getShip().getGrossTonnage().intValue() <= entry.getKey().getMax();
  }

  private BigDecimal getRandomGrossTonnage(int min, int max) {
    Random random = new Random();
    return BigDecimal.valueOf(random.ints(min, max).findFirst().getAsInt());
  }
}
