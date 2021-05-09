package flagship.domain.tariffs.serviceduestariffs;

import flagship.domain.tariffs.Due;
import flagship.domain.tariffs.PdaWarningsGenerator.PdaWarning;
import flagship.domain.tariffs.Range;
import flagship.domain.tariffs.Tariff;
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
  private Map<PdaWarning, BigDecimal> increaseCoefficientsByWarningType;
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
