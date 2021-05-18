package flagship.domain.tariffs.statedues;

import flagship.domain.cases.entities.enums.ShipType;
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
  private Map<ShipType, BigDecimal> discountCoefficientByShipType;
  private Map<PortArea, BigDecimal> discountCoefficientsByPortAreaForContainers;
  private Map<PortArea, BigDecimal> discountCoefficientsByPortAreaPerCallCountForContainers;
  private Set<ShipType> shipTypesNotEligibleForDiscount;
  private Integer callCountThreshold;
  private BigDecimal defaultCallCountDiscountCoefficient;
}
