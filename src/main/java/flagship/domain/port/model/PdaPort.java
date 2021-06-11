package flagship.domain.port.model;

import lombok.*;

import static flagship.domain.calculation.tariffs.service.MooringDueTariff.MooringServiceProvider;
import static flagship.domain.calculation.tariffs.service.PilotageDueTariff.PilotageArea;
import static flagship.domain.calculation.tariffs.service.TugDueTariff.TugArea;
import static flagship.domain.calculation.tariffs.service.TugDueTariff.TugServiceProvider;
import static flagship.domain.port.entity.Port.PortArea;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PdaPort {

  private String name;
  private PortArea portArea;
  private PilotageArea pilotageArea;
  private TugArea tugArea;
  private TugServiceProvider tugServiceProvider;
  private MooringServiceProvider mooringServiceProvider;
}
