package flagship.domain.calculators.agencyduescalculators;

import flagship.domain.calculators.DueCalculator;
import flagship.domain.cases.dto.PdaCase;
import flagship.domain.tariffs.Due;
import flagship.domain.tariffs.Range;
import flagship.domain.tariffs.Tariff;
import flagship.domain.tariffs.agencyduestariffs.AgencyDuesTariff;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

@NoArgsConstructor
public class BankExpensesDueCalculator implements DueCalculator<PdaCase, Tariff> {

  private PdaCase source;
  private AgencyDuesTariff tariff;

  @Override
  public void set(final PdaCase source, final Tariff tariff) {
    this.source = source;
    this.tariff = (AgencyDuesTariff) tariff;
  }

  @Override
  public BigDecimal calculate() {
    return getBaseDue().multiply(tariff.getBankExpensesCoefficient());
  }

  private BigDecimal getBaseDue() {
    return tariff.getBasicAgencyDuePerGrossTonnage().entrySet().stream()
            .filter(this::shipGrossTonnageIsInRange)
            .map(e -> e.getValue().getBase())
            .findFirst()
            .get();
  }

  private boolean shipGrossTonnageIsInRange(final Map.Entry<Range, Due> entry) {
    return source.getShip().getGrossTonnage().intValue() >= entry.getKey().getMin()
            && source.getShip().getGrossTonnage().intValue() <= entry.getKey().getMax();
  }
}
