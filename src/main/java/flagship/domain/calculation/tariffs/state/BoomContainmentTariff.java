package flagship.domain.calculation.tariffs.state;

import flagship.domain.calculation.tariffs.Tariff;
import flagship.domain.tuples.due.Due;
import flagship.domain.tuples.range.Range;
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
