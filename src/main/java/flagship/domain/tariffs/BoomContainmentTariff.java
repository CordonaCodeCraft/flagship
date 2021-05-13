package flagship.domain.tariffs;

import flagship.domain.tariffs.mix.Due;
import flagship.domain.tariffs.mix.Range;
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
