package flagship.domain.tariffs;

import flagship.domain.cases.entities.enums.CallPurpose;
import flagship.domain.cases.entities.enums.ShipType;
import flagship.domain.tariffs.mix.Due;
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
