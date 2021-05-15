package flagship.domain.tariffs;

import flagship.domain.tariffs.mix.Due;
import flagship.domain.tariffs.mix.PortName;
import flagship.domain.tariffs.mix.Range;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

import static flagship.domain.PdaWarningsGenerator.WarningType;

@Getter
@Setter
@NoArgsConstructor
public class PilotageDueTariff extends Tariff {

  private Map<PilotageArea, Set<PortName>> portNamesInPilotageAreas;
  private Map<PilotageArea, Map<Range, Due>> pilotageDuesByArea;
  private Map<WarningType, BigDecimal> increaseCoefficientsByWarningType;
  private Set<LocalDate> holidayCalendar;
  private BigDecimal grossTonnageThreshold;

  public enum PilotageArea {
    VARNA_FIRST,
    VARNA_SECOND,
    VARNA_THIRD,
    BOURGAS_FIRST,
  }
}
