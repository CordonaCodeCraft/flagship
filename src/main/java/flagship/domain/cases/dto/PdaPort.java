package flagship.domain.cases.dto;

import flagship.domain.tariffs.PilotageDueTariff;
import flagship.domain.tariffs.PortArea;
import lombok.*;

import static flagship.domain.tariffs.MooringDueTariff.MooringServiceProvider;
import static flagship.domain.tariffs.TugDueTariff.TugArea;
import static flagship.domain.tariffs.TugDueTariff.TugServiceProvider;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PdaPort {

  private String name;
  private PortArea portArea;
  private PilotageDueTariff.PilotageArea pilotageArea;
  private TugArea tugArea;
  private TugServiceProvider tugServiceProvider;
  private MooringServiceProvider mooringServiceProvider;
}
