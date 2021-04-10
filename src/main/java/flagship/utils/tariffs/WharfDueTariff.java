package flagship.utils.tariffs;

import flagship.domain.entities.enums.CallPurpose;
import flagship.domain.entities.enums.ShipType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
@Component
@NoArgsConstructor
public class WharfDueTariff implements Serializable {

    private static final long serialVersionUID = 7488308811075008968L;

    private Map<CallPurpose, Double> discountCoefficientsByCallPurpose;
    private Set<ShipType> shipTypesNotEligibleForDiscount;
    private Map<ShipType, Double> WharfDuesByShipType;
    private Double defaultWharfDue;

}
