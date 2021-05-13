package flagship.domain.resolvers;

import flagship.domain.tariffs.MooringDueTariff.MooringServiceProvider;
import flagship.domain.tariffs.mix.PortName;

public abstract class MooringServiceProviderResolver {

  public static MooringServiceProvider resolveMooringServiceProvider(String portName) {
    if (portName.equals(PortName.LESPORT.name)) {
      return MooringServiceProvider.LESPORT;
    } else if (portName.equals(PortName.ODESSOS_PBM.name)) {
      return MooringServiceProvider.ODESSOS;
    } else if (portName.equals(PortName.BALCHIK_PORT.name)) {
      return MooringServiceProvider.BALCHIK;
    } else {
      return MooringServiceProvider.VTC;
    }
  }
}
