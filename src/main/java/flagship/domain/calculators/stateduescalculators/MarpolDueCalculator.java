package flagship.domain.calculators.stateduescalculators;

import flagship.domain.calculators.DueCalculator;
import flagship.domain.cases.dto.PdaCase;
import flagship.domain.tariffs.PortName;
import flagship.domain.tariffs.Range;
import flagship.domain.tariffs.Tariff;
import flagship.domain.tariffs.stateduestariffs.MarpolDueTariff;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;

public class MarpolDueCalculator implements DueCalculator<PdaCase, Tariff> {

  private PdaCase source;
  private MarpolDueTariff tariff;

  @Override
  public void set(final PdaCase source, final Tariff tariff) {
    this.source = source;
    this.tariff = (MarpolDueTariff) tariff;
  }

  @Override
  public BigDecimal calculate() {

    if (isOdessosPort()) {
      return tariff.getOdessosFixedMarpolDue();
    }

    if (grossTonnageOverThreshold()) {
      return Arrays.stream(tariff.getMaximumMarpolDueValues())
          .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    return tariff.getMarpolDuePerGrossTonnage().entrySet().stream()
        .filter(this::grossTonnageIsWithinRange)
        .flatMap(entry -> Arrays.stream(entry.getValue()))
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  private boolean isOdessosPort() {
    return source.getPort().getName().equals(PortName.ODESSOS_PBM.name);
  }
  public BigDecimal getFreeSewageDisposalQuantity() {
    return new FreeWasteAdvisor().getFreeSewageDisposalQuantity();
  }

  public BigDecimal getFreeGarbageDisposalQuantity() {
    return new FreeWasteAdvisor().getFreeGarbageDisposalQuantity();
  }

  private boolean grossTonnageOverThreshold() {
    return source.getShip().getGrossTonnage().intValue()
        > tariff.getMarpolDuePerGrossTonnage().keySet().stream()
            .mapToInt(Range::getMax)
            .max()
            .getAsInt();
  }

  private boolean grossTonnageIsWithinRange(final Map.Entry<Range, BigDecimal[]> entry) {
    return source.getShip().getGrossTonnage().intValue() >= entry.getKey().getMin()
        && source.getShip().getGrossTonnage().intValue() <= entry.getKey().getMax();
  }

  private class FreeWasteAdvisor {

    public BigDecimal getFreeSewageDisposalQuantity() {

      if (isOdessosPort()) {
        return tariff.getOdessosFreeSewageDisposalQuantity();
      }

      return tariff.getFreeSewageDisposalQuantitiesPerGrossTonnage().entrySet().stream()
          .filter(this::grossTonnageIsWithinFreeWasteRange)
          .map(Map.Entry::getValue)
          .findFirst()
          .orElse(tariff.getMaximumFreeSewageDisposalQuantity());
    }

    public BigDecimal getFreeGarbageDisposalQuantity() {

      if (isOdessosPort()) {
        return tariff.getOdessosFreeGarbageDisposalQuantity();
      }

      return tariff.getFreeGarbageDisposalQuantitiesPerGrossTonnage().entrySet().stream()
          .filter(this::grossTonnageIsWithinFreeWasteRange)
          .map(Map.Entry::getValue)
          .findFirst()
          .orElse(tariff.getMaximumFreeGarbageDisposalQuantity());
    }

    private boolean grossTonnageIsWithinFreeWasteRange(Map.Entry<Range, BigDecimal> entry) {
      return source.getShip().getGrossTonnage().intValue() >= entry.getKey().getMin()
          && source.getShip().getGrossTonnage().intValue() <= entry.getKey().getMax();
    }
  }
}
