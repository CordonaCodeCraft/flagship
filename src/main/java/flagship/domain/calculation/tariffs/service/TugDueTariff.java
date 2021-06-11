package flagship.domain.calculation.tariffs.service;

import flagship.domain.base.due.tuple.Due;
import flagship.domain.base.range.tuple.Range;
import flagship.domain.calculation.tariffs.Tariff;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

import static flagship.domain.port.entity.Port.PortName;
import static flagship.domain.warning.generator.WarningsGenerator.WarningType;

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

  public enum TugServiceProvider {
    VTC,
    PORTFLEET,
  }
}
