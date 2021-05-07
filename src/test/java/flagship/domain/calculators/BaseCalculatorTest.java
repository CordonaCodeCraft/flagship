package flagship.domain.calculators;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import flagship.config.serialization.DueDeserializer;
import flagship.config.serialization.GtRangeDeserializer;
import flagship.domain.cases.dto.PdaCase;
import flagship.domain.tariffs.Due;
import flagship.domain.tariffs.GtRange;
import flagship.domain.tariffs.agencyduestariffs.BasicAgencyDueTariff;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

@Tag("calculator")
public abstract class BaseCalculatorTest {

  private static final String TARIFFS_PATH = "src/main/resources/";

  private static final ObjectMapper mapper = new ObjectMapper();

  protected static BasicAgencyDueTariff basicAgencyDueTariff;

  protected PdaCase testCase;

  @BeforeAll
  public static void beforeClass() throws IOException {

    final SimpleModule simpleModule = new SimpleModule();

    simpleModule.addKeyDeserializer(GtRange.class, new GtRangeDeserializer());
    simpleModule.addDeserializer(Due.class, new DueDeserializer());

    mapper.registerModule(simpleModule);
    mapper.registerModule(new JavaTimeModule());

    basicAgencyDueTariff =
        mapper.readValue(
            new File(TARIFFS_PATH + "basicAgencyDueTariff.json"), BasicAgencyDueTariff.class);
  }

  protected BigDecimal getFixedDue(Map<GtRange, Due> map) {
    return map.entrySet().stream()
        .filter(this::shipGrossTonnageIsInRange)
        .map(e -> e.getValue().getBase())
        .findFirst()
        .get();
  }

  protected BigDecimal getAddition(Map<GtRange, Due> map) {
    return map.values().stream()
            .map(Due::getAddition)
            .filter(Objects::nonNull)
            .findFirst()
            .get();
  }

  protected boolean shipGrossTonnageIsInRange(Map.Entry<GtRange, Due> entry) {
    return testCase.getShip().getGrossTonnage().intValue() >= entry.getKey().getMin()
            && testCase.getShip().getGrossTonnage().intValue() <= entry.getKey().getMax();
  }

  protected BigDecimal getRandomGrossTonnage(final int min, final int max) {
    Random random = new Random();
    return BigDecimal.valueOf(random.ints(min, max).findFirst().getAsInt());
  }

  protected BigDecimal getMultiplier(final Integer threshold) {
    final double a =
            (testCase.getShip().getGrossTonnage().doubleValue() - threshold.doubleValue()) / 1000;
    final double b = a - Math.floor(a) > 0 ? 1 : 0;

    return BigDecimal.valueOf((int) a + b);
  }
}
