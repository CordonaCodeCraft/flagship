package flagship.domain.calculation.tariffs.service;

import flagship.domain.calculation.tariffs.Tariff;
import flagship.domain.base.due.tuple.Due;
import flagship.domain.base.range.tuple.Range;
import flagship.domain.pda.model.PdaCase;
import flagship.domain.port.entity.Port;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

import static flagship.domain.warning.generator.WarningsGenerator.WarningType;

@Getter
@Setter
@NoArgsConstructor
public class TugDueTariff extends Tariff {

  private Map<TugServiceProvider, Map<TugArea, Set<Port.PortName>>> portNamesInTugAreas;
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

  public static class TugAreaResolver {

    public static TugArea resolveTugArea(final PdaCase source, final TugDueTariff tariff) {
      return tariff
          .getPortNamesInTugAreas()
          .get(source.getPort().getTugServiceProvider())
          .entrySet()
          .stream()
          .filter(
              entry ->
                  entry.getValue().stream()
                      .anyMatch(v -> v.name.equals(source.getPort().getName())))
          .map(Map.Entry::getKey)
          .findFirst()
          .get();
    }
  }
}
