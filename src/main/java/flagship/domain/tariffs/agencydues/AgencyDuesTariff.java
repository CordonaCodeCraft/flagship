package flagship.domain.tariffs.agencydues;

import flagship.domain.tariffs.Tariff;
import flagship.domain.tariffs.mix.Due;
import flagship.domain.tariffs.mix.PortName;
import flagship.domain.tariffs.mix.Range;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class AgencyDuesTariff extends Tariff {

  BigDecimal basicAgencyDueDiscountCoefficientByCallPurpose;
  BigDecimal basicAgencyDueGrossTonnageThreshold;
  private Map<Range, Due> basicAgencyDuePerGrossTonnage;
  private Map<Range, Map<Range, Due>> carsDueByGrossTonnageAndAlongsideDaysExpected;
  private Map<PortName, BigDecimal> carsDuesIncreaseCoefficientByPortName;
  private BigDecimal clearanceIn;
  private BigDecimal clearanceOut;
  private BigDecimal baseCommunicationsDue;
  private BigDecimal communicationsDueGrossTonnageThreshold;
  private BigDecimal communicationsAdditionalDue;
  private BigDecimal bankExpensesCoefficient;
  private BigDecimal overtimeCoefficient;
}
