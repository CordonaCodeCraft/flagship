package flagship.domain.tariffs.stateduestariffs;

import flagship.domain.tariffs.Range;
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
public class MarpolDueTariff extends Tariff {

  private Map<Range, BigDecimal> freeSewageDisposalQuantitiesPerGrossTonnage;
  private Map<Range, BigDecimal> freeGarbageDisposalQuantitiesPerGrossTonnage;
  private Map<Range, BigDecimal[]> marpolDuePerGrossTonnage;
  private BigDecimal maximumFreeSewageDisposalQuantity;
  private BigDecimal maximumFreeGarbageDisposalQuantity;
  private BigDecimal[] maximumMarpolDueValues;
  private BigDecimal odessosFixedMarpolDue;
  private BigDecimal odessosFreeSewageDisposalQuantity;
  private BigDecimal odessosFreeGarbageDisposalQuantity;
}
