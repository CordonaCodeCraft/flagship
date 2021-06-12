package flagship.domain.calculation.tariffs.service;

import flagship.domain.calculation.tariffs.Tariff;
import flagship.domain.tuples.due.Due;
import flagship.domain.tuples.range.Range;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

import static flagship.domain.caze.model.request.resolvers.TugAreaResolver.TugArea;
import static flagship.domain.warning.generator.WarningsGenerator.WarningType;

@Getter
@Setter
@NoArgsConstructor
public class TugDueTariff extends Tariff {

  private Map<TugArea, Map<Range, Due>> tugDuesByArea;
  private Map<Range, BigDecimal> tugCountByGrossTonnage;
  private Map<WarningType, BigDecimal> increaseCoefficientsByWarningType;
  private Set<LocalDate> holidayCalendar;
  private BigDecimal grossTonnageThreshold;
  private BigDecimal grossTonnageThresholdForTugCountReduce;
}
