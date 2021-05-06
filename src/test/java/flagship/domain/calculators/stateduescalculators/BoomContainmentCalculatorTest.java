package flagship.domain.calculators.stateduescalculators;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import flagship.config.serialization.RangeDeserializer;
import flagship.domain.cases.dto.PdaCase;
import flagship.domain.cases.dto.PdaPort;
import flagship.domain.cases.dto.PdaShip;
import flagship.domain.tariffs.GtRange;
import flagship.domain.tariffs.stateduestariffs.BoomContainmentTariff;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Random;

import static flagship.domain.cases.entities.enums.ShipType.GENERAL;
import static flagship.domain.cases.entities.enums.ShipType.OIL_TANKER;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("Boom containment due calculator tests")
class BoomContainmentCalculatorTest {

  private static BoomContainmentTariff tariff;
  private final BoomContainmentCalculator calculator = new BoomContainmentCalculator();
  private PdaCase testCase;

  @BeforeAll
  public static void beforeClass() throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    SimpleModule simpleModule = new SimpleModule();
    simpleModule.addKeyDeserializer(GtRange.class, new RangeDeserializer());
    mapper.registerModule(simpleModule);
    tariff =
        mapper.readValue(
            new File("src/main/resources/boomContainmentDueTariff.json"),
            BoomContainmentTariff.class);
  }

  @BeforeEach
  void setUp() {
    PdaPort testPort = PdaPort.builder().build();
    PdaShip testShip =
        PdaShip.builder().type(OIL_TANKER).grossTonnage(BigDecimal.valueOf(1650)).build();
    testCase = PdaCase.builder().port(testPort).ship(testShip).build();
  }

  @DisplayName("Should return boom containment due within threshold")
  @Test
  void testReturnsBoomContainmentDueWithinThreshold() {
    BigDecimal grossTonnage = getRandomGrossTonnage(150, getBoomContainmentGrossTonnageThreshold());

    testCase.getShip().setGrossTonnage(grossTonnage);

    calculator.set(testCase, tariff);

    BigDecimal expected =
        tariff.getBoomContainmentDuePerGrossTonnage().entrySet().stream()
            .filter(this::grossTonnageIsWithinBoomContainmentDueRange)
            .map(Map.Entry::getValue)
            .findFirst()
            .orElse(tariff.getMaximumBoomContainmentDueValue());

    BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Should return maximum boom containment due value")
  @Test
  void testReturnsMaximumBoomContainmentDueValue() {
    BigDecimal grossTonnage =
        getRandomGrossTonnage(getBoomContainmentGrossTonnageThreshold(), 200000);

    testCase.getShip().setGrossTonnage(grossTonnage);

    calculator.set(testCase, tariff);

    BigDecimal expected = tariff.getMaximumBoomContainmentDueValue();

    BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Should return zero if ship type is not oil tanker")
  @Test
  void testReturnsZeroIfShipTypeIsNotOilTanker() {

    testCase.getShip().setType(GENERAL);

    calculator.set(testCase, tariff);

    BigDecimal expected = BigDecimal.ZERO;

    BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }


  private BigDecimal getRandomGrossTonnage(int min, int max) {
    Random random = new Random();
    return BigDecimal.valueOf(random.ints(min, max).findFirst().getAsInt());
  }

  private int getBoomContainmentGrossTonnageThreshold() {
    return tariff.getBoomContainmentDuePerGrossTonnage().keySet().stream()
        .mapToInt(GtRange::getMax)
        .max()
        .getAsInt();
  }

  private boolean grossTonnageIsWithinBoomContainmentDueRange(
      Map.Entry<GtRange, BigDecimal> entry) {
    return testCase.getShip().getGrossTonnage().intValue() >= entry.getKey().getMin()
        && testCase.getShip().getGrossTonnage().intValue() <= entry.getKey().getMax();
  }
}
