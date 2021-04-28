package flagship.domain.cases.dto;

import flagship.domain.calculators.tariffs.enums.PilotageArea;
import flagship.domain.calculators.tariffs.enums.PortArea;
import flagship.domain.calculators.tariffs.enums.TugArea;
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
