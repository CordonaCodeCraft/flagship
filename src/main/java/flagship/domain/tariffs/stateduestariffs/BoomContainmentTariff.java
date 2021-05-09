package flagship.domain.tariffs.stateduestariffs;

import flagship.domain.tariffs.Due;
import flagship.domain.tariffs.Range;
import flagship.domain.tariffs.Tariff;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.Map;

@Getter
@Setter
@Component
@NoArgsConstructor
public class BoomContainmentTariff extends Tariff {

  private Map<Range, Due> boomContainmentDuePerGrossTonnage;
}
