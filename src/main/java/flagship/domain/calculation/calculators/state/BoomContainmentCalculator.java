package flagship.domain.calculation.calculators.state;

import flagship.domain.calculation.calculators.PrivateDueCalculator;
import flagship.domain.calculation.tariffs.Tariff;
import flagship.domain.calculation.tariffs.state.BoomContainmentTariff;
import flagship.domain.caze.model.PdaCase;

import java.math.BigDecimal;

import static flagship.domain.ship.entity.Ship.ShipType.OIL_TANKER;

public class BoomContainmentCalculator extends PrivateDueCalculator {

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
