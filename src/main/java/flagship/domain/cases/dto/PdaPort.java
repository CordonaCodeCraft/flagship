package flagship.domain.cases.dto;

import flagship.domain.tariffs.stateduestariffs.PortArea;
import flagship.domain.tariffs.serviceduestariffs.PilotageDueTariff;
import lombok.*;

import static flagship.domain.tariffs.serviceduestariffs.MooringDueTariff.*;
import static flagship.domain.tariffs.serviceduestariffs.TugDueTariff.TugArea;
import static flagship.domain.tariffs.serviceduestariffs.TugDueTariff.TugServiceProvider;

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
