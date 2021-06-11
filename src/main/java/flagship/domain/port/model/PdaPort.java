package flagship.domain.port.model;

import lombok.*;

import static flagship.domain.caze.model.createrequest.resolvers.MooringServiceProviderResolver.MooringServiceProvider;
import static flagship.domain.caze.model.createrequest.resolvers.PilotageAreaResolver.PilotageArea;
import static flagship.domain.caze.model.createrequest.resolvers.PortAreaResolver.PortArea;
import static flagship.domain.caze.model.createrequest.resolvers.TugAreaResolver.TugArea;
import static flagship.domain.caze.model.createrequest.resolvers.TugAreaResolver.TugServiceProvider;

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
