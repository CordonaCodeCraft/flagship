package flagship.domain.tariffs.servicedues;

import flagship.domain.tariffs.Tariff;
import flagship.domain.tariffs.mix.Due;
import flagship.domain.tariffs.mix.PortName;
import flagship.domain.tariffs.mix.Range;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

import static flagship.domain.PdaWarningsGenerator.WarningType;

@Getter
@Setter
@NoArgsConstructor
public class TugDueTariff extends Tariff {

  private Map<TugServiceProvider, Map<TugArea, Set<PortName>>> portNamesInTugAreas;
  private Map<TugArea, Map<Range, Due>> tugDuesByArea;
  private Map<Range, BigDecimal> tugCountByGrossTonnage;
  private Map<WarningType, BigDecimal> increaseCoefficientsByWarningType;
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
