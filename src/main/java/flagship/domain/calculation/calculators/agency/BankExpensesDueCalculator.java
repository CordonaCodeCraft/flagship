package flagship.domain.calculation.calculators.agency;

import flagship.domain.calculation.calculators.PrivateDueCalculator;
import flagship.domain.calculation.tariffs.Tariff;
import flagship.domain.calculation.tariffs.agency.AgencyDuesTariff;
import flagship.domain.caze.model.PdaCase;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
public class BankExpensesDueCalculator extends PrivateDueCalculator {

  private AgencyDuesTariff tariff;

  @Override
  public void set(final PdaCase source, final Tariff tariff) {
    super.source = source;
    this.tariff = (AgencyDuesTariff) tariff;
  }

  @Override
  public BigDecimal calculate() {
    return getBaseDue(tariff.getBasicAgencyDuePerGrossTonnage())
        .multiply(tariff.getBankExpensesCoefficient());
  }
}
