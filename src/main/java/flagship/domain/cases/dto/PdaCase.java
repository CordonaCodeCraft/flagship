package flagship.domain.cases.dto;

import flagship.domain.cases.entities.enums.CallPurpose;
import flagship.domain.cases.entities.enums.CargoType;
import flagship.domain.tariffs.PdaWarningsGenerator;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

import static flagship.domain.tariffs.PdaWarningsGenerator.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
}
