package flagship.domain.calculators.stateduescalculators;

import flagship.domain.calculators.PrivateDueCalculator;
import flagship.domain.cases.dto.PdaCase;
import flagship.domain.tariffs.Tariff;
import flagship.domain.tariffs.stateduestariffs.BoomContainmentTariff;

import java.math.BigDecimal;

import static flagship.domain.cases.entities.enums.ShipType.OIL_TANKER;

public class BoomContainmentCalculator extends PrivateDueCalculator<PdaCase, Tariff> {

  private BoomContainmentTariff tariff;

  @Override
  public void set(final PdaCase source, final Tariff tariff) {
    super.source = source;
    this.tariff = (BoomContainmentTariff) tariff;
  }

  @Override
  public BigDecimal calculate() {

    if (source.getShip().getType() == OIL_TANKER) {
      return getBaseDue(tariff.getBoomContainmentDuePerGrossTonnage());
    } else {
      return BigDecimal.ZERO;
    }
  }
}
