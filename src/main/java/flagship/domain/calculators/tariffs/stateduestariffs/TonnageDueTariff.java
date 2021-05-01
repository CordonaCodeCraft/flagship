package flagship.domain.calculators.tariffs.stateduestariffs;

import flagship.domain.calculators.tariffs.Tariff;
import flagship.domain.calculators.tariffs.enums.PortArea;
import flagship.domain.cases.entities.enums.CallPurpose;
import flagship.domain.cases.entities.enums.ShipType;
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

  Integer callCountThreshold;
  BigDecimal callCountDiscountCoefficient;
  BigDecimal discountCoefficientForPortOfArrival;
  private Map<PortArea, BigDecimal> tonnageDuesByPortArea;
  private Map<ShipType, BigDecimal> tonnageDuesByShipType;
  private Map<CallPurpose, BigDecimal> tonnageDuesByCallPurpose;
  private Map<CallPurpose, BigDecimal> discountCoefficientsByCallPurpose;
  private Map<ShipType, BigDecimal> discountCoefficientsByShipType;
  private Set<ShipType> shipTypesNotEligibleForDiscount;
  private Set<CallPurpose> callPurposesNotEligibleForDiscount;
}
