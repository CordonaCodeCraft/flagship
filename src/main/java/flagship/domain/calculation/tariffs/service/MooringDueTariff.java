package flagship.domain.calculation.tariffs.service;

import flagship.domain.calculation.tariffs.Tariff;
import flagship.domain.tuples.due.Due;
import flagship.domain.tuples.range.Range;
import flagship.domain.warning.generator.WarningsGenerator.WarningType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

import static flagship.domain.caze.model.request.resolvers.MooringServiceProviderResolver.MooringServiceProvider;

@Getter
@Setter
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
}
