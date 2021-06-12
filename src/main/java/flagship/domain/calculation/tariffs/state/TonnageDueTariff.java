package flagship.domain.calculation.tariffs.state;

import flagship.domain.calculation.tariffs.Tariff;
import flagship.domain.tuples.due.Due;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

import static flagship.domain.caze.entity.Case.CallPurpose;
import static flagship.domain.caze.model.request.resolvers.PortAreaResolver.PortArea;
import static flagship.domain.ship.entity.Ship.ShipType;

@Getter
@Setter
@NoArgsConstructor
public class TonnageDueTariff extends Tariff {

  private Map<PortArea, Due> tonnageDuesByPortArea;
  private Map<ShipType, Due> tonnageDuesByShipType;
  private Map<CallPurpose, Due> tonnageDuesByCallPurpose;
  private Map<CallPurpose, BigDecimal> discountCoefficientsByCallPurpose;
  private Map<ShipType, BigDecimal> discountCoefficientsByShipType;
  private Set<ShipType> shipTypesNotEligibleForDiscount;
  private Set<CallPurpose> callPurposesNotEligibleForDiscount;
  private Integer callCountThreshold;
  private BigDecimal callCountDiscountCoefficient;
  private BigDecimal discountCoefficientForPortOfArrival;
}
