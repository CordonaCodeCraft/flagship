package flagship.domain.calculation.calculators;

import flagship.domain.caze.model.PdaCase;
import flagship.domain.tuples.due.Due;
import flagship.domain.tuples.range.Range;
import org.junit.jupiter.api.Tag;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Random;

@Tag("calculator")
public abstract class BaseCalculatorTest extends TariffsInitializer {

  public static final Integer MIN_GT = 150;
  public static final Integer MAX_GT = 650000;
  protected PdaCase testCase;

  protected BigDecimal getDueByRange(final Map<Range, Due> map) {
    return map.entrySet().stream()
        .filter(this::shipGrossTonnageIsInRange)
        .map(e -> e.getValue().getBase())
        .findFirst()
        .get();
  }

  protected <E> BigDecimal getDueByValue(final E value, final Map<E, Due> map) {
    return map.entrySet().stream()
        .filter(entry -> entry.getKey() == value)
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
    final Random random = new Random();
    return BigDecimal.valueOf(random.ints(min, max).findFirst().getAsInt());
  }

  protected BigDecimal getMultiplier(final BigDecimal threshold) {
    final double a =
        (testCase.getShip().getGrossTonnage().doubleValue() - threshold.doubleValue()) / 1000;
    final double b = a - Math.floor(a) > 0 ? 1 : 0;
    return BigDecimal.valueOf((int) a + b);
  }
}
