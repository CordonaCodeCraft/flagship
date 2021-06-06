package flagship.domain.calculation.tariffs.state;

import flagship.domain.calculation.tariffs.Tariff;
import flagship.domain.base.due.tuple.Due;
import flagship.domain.caze.entity.Case;
import flagship.domain.ship.entity.Ship;
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
