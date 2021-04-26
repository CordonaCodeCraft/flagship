package flagship.domain.cases.dto;

import flagship.domain.cases.entities.enums.PilotageArea;
import flagship.domain.cases.entities.enums.PortArea;
import flagship.domain.cases.entities.enums.TugArea;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PdaPort {

  private String name;
  private PortArea area;
  private PilotageArea pilotageArea;
  private TugArea tugArea;
}
