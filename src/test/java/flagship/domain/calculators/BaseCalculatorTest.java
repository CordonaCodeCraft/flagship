package flagship.domain.calculators;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import flagship.config.serialization.DueDeserializer;
import flagship.config.serialization.RangeDeserializer;
import flagship.domain.cases.dto.PdaCase;
import flagship.domain.tariffs.Due;
import flagship.domain.tariffs.Range;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Random;

@Tag("calculator")
public abstract class BaseCalculatorTest {

  protected static final String TARIFFS_PATH = "src/main/resources/";
  protected static final Integer MIN_GT = 150;
  protected static final Integer MAX_GT = 650000;

  protected static final ObjectMapper mapper = new ObjectMapper();

  protected PdaCase testCase;

  @BeforeAll
  public static void beforeClass() throws IOException {
    final SimpleModule simpleModule = new SimpleModule();
    simpleModule.addKeyDeserializer(Range.class, new RangeDeserializer());
    simpleModule.addDeserializer(Due.class, new DueDeserializer());
    mapper.registerModule(simpleModule);
    mapper.registerModule(new JavaTimeModule());
  }

  protected BigDecimal getFixedDue(final Map<Range, Due> map) {
    return map.entrySet().stream()
        .filter(this::shipGrossTonnageIsInRange)
        .map(e -> e.getValue().getBase())
        .findFirst()
        .get();
  }

  protected BigDecimal getAddition(final Map<Range, Due> map) {
    return map.entrySet().stream()
        .filter(this::shipGrossTonnageIsInRange)
        .map(e -> e.getValue().getAddition())
        .findFirst()
        .get();
  }

  protected boolean shipGrossTonnageIsInRange(final Map.Entry<Range, Due> entry) {
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
