package flagship.domain.calculators.stateduescalculators;

import flagship.domain.calculators.PrivateDueCalculator;
import flagship.domain.cases.dto.PdaCase;
import flagship.domain.tariffs.MarpolDueTariff;
import flagship.domain.tariffs.Tariff;
import flagship.domain.tariffs.mix.Due;
import flagship.domain.tariffs.mix.PortName;
import flagship.domain.tariffs.mix.Range;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;

public class MarpolDueCalculator extends PrivateDueCalculator {

  private MarpolDueTariff tariff;

  @Override
  public void set(final PdaCase source, final Tariff tariff) {
    super.source = source;
    this.tariff = (MarpolDueTariff) tariff;
  }

  @Override
  public BigDecimal calculate() {

    if (isOdessosPort()) {
      return tariff.getOdessosFixedMarpolDue();
    }

    return tariff.getMarpolDuePerGrossTonnage().entrySet().stream()
        .filter(this::grossTonnageIsWithinRange)
        .flatMap(e -> Arrays.stream(e.getValue()))
        .map(Due::getBase)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  private boolean isOdessosPort() {
    return source.getPort().getName().equals(PortName.ODESSOS_PBM.name);
  }

  public BigDecimal getFreeSewageDisposalQuantity() {
    return new FreeWasteDisposalQuantityAdvisor().getFreeSewageDisposalQuantity();
  }

  public BigDecimal getFreeGarbageDisposalQuantity() {
    return new FreeWasteDisposalQuantityAdvisor().getFreeGarbageDisposalQuantity();
  }

  private boolean grossTonnageIsWithinRange(final Map.Entry<Range, Due[]> entry) {
    return source.getShip().getGrossTonnage().intValue() >= entry.getKey().getMin()
        && source.getShip().getGrossTonnage().intValue() <= entry.getKey().getMax();
  }

  private class FreeWasteDisposalQuantityAdvisor {

    public BigDecimal getFreeSewageDisposalQuantity() {

      if (isOdessosPort()) {
        return tariff.getOdessosFreeSewageDisposalQuantity();
      }

      return getBaseDue(tariff.getFreeSewageDisposalQuantitiesPerGrossTonnage());
    }

    public BigDecimal getFreeGarbageDisposalQuantity() {

      if (isOdessosPort()) {
        return tariff.getOdessosFreeGarbageDisposalQuantity();
      }

      return getBaseDue(tariff.getFreeGarbageDisposalQuantitiesPerGrossTonnage());
    }
  }
}
