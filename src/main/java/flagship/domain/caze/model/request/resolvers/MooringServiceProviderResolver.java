package flagship.domain.caze.model.request.resolvers;

import flagship.domain.port.entity.Port;

public class MooringServiceProviderResolver {

  public static MooringServiceProvider resolveMooringServiceProvider(final String portName) {
    if (portName.equals(Port.PortName.LESPORT.name)) {
      return MooringServiceProvider.LESPORT;
    } else if (portName.equals(Port.PortName.ODESSOS_PBM.name)) {
      return MooringServiceProvider.ODESSOS;
    } else if (portName.equals(Port.PortName.BALCHIK_PORT.name)) {
      return MooringServiceProvider.BALCHIK;
    } else {
      return MooringServiceProvider.VTC;
    }
  }

  public enum MooringServiceProvider {
    VTC,
    PORTFLEET,
    LESPORT,
    ODESSOS,
    BALCHIK,
    PCHMV,
  }
}
