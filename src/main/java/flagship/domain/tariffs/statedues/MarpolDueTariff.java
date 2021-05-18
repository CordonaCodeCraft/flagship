package flagship.domain.tariffs.statedues;

import flagship.domain.tariffs.Tariff;
import flagship.domain.tariffs.mix.Due;
import flagship.domain.tariffs.mix.Range;
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
