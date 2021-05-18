package flagship.domain.calculators.statedues;

import flagship.domain.calculators.DueCalculator;
import flagship.domain.cases.dto.PdaCase;
import flagship.domain.tariffs.Tariff;
import flagship.domain.tariffs.statedues.SailingPermissionTariff;
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
