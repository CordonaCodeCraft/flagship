package flagship.domain.cases.dto;

import flagship.domain.cases.entities.ProformaDisbursementAccount;
import flagship.domain.cases.entities.enums.CallPurpose;
import lombok.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Set;

import static flagship.domain.PdaWarningsGenerator.PdaWarning;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Component
public class PdaCase {

  private PdaShip ship;
  private PdaPort port;
  private Set<PdaWarning> warnings;
  private CallPurpose callPurpose;
  private Integer callCount;
  private Integer alongsideDaysExpected;
  private LocalDate estimatedDateOfArrival;
  private LocalDate estimatedDateOfDeparture;
  private Boolean arrivesFromBulgarianPort;
  private ProformaDisbursementAccount proformaDisbursementAccount;
}
