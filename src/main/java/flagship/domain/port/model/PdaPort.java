package flagship.domain.port.model;

import flagship.domain.calculation.tariffs.service.PilotageDueTariff;
import flagship.domain.port.entity.Port;
import lombok.*;

import static flagship.domain.calculation.tariffs.service.MooringDueTariff.MooringServiceProvider;
import static flagship.domain.calculation.tariffs.service.TugDueTariff.TugArea;
import static flagship.domain.calculation.tariffs.service.TugDueTariff.TugServiceProvider;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PdaPort {

  private String name;
  private Port.PortArea portArea;
  private PilotageDueTariff.PilotageArea pilotageArea;
  private TugArea tugArea;
  private TugServiceProvider tugServiceProvider;
  private MooringServiceProvider mooringServiceProvider;
}
