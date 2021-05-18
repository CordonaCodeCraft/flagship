package flagship.domain.calculators.servicedues;

import flagship.domain.calculators.PrivateDueCalculator;
import flagship.domain.cases.dto.PdaCase;
import flagship.domain.tariffs.Tariff;
import flagship.domain.tariffs.mix.Due;
import flagship.domain.tariffs.mix.Range;
import flagship.domain.tariffs.servicedues.TugDueTariff;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

import static flagship.domain.PdaWarningsGenerator.WarningType.DANGEROUS_TUG_CARGO;

@NoArgsConstructor
public class TugDueCalculator extends PrivateDueCalculator {
  private TugDueTariff tariff;

  @Override
  public void set(final PdaCase source, final Tariff tariff) {
    super.source = source;
    this.tariff = (TugDueTariff) tariff;
  }

  @Override
  public BigDecimal calculate() {

    Map<Range, Due> dues = tariff.getTugDuesByArea().get(source.getPort().getTugArea());

    BigDecimal tugDue = getBaseDue(dues);

    if (grossTonnageExceedsThreshold(tariff.getGrossTonnageThreshold())) {
      final BigDecimal addition = getAddition(dues);
      final BigDecimal multiplier = getMultiplier(tariff.getGrossTonnageThreshold());
      tugDue = tugDue.add(addition.multiply(multiplier));
    }

    return tugDue.multiply(getTugCount());
  }

  private BigDecimal getTugCount() {

    final BigDecimal tugCount =
        tariff.getTugCountByGrossTonnage().entrySet().stream()
            .filter(this::gtInRange)
            .map(Map.Entry::getValue)
            .findFirst()
            .get();

    if (isNotEligibleForTugCountReduce()) {
      return tugCount;
    } else {
      return tugCount.intValue() == 1
          ? BigDecimal.valueOf(1.00)
          : tugCount.subtract(BigDecimal.valueOf(1.00));
    }
  }

  private boolean gtInRange(Map.Entry<Range, BigDecimal> entry) {
    return source.getShip().getGrossTonnage().intValue() >= entry.getKey().getMin()
        && source.getShip().getGrossTonnage().intValue() <= entry.getKey().getMax();
  }

  private boolean isNotEligibleForTugCountReduce() {
    return !source.getShip().getHasIncreasedManeuverability()
        || (source.getWarningTypes().contains(DANGEROUS_TUG_CARGO)
            && source.getShip().getGrossTonnage().intValue()
                < tariff.getGrossTonnageThresholdForTugCountReduce().intValue());
  }
}
