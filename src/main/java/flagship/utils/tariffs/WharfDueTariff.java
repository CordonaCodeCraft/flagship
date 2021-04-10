package flagship.utils.tariffs;

import flagship.domain.entities.enums.CallPurpose;
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
public class WharfDueTariff extends Tariff {

    private Map<CallPurpose, Double> discountCoefficientsByCallPurpose;
    private Set<ShipType> shipTypesNotEligibleForDiscount;
    private Map<ShipType, Double> WharfDuesByShipType;
    private Double defaultWharfDue;

}
