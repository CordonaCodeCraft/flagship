package flagship.domain.tariffs.statedues;

import flagship.domain.tariffs.Tariff;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class SailingPermissionTariff extends Tariff {
  private BigDecimal sailingPermissionDue;
}
