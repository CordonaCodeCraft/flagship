package flagship.domain.utils.tariffs;

import flagship.domain.cases.entities.enums.CallPurpose;
import flagship.domain.cases.entities.enums.PortArea;
import flagship.domain.cases.entities.enums.ShipType;
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
public class TonnageDueTariff extends Tariff {

    private Map<PortArea, Double> tonnageDuesByPortArea;
    private Map<ShipType, Double> tonnageDuesByShipType;
    private Map<CallPurpose, Double> tonnageDuesByCallPurpose;
    private Map<CallPurpose, Double> discountCoefficientsByCallPurpose;
    private Map<ShipType, Double> discountCoefficientsByShipType;
    private Set<ShipType> shipTypesNotEligibleForDiscount;
    private Set<CallPurpose> callPurposesNotEligibleForDiscount;
    int callCountThreshold;
    double callCountDiscountCoefficient;
}
