package flagship.domain.cases.dto;

import flagship.domain.cases.entities.Case;
import flagship.domain.cases.entities.ProformaDisbursementAccount;
import flagship.domain.cases.entities.Warning;
import lombok.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static flagship.domain.PdaWarningsGenerator.WarningType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Component
public class PdaCase {

  private PdaShip ship;
  private PdaPort port;
  private Set<WarningType> warningTypes;
  private Set<Warning> warnings;
  private List<String> cargoManifest;
  private Case.CallPurpose callPurpose;
  private Integer callCount;
  private Integer alongsideDaysExpected;
  private LocalDate estimatedDateOfArrival;
  private LocalDate estimatedDateOfDeparture;
  private Boolean arrivesFromBulgarianPort;
  private BigDecimal clientDiscountCoefficient;
  private ProformaDisbursementAccount proformaDisbursementAccount;
}
