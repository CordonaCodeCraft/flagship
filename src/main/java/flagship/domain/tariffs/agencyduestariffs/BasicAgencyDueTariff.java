package flagship.domain.tariffs.agencyduestariffs;

import flagship.domain.tariffs.Due;
import flagship.domain.tariffs.GtRange;
import flagship.domain.tariffs.Tariff;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;

@Getter
@Setter
@Component
@NoArgsConstructor
public class BasicAgencyDueTariff extends Tariff {

  private Map<GtRange, Due> basicAgencyDuePerGrossTonnage;
  BigDecimal discountCoefficientForCallPurpose;
  BigDecimal grossTonnageThreshold;
}
