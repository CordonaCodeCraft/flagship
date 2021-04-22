package flagship.domain.utils.tariffs.serviceduestariffs;

import flagship.domain.cases.entities.enums.CargoType;
import flagship.domain.cases.entities.enums.PilotageArea;
import flagship.domain.utils.tariffs.Tariff;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;

@Getter
@Setter
@Component
@NoArgsConstructor
public class PilotageDueTariff extends Tariff {

    private Map<PilotageArea, Map<BigDecimal, Integer[]>> pilotageDuesByArea;
    private Map<CargoType, BigDecimal> increaseCoefficientsByCargoType;
    private Map<PdaWarning, BigDecimal> increaseCoefficientsByWarningType;
    private BigDecimal grossTonnageThreshold;

}
