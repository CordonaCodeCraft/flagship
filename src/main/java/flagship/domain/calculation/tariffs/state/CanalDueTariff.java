package flagship.domain.calculation.tariffs.state;

import flagship.domain.base.due.tuple.Due;
import flagship.domain.calculation.tariffs.Tariff;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

import static flagship.domain.caze.model.request.resolvers.PortAreaResolver.PortArea;
import static flagship.domain.ship.entity.Ship.ShipType;

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
