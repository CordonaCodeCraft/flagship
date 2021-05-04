package flagship.domain.tariffs.serviceduestariffs;

import flagship.domain.tariffs.PdaWarningsGenerator;
import flagship.domain.tariffs.Tariff;
import flagship.domain.tariffs.PortName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

import static flagship.domain.tariffs.PdaWarningsGenerator.*;

@Getter
@Setter
@Component
@NoArgsConstructor
public class PilotageDueTariff extends Tariff {

  private Map<PilotageArea, Set<PortName>> portNamesInPilotageAreas;
  private Map<PilotageArea, Map<BigDecimal, Integer[]>> pilotageDuesByArea;
  private Map<PdaWarning, BigDecimal> increaseCoefficientsByPdaWarning;
  private Set<LocalDate> holidayCalendar;
  private BigDecimal grossTonnageThreshold;

  public enum PilotageArea {
    VARNA_FIRST,
    VARNA_SECOND,
    VARNA_THIRD,
    BOURGAS_FIRST,
  }
}
