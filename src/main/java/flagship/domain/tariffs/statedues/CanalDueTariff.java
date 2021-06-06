package flagship.domain.tariffs.statedues;

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
public class CanalDueTariff extends Tariff {

  private Map<PortArea, Due> canalDuesByPortArea;
  private Map<Ship.ShipType, BigDecimal> discountCoefficientByShipType;
  private Map<PortArea, BigDecimal> discountCoefficientsByPortAreaForContainers;
  private Map<PortArea, BigDecimal> discountCoefficientsByPortAreaPerCallCountForContainers;
  private Set<Ship.ShipType> shipTypesNotEligibleForDiscount;
  private Integer callCountThreshold;
  private BigDecimal defaultCallCountDiscountCoefficient;
}
