package flagship.domain.calculation.tariffs.agency;

import flagship.domain.calculation.tariffs.Tariff;
import flagship.domain.base.due.tuple.Due;
import flagship.domain.base.range.tuple.Range;
import flagship.domain.port.entity.Port;
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
  private Map<Port.PortName, BigDecimal> carsDuesIncreaseCoefficientByPortName;
  private BigDecimal clearanceIn;
  private BigDecimal clearanceOut;
  private BigDecimal baseCommunicationsDue;
  private BigDecimal communicationsDueGrossTonnageThreshold;
  private BigDecimal communicationsAdditionalDue;
  private BigDecimal bankExpensesCoefficient;
  private BigDecimal overtimeCoefficient;
}
