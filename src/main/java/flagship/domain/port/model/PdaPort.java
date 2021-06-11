package flagship.domain.port.model;

import lombok.*;

import static flagship.domain.caze.model.request.resolvers.MooringServiceProviderResolver.MooringServiceProvider;
import static flagship.domain.caze.model.request.resolvers.PilotageAreaResolver.PilotageArea;
import static flagship.domain.caze.model.request.resolvers.PortAreaResolver.PortArea;
import static flagship.domain.caze.model.request.resolvers.TugAreaResolver.TugArea;
import static flagship.domain.caze.model.request.resolvers.TugAreaResolver.TugServiceProvider;

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
