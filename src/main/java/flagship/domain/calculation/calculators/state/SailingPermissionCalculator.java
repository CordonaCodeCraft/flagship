package flagship.domain.calculation.calculators.state;

import flagship.domain.calculation.calculators.DueCalculator;
import flagship.domain.calculation.tariffs.Tariff;
import flagship.domain.calculation.tariffs.state.SailingPermissionTariff;
import flagship.domain.pda.model.PdaCase;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
public class SailingPermissionCalculator implements DueCalculator {

  private PdaCase source;
  private SailingPermissionTariff tariff;

  @Override
  public void set(final PdaCase source, final Tariff tariff) {
    this.source = source;
    this.tariff = (SailingPermissionTariff) tariff;
  }

  @Override
  public BigDecimal calculate() {
    return tariff.getSailingPermissionDue();
  }
}
