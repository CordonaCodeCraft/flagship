package flagship.domain.calculation.tariffs.state;

import flagship.domain.calculation.tariffs.Tariff;
import flagship.domain.base.due.tuple.Due;
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
public class CanalDueTariff extends Tariff {

  private Map<Port.PortArea, Due> canalDuesByPortArea;
  private Map<Ship.ShipType, BigDecimal> discountCoefficientByShipType;
  private Map<Port.PortArea, BigDecimal> discountCoefficientsByPortAreaForContainers;
  private Map<Port.PortArea, BigDecimal> discountCoefficientsByPortAreaPerCallCountForContainers;
  private Set<Ship.ShipType> shipTypesNotEligibleForDiscount;
  private Integer callCountThreshold;
  private BigDecimal defaultCallCountDiscountCoefficient;
}
