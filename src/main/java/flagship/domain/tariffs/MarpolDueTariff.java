package flagship.domain.tariffs;

import flagship.domain.tariffs.mix.Due;
import flagship.domain.tariffs.mix.Range;
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
public class MarpolDueTariff extends Tariff {

  private Map<Range, Due> freeSewageDisposalQuantitiesPerGrossTonnage;
  private Map<Range, Due> freeGarbageDisposalQuantitiesPerGrossTonnage;
  private Map<Range, Due[]> marpolDuePerGrossTonnage;
  private BigDecimal odessosFixedMarpolDue;
  private BigDecimal odessosFreeSewageDisposalQuantity;
  private BigDecimal odessosFreeGarbageDisposalQuantity;
}
