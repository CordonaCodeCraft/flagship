package flagship.domain.tariffs.stateduestariffs;

import flagship.domain.tariffs.GtRange;
import flagship.domain.tariffs.Tariff;
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
public class BoomContainmentTariff extends Tariff {

    private Map<GtRange, BigDecimal> boomContainmentDuePerGrossTonnage;
    private BigDecimal maximumBoomContainmentDueValue;
}
