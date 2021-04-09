package flagship.utils.tariffs;

import flagship.domain.entities.enums.CallPurpose;
import flagship.domain.entities.enums.PortArea;
import flagship.domain.entities.enums.ShipType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Component
@NoArgsConstructor
public class TonnageDueTariff implements Serializable {

    private static final long serialVersionUID = 7488308811075008968L;

    private Map<ShipType, Double> tonnageDuesByShipType;
    private Map<CallPurpose, Double> tonnageDuesByCallPurpose;
    private Map<PortArea, Double> tonnageDuesByPortArea;
    private Map<ShipType, Double> discountCoefficientsByShipType;

    List<ShipType> shipTypesAffectingTonnageDue;
    List<CallPurpose> callPurposesAffectingTonnageDue;
    List<ShipType> shipTypesNotEligibleForDiscount;
    List<CallPurpose> callPurposesNotEligibleForDiscount;
    List<CallPurpose> callPurposesEligibleForDiscount;

    int callCountThreshold;
    double callCountDiscountCoefficient;
    double callPurposeDiscountCoefficient;


}
