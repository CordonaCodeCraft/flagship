package flagship.domain.caze.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class CreateCaseRequest {

  private String portName;
  private String shipName;
  private String shipType;
  private BigDecimal shipLengthOverall;
  private BigDecimal shipGrossTonnage;
  private Boolean shipHasIncreasedManeuverability;
  private List<String> warningTypes;
  private List<String> cargoManifest;
  private String callPurpose;
  private Integer callCount;
  private LocalDate estimatedDateOfArrival;
  private LocalDate estimatedDateOfDeparture;
  private Integer alongsideDaysExpected;
  private BigDecimal clientDiscountCoefficient;
}
