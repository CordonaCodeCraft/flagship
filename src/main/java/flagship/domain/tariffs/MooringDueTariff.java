package flagship.domain.tariffs;

import flagship.domain.PdaWarningsGenerator.WarningType;
import flagship.domain.tariffs.mix.Due;
import flagship.domain.tariffs.mix.Range;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
@Component
@NoArgsConstructor
public class MooringDueTariff extends Tariff {

  private Map<MooringServiceProvider, Map<Range, Due>> mooringDuesByProvider;
  private Map<WarningType, BigDecimal> increaseCoefficientsByWarningType;
  private Set<LocalDate> holidayCalendar;
  private BigDecimal lesportGrossTonnageThreshold;
  private BigDecimal odessosGrossTonnageThreshold;
  private BigDecimal balchikGrossTonnageThreshold;
  private BigDecimal pchvmGrossTonnageThreshold;
  private BigDecimal vtcGrossTonnageThreshold;
  private BigDecimal portfleetGrossTonnageThreshold;

  public enum MooringServiceProvider {
    VTC,
    PORTFLEET,
    LESPORT,
    ODESSOS,
    BALCHIK,
    PCHMV,
  }
}
