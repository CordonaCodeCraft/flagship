package flagship.domain.calculation.tariffs.state;

import flagship.domain.calculation.tariffs.Tariff;
import flagship.domain.base.due.tuple.Due;
import flagship.domain.caze.entity.Case;
import flagship.domain.port.entity.Port;
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
public class TonnageDueTariff extends Tariff {

  private Map<Port.PortArea, Due> tonnageDuesByPortArea;
  private Map<Ship.ShipType, Due> tonnageDuesByShipType;
  private Map<Case.CallPurpose, Due> tonnageDuesByCallPurpose;
  private Map<Case.CallPurpose, BigDecimal> discountCoefficientsByCallPurpose;
  private Map<Ship.ShipType, BigDecimal> discountCoefficientsByShipType;
  private Set<Ship.ShipType> shipTypesNotEligibleForDiscount;
  private Set<Case.CallPurpose> callPurposesNotEligibleForDiscount;
  private Integer callCountThreshold;
  private BigDecimal callCountDiscountCoefficient;
  private BigDecimal discountCoefficientForPortOfArrival;
}
