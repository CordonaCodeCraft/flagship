package flagship.domain.calculators.stateduescalculators;

import flagship.domain.calculators.DueCalculator;
import flagship.domain.cases.dto.PdaCase;
import flagship.domain.tariffs.Range;
import flagship.domain.tariffs.Tariff;
import flagship.domain.tariffs.stateduestariffs.BoomContainmentTariff;

import java.math.BigDecimal;
import java.util.Map;

import static flagship.domain.cases.entities.enums.ShipType.OIL_TANKER;

public class BoomContainmentCalculator implements DueCalculator<PdaCase, Tariff> {

  private PdaCase source;
  private BoomContainmentTariff tariff;

  @Override
  public void set(final PdaCase source, final Tariff tariff) {
    this.source = source;
    this.tariff = (BoomContainmentTariff) tariff;
  }

  @Override
  public BigDecimal calculate() {

    if (source.getShip().getType() == OIL_TANKER) {
      return tariff.getBoomContainmentDuePerGrossTonnage().entrySet().stream()
          .filter(this::grossTonnageIsWithinBoomContainmentDueRange)
          .map(Map.Entry::getValue)
          .findFirst()
          .orElse(tariff.getMaximumBoomContainmentDueValue());
    } else {
      return BigDecimal.ZERO;
    }
  }

  private boolean grossTonnageIsWithinBoomContainmentDueRange(final
      Map.Entry<Range, BigDecimal> entry) {
    return source.getShip().getGrossTonnage().intValue() >= entry.getKey().getMin()
        && source.getShip().getGrossTonnage().intValue() <= entry.getKey().getMax();
  }
}
