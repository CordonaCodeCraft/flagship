package flagship.domain.tariffs.statedues;

import flagship.domain.cases.entities.Case;
import flagship.domain.cases.entities.Ship;
import flagship.domain.tariffs.Tariff;
import flagship.domain.tariffs.mix.Due;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class WharfDueTariff extends Tariff {

  private Map<Ship.ShipType, Due> WharfDuesByShipType;
  private Map<Case.CallPurpose, BigDecimal> discountCoefficientsByCallPurpose;
  private Set<Ship.ShipType> shipTypesNotEligibleForDiscount;
  private Due defaultWharfDue;
}
