package flagship.domain.calculators.servicedues;

import flagship.domain.calculators.tariffs.Tariff;
import flagship.domain.calculators.tariffs.serviceduestariffs.TugDueTariff;
import flagship.domain.cases.dto.PdaCase;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.*;

import static flagship.domain.calculators.tariffs.enums.PdaWarning.HOLIDAY;

@NoArgsConstructor
public class TugDueCalculator extends ServiceDueCalculator<PdaCase, Tariff> {

  private PdaCase source;
  private TugDueTariff tariff;

  @Override
  public void set(final PdaCase source, final Tariff tariff) {
    this.source = source;
    this.tariff = (TugDueTariff) tariff;
  }

  @Override
  public BigDecimal calculate() {

    BigDecimal tugDue = getFixedTugDue();

    if (grossTonnageIsAboveThreshold()) {
      tugDue = tugDue.add(calculateAdditionalDue());
    }

    return tugDue.multiply(getTugCount());
  }

  private BigDecimal calculateAdditionalDue() {
    return getDueAdditionalValue().multiply(getMultiplier());
  }

  private BigDecimal getFixedTugDue() {
    return tariff.getTugDuesByArea().get(source.getPort().getTugArea()).entrySet().stream()
        .filter(this::shipGrossTonnageInRange)
        .map(Map.Entry::getKey)
        .findFirst()
        .orElse(getBiggestFixedTugDue());
  }

  private boolean grossTonnageIsAboveThreshold() {
    return source.getShip().getGrossTonnage().doubleValue()
        > tariff.getGrossTonnageThreshold().doubleValue();
  }

  private BigDecimal getDueAdditionalValue() {
    return BigDecimal.valueOf(
        tariff
            .getTugDuesByArea()
            .get(source.getPort().getTugArea())
            .get(getBiggestFixedTugDue())[2]);
  }

  private BigDecimal getMultiplier() {
    final double a =
        (source.getShip().getGrossTonnage().doubleValue()
                - tariff.getGrossTonnageThreshold().doubleValue())
            / 1000;
    final double b = a - Math.floor(a) > 0 ? 1 : 0;

    return BigDecimal.valueOf((int) a + b);
  }

  private BigDecimal getBiggestFixedTugDue() {
    return tariff.getTugDuesByArea().get(source.getPort().getTugArea()).keySet().stream()
        .max(Comparator.naturalOrder())
        .get();
  }

  private BigDecimal getTugCount() {

    final BigDecimal tugCount =
        tariff.getTugCountByGrossTonnage().entrySet().stream()
            .filter(this::shipGrossTonnageInRange)
            .map(Map.Entry::getKey)
            .findFirst()
            .orElse(tariff.getMaximumTugCount());

    if (isNotEligibleForTugCountReduce()) {
      return tugCount;
    } else {
      return tugCount.intValue() == 1
          ? BigDecimal.valueOf(1)
          : tugCount.subtract(BigDecimal.valueOf(1));
    }
  }

  private boolean isNotEligibleForTugCountReduce() {
    return !source.getShip().getHasIncreasedManeuverability()
        || (source.getShip().getTransportsDangerousCargo()
            && source.getShip().getGrossTonnage().intValue()
                < tariff.getGrossTonnageThresholdForTugCountReduce().intValue());
  }

  private boolean shipGrossTonnageInRange(final Map.Entry<BigDecimal, Integer[]> entry) {
    return source.getShip().getGrossTonnage().intValue() >= entry.getValue()[0]
        && source.getShip().getGrossTonnage().intValue() <= entry.getValue()[1];
  }
}
