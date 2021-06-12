package flagship.domain.calculation.tariffs.agency;

import flagship.domain.calculation.tariffs.Tariff;
import flagship.domain.tuples.due.Due;
import flagship.domain.tuples.range.Range;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Map;

import static flagship.domain.port.entity.Port.PortName;

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
