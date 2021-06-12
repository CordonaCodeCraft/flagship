package flagship.domain.calculation.tariffs.state;

import flagship.domain.calculation.tariffs.Tariff;
import flagship.domain.tuples.due.Due;
import flagship.domain.tuples.range.Range;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class MarpolDueTariff extends Tariff {

  private Map<Range, Due> freeSewageDisposalQuantitiesPerGrossTonnage;
  private Map<Range, Due> freeGarbageDisposalQuantitiesPerGrossTonnage;
  private Map<Range, Due[]> marpolDuePerGrossTonnage;
  private BigDecimal odessosFixedMarpolDue;
  private BigDecimal odessosFreeSewageDisposalQuantity;
  private BigDecimal odessosFreeGarbageDisposalQuantity;
}
