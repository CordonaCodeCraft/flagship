package flagship.domain.calculators.tariffs.serviceduestariffs;

import flagship.domain.calculators.tariffs.Tariff;
import flagship.domain.calculators.tariffs.enums.PortName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
@Component
@NoArgsConstructor
public class MooringDueTariff extends Tariff {

  private Map<MooringServiceProvider, Map<BigDecimal, Integer[]>> mooringDuesByProvider;
  private Set<LocalDate> holidayCalendar;
  private BigDecimal lesportGrossTonnageThreshold;
  private BigDecimal odessosGrossTonnageThreshold;
  private BigDecimal balchikGrossTonnageThreshold;
  private BigDecimal vtcGrossTonnageThreshold;
  private BigDecimal portfleetGrossTonnageThreshold;

  public enum MooringServiceProvider {
    VTC,
    PORTFLEET,
    LESPORT,
    ODESSOS,
    BALCHIK
  }

}
