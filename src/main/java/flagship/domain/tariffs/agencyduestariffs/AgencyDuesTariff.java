package flagship.domain.tariffs.agencyduestariffs;

import flagship.domain.tariffs.Due;
import flagship.domain.tariffs.PortName;
import flagship.domain.tariffs.Range;
import flagship.domain.tariffs.Tariff;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@Component
public class AgencyDuesTariff extends Tariff {

  private Map<Range, Due> basicAgencyDuePerGrossTonnage;
  private Map<Range, Map<Range, BigDecimal>> carsDueByGrossTonnageAndAlongsideDaysExpected;
  private Map<PortName, BigDecimal> carsDuesIncreaseCoefficientByPortName;
  BigDecimal basicAgencyDueDiscountCoefficientByCallPurpose;
  BigDecimal basicAgencyDueGrossTonnageThreshold;
  private BigDecimal clearanceIn;
  private BigDecimal clearanceOut;
  private BigDecimal baseCommunicationsDue;
  private BigDecimal communicationsDueGrossTonnageThreshold;
  private BigDecimal communicationsAdditionalDue;
}
