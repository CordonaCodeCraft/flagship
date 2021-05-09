package flagship.domain.calculators.agencyduescalculators;

import flagship.domain.calculators.DueCalculator;
import flagship.domain.cases.dto.PdaCase;
import flagship.domain.tariffs.Tariff;
import flagship.domain.tariffs.agencyduestariffs.AgencyDuesTariff;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
public class ClearanceDueCalculator implements DueCalculator<PdaCase, Tariff> {

  private AgencyDuesTariff tariff;

  @Override
  public void set(final PdaCase source, final Tariff tariff) {
    this.tariff = (AgencyDuesTariff) tariff;
  }

  @Override
  public BigDecimal calculate() {
    return tariff.getClearanceIn().add(tariff.getClearanceOut());
  }
}
