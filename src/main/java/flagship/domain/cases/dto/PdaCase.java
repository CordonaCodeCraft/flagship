package flagship.domain.cases.dto;

import flagship.domain.cases.entities.enums.CallPurpose;
import flagship.domain.cases.entities.enums.CargoType;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PdaCase {

  private PdaShip ship;
  private PdaPort port;
  private CargoType cargoType;
  private CallPurpose callPurpose;
  private Integer callCount;
  private Integer alongsideDaysExpected;
  private LocalDate estimatedDateOfArrival;
  private LocalDate estimatedDateOfDeparture;
}
