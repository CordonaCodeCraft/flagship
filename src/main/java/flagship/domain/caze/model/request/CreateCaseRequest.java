package flagship.domain.caze.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

import static flagship.domain.caze.model.request.resolvers.TugAreaResolver.TugServiceProvider;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCaseRequest {

  private String shipName;
  private String shipType;
  private BigDecimal shipLengthOverall;
  private BigDecimal shipGrossTonnage;
  private Boolean shipHasIncreasedManeuverability;
  private String portName;
  private List<String> warningTypes;
  private List<String> cargoManifest;
  private String callPurpose;
  private Integer callCount;
  private Integer alongsideDaysExpected;
  private String estimatedDateOfArrival;
  private String estimatedDateOfDeparture;
  private Boolean arrivesFromBulgarianPort;
  private BigDecimal clientDiscountCoefficient;
  private BigDecimal agencyCommissionCoefficient;

  private TugServiceProvider tugServiceProvider;
}
