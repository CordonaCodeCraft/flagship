package flagship.domain.calculators.serviceduescalculators;

import flagship.domain.calculators.DueCalculator;
import flagship.domain.cases.dto.PdaCase;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Map;

public abstract class ServiceDueCalculator<S, T> implements DueCalculator<S, T> {

  protected <K> BigDecimal getFixedDue(
          final PdaCase source, final K key, final Map<K, Map<BigDecimal, Integer[]>> map) {
    return map.get(key).entrySet().stream()
        .filter(entry -> grossTonnageIsInRange(source, entry))
        .map(Map.Entry::getKey)
        .findFirst()
        .orElse(getBiggestDueValue(key, map));
  }

  protected boolean grossTonnageIsInRange(
      final PdaCase source, final Map.Entry<BigDecimal, Integer[]> entry) {
    return source.getShip().getGrossTonnage().intValue() >= entry.getValue()[0]
        && source.getShip().getGrossTonnage().intValue() <= entry.getValue()[1];
  }

  protected <K> BigDecimal getBiggestDueValue(
          final K key, final Map<K, Map<BigDecimal, Integer[]>> map) {
    return map.get(key).keySet().stream().max(Comparator.naturalOrder()).get();
  }

  protected <K> BigDecimal getAdditionalDueValue(
          final K key, final Map<K, Map<BigDecimal, Integer[]>> map) {
    return BigDecimal.valueOf(map.get(key).get(getBiggestDueValue(key, map))[2]);
  }

  protected boolean grossTonnageIsAboveThreshold(final PdaCase source, final BigDecimal threshold) {
    return source.getShip().getGrossTonnage().doubleValue() > threshold.doubleValue();
  }
}
