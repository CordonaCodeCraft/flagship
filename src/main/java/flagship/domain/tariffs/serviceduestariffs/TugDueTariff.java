package flagship.domain.tariffs.serviceduestariffs;

import flagship.domain.tariffs.Due;
import flagship.domain.tariffs.PortName;
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

import static flagship.domain.tariffs.PdaWarningsGenerator.PdaWarning;

@Getter
@Setter
@Component
@NoArgsConstructor
public class TugDueTariff extends Tariff {

  private Map<TugServiceProvider, Map<TugArea, Set<PortName>>> portNamesInTugAreas;
  private Map<TugArea, Map<Range, Due>> tugDuesByArea;
  private Map<Range, BigDecimal> tugCountByGrossTonnage;
  private Map<PdaWarning, BigDecimal> increaseCoefficientsByWarningType;
  private Set<LocalDate> holidayCalendar;
  private BigDecimal grossTonnageThreshold;
  private BigDecimal grossTonnageThresholdForTugCountReduce;

  public enum TugServiceProvider {
    VTC,
    PORTFLEET,
  }

  public enum TugArea {
    VTC_FIRST,
    VTC_SECOND,
    VTC_THIRD,
    VTC_FOURTH,
    VTC_FIFTH,
    PORTFLEET_FIRST,
    PORTFLEET_SECOND,
    PORTFLEET_THIRD,
    PORTFLEET_FOURTH,
    PORTFLEET_FIFTH,
  }
}
