package flagship.domain.calculators;

import flagship.domain.cases.dto.PdaCase;
import flagship.domain.tariffs.mix.Due;
import flagship.domain.tariffs.mix.Range;

import java.math.BigDecimal;
import java.util.Map;

public abstract class PrivateDueCalculator implements DueCalculator {

  protected PdaCase source;

  protected BigDecimal getBaseDue(final Map<Range, Due> map) {
    return map.entrySet().stream()
        .filter(entry -> grossTonnageIsInRange(source, entry))
        .map(e -> e.getValue().getBase())
        .findFirst()
        .get();
  }

  protected BigDecimal getAddition(final Map<Range, Due> map) {
    return map.entrySet().stream()
        .filter(entry -> grossTonnageIsInRange(source, entry))
        .map(e -> e.getValue().getAddition())
        .findFirst()
        .get();
  }

  protected BigDecimal getMultiplier(final BigDecimal threshold) {
    final double a =
        (source.getShip().getGrossTonnage().doubleValue() - threshold.doubleValue()) / 1000;
    final double b = a - Math.floor(a) > 0 ? 1 : 0;
    return BigDecimal.valueOf((int) a + b);
  }

  protected boolean grossTonnageExceedsThreshold(final BigDecimal threshold) {
    return source.getShip().getGrossTonnage().intValue() > threshold.intValue();
  }

  private boolean grossTonnageIsInRange(final PdaCase source, final Map.Entry<Range, Due> entry) {
    return source.getShip().getGrossTonnage().intValue() >= entry.getKey().getMin()
        && source.getShip().getGrossTonnage().intValue() <= entry.getKey().getMax();
  }
}
