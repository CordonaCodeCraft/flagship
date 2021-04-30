package flagship.domain.cases.dto;

import flagship.domain.calculators.tariffs.enums.PortArea;
import flagship.domain.calculators.tariffs.serviceduestariffs.PilotageDueTariff;
import lombok.*;

import static flagship.domain.calculators.tariffs.serviceduestariffs.TugDueTariff.TugArea;
import static flagship.domain.calculators.tariffs.serviceduestariffs.TugDueTariff.TugProvider;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PdaPort {

  private String name;
  private PortArea area;
  private PilotageDueTariff.PilotageArea pilotageArea;
  private TugArea tugArea;
  private TugProvider tugServiceProvider;
}
