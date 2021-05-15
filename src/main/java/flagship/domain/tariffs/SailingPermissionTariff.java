package flagship.domain.tariffs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class SailingPermissionTariff extends Tariff {
  private BigDecimal sailingPermissionDue;
}
