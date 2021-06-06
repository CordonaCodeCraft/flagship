package flagship.domain.tariffs.statedues;

import flagship.domain.cases.entities.Case;
import flagship.domain.cases.entities.Ship;
import flagship.domain.tariffs.PortArea;
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
public class TonnageDueTariff extends Tariff {

  private Map<PortArea, Due> tonnageDuesByPortArea;
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
