package flagship.utils.tariffs;

import flagship.domain.entities.Ship;
import flagship.domain.entities.enums.PortArea;
import flagship.domain.entities.enums.ShipType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

@Getter
@Setter
@Component
@NoArgsConstructor
public class CanalDueTariff extends Tariff {

    private Map<PortArea, Double> canalDuesByPortArea;
    private Map<ShipType, Double> discountCoefficientByShipType;
    private Map<PortArea, Double> discountCoefficientsByPortAreaForContainers;
    private Map<PortArea, Double> discountCoefficientsByPortAreaPerCallCountForContainers;
    private Set<ShipType> shipTypesNotEligibleForDiscount;
    private int callCountThreshold;
    private Double defaultCallCountDiscountCoefficient;

}
