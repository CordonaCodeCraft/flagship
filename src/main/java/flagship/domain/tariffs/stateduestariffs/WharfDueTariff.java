package flagship.domain.tariffs.stateduestariffs;

import flagship.domain.cases.entities.enums.CallPurpose;
import flagship.domain.cases.entities.enums.ShipType;
import flagship.domain.tariffs.Due;
import flagship.domain.tariffs.Tariff;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
@Component
@NoArgsConstructor
public class WharfDueTariff extends Tariff {

  private Map<ShipType, Due> WharfDuesByShipType;
  private Map<CallPurpose, BigDecimal> discountCoefficientsByCallPurpose;
  private Set<ShipType> shipTypesNotEligibleForDiscount;
  private Due defaultWharfDue;
}
