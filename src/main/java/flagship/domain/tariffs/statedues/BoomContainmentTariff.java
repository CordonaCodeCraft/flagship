package flagship.domain.tariffs.statedues;

import flagship.domain.tariffs.Tariff;
import flagship.domain.tariffs.mix.Due;
import flagship.domain.tariffs.mix.Range;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class BoomContainmentTariff extends Tariff {

  private Map<Range, Due> boomContainmentDuePerGrossTonnage;
}
