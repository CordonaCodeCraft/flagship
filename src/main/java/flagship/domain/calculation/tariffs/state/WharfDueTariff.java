package flagship.domain.calculation.tariffs.state;

import flagship.domain.base.due.tuple.Due;
import flagship.domain.calculation.tariffs.Tariff;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

import static flagship.domain.caze.entity.Case.CallPurpose;
import static flagship.domain.ship.entity.Ship.ShipType;

@Getter
@Setter
@NoArgsConstructor
public class WharfDueTariff extends Tariff {

  private Map<ShipType, Due> WharfDuesByShipType;
  private Map<CallPurpose, BigDecimal> discountCoefficientsByCallPurpose;
  private Set<ShipType> shipTypesNotEligibleForDiscount;
  private Due defaultWharfDue;
}
