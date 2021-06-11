package flagship.domain.calculation.tariffs.service;

import flagship.domain.base.due.tuple.Due;
import flagship.domain.base.range.tuple.Range;
import flagship.domain.calculation.tariffs.Tariff;
import flagship.domain.caze.model.createrequest.resolvers.MooringServiceProviderResolver;
import flagship.domain.warning.generator.WarningsGenerator.WarningType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class MooringDueTariff extends Tariff {

  private Map<MooringServiceProviderResolver.MooringServiceProvider, Map<Range, Due>>
      mooringDuesByProvider;
  private Map<WarningType, BigDecimal> increaseCoefficientsByWarningType;
  private Set<LocalDate> holidayCalendar;
  private BigDecimal lesportGrossTonnageThreshold;
  private BigDecimal odessosGrossTonnageThreshold;
  private BigDecimal balchikGrossTonnageThreshold;
  private BigDecimal pchvmGrossTonnageThreshold;
  private BigDecimal vtcGrossTonnageThreshold;
  private BigDecimal portfleetGrossTonnageThreshold;
}
