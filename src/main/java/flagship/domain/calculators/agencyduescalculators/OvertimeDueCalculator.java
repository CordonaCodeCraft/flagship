package flagship.domain.calculators.agencyduescalculators;

import flagship.domain.calculators.PrivateDueCalculator;
import flagship.domain.cases.dto.PdaCase;
import flagship.domain.tariffs.AgencyDuesTariff;
import flagship.domain.tariffs.Tariff;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
public class OvertimeDueCalculator extends PrivateDueCalculator {

  private AgencyDuesTariff tariff;

  @Override
  public void set(final PdaCase source, final Tariff tariff) {
    super.source = source;
    this.tariff = (AgencyDuesTariff) tariff;
  }

  @Override
  public BigDecimal calculate() {
    return getBaseDue(tariff.getBasicAgencyDuePerGrossTonnage())
        .multiply(tariff.getOvertimeCoefficient());
  }
}
