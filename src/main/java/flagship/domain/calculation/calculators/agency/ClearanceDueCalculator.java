package flagship.domain.calculation.calculators.agency;

import flagship.domain.calculation.calculators.DueCalculator;
import flagship.domain.calculation.tariffs.Tariff;
import flagship.domain.calculation.tariffs.agency.AgencyDuesTariff;
import flagship.domain.pda.model.PdaCase;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
public class ClearanceDueCalculator implements DueCalculator {

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
