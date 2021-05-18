package flagship.domain.cases.dto;

import flagship.domain.tariffs.PortArea;
import flagship.domain.tariffs.servicedues.PilotageDueTariff;
import lombok.*;

import static flagship.domain.tariffs.servicedues.MooringDueTariff.MooringServiceProvider;
import static flagship.domain.tariffs.servicedues.TugDueTariff.TugArea;
import static flagship.domain.tariffs.servicedues.TugDueTariff.TugServiceProvider;

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
