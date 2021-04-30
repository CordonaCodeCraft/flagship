package flagship.domain.calculators.tariffs.serviceduestariffs;

import flagship.domain.calculators.tariffs.enums.PdaWarning;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
@Component
@NoArgsConstructor
public class TugDueTariff {

  private Map<TugProvider, Map<TugArea, List<String>>> portNamesInTugAreas;
  private Map<TugArea, Map<BigDecimal, Integer[]>> tugDuesByArea;
  private Map<BigDecimal, Integer[]> tugCountByGrossTonnage;
  private Map<PdaWarning, BigDecimal> increaseCoefficientsByWarningType;
  private Set<LocalDate> holidayCalendar;
  private BigDecimal grossTonnageThreshold;
  private BigDecimal grossTonnageThresholdForTugCountReduce;
  private BigDecimal maximumTugCount;

  public enum TugProvider {
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
