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

import static flagship.domain.caze.model.createrequest.resolvers.PilotageAreaResolver.PilotageArea;
import static flagship.domain.warning.generator.WarningsGenerator.WarningType;

@Getter
@Setter
@NoArgsConstructor
public class PilotageDueTariff extends Tariff {

  private Map<PilotageArea, Map<Range, Due>> pilotageDuesByArea;
  private Map<WarningType, BigDecimal> increaseCoefficientsByWarningType;
  private Set<LocalDate> holidayCalendar;
  private BigDecimal grossTonnageThreshold;
}
