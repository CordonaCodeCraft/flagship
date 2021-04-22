package flagship.domain.utils.tariffs.stateduestariffs;

import flagship.domain.cases.entities.enums.PortArea;
import flagship.domain.cases.entities.enums.ShipType;
import flagship.domain.utils.tariffs.Tariff;
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
public class CanalDueTariff extends Tariff {

  private Map<PortArea, BigDecimal> canalDuesByPortArea;
  private Map<ShipType, BigDecimal> discountCoefficientByShipType;
  private Map<PortArea, BigDecimal> discountCoefficientsByPortAreaForContainers;
  private Map<PortArea, BigDecimal> discountCoefficientsByPortAreaPerCallCountForContainers;
  private Set<ShipType> shipTypesNotEligibleForDiscount;
  private Integer callCountThreshold;
  private BigDecimal defaultCallCountDiscountCoefficient;
}
