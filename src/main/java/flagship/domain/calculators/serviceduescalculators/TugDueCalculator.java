package flagship.domain.calculators.serviceduescalculators;

import flagship.domain.tariffs.Tariff;
import flagship.domain.tariffs.serviceduestariffs.TugDueTariff;
import flagship.domain.cases.dto.PdaCase;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

import static flagship.domain.tariffs.PdaWarningsGenerator.PdaWarning.*;

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

    BigDecimal tugDue =
        getFixedDue(source, source.getPort().getTugArea(), tariff.getTugDuesByArea());

    if (grossTonnageIsAboveThreshold(source, tariff.getGrossTonnageThreshold())) {
      tugDue = tugDue.add(calculateAdditionalDue());
    }

    return tugDue.multiply(getTugCount());
  }

  private BigDecimal calculateAdditionalDue() {
    return getAdditionalDueValue(source.getPort().getTugArea(), tariff.getTugDuesByArea())
        .multiply(getMultiplier());
  }

  private BigDecimal getMultiplier() {
    final double a =
        (source.getShip().getGrossTonnage().doubleValue()
                - tariff.getGrossTonnageThreshold().doubleValue())
            / 1000;
    final double b = a - Math.floor(a) > 0 ? 1 : 0;

    return BigDecimal.valueOf((int) a + b);
  }

  private BigDecimal getTugCount() {

    final BigDecimal tugCount =
        tariff.getTugCountByGrossTonnage().entrySet().stream()
            .filter(entry -> grossTonnageIsInRange(source, entry))
            .map(Map.Entry::getKey)
            .findFirst()
            .orElse(tariff.getMaximumTugCount());

    if (isNotEligibleForTugCountReduce()) {
      return tugCount;
    } else {
      return tugCount.intValue() == 1
          ? BigDecimal.valueOf(1.00)
          : tugCount.subtract(BigDecimal.valueOf(1.00));
    }
  }

  private boolean isNotEligibleForTugCountReduce() {
    return !source.getShip().getHasIncreasedManeuverability()
        || (source.getWarnings().contains(DANGEROUS_TUG_CARGO)
            && source.getShip().getGrossTonnage().intValue()
                < tariff.getGrossTonnageThresholdForTugCountReduce().intValue());
  }

}
