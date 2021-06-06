package flagship.domain.calculation.tariffs.service;

import flagship.domain.port.entity.Port;

public class MooringServiceProviderResolver {

  public static MooringDueTariff.MooringServiceProvider resolveMooringServiceProvider(
      String portName) {
    if (portName.equals(Port.PortName.LESPORT.name)) {
      return MooringDueTariff.MooringServiceProvider.LESPORT;
    } else if (portName.equals(Port.PortName.ODESSOS_PBM.name)) {
      return MooringDueTariff.MooringServiceProvider.ODESSOS;
    } else if (portName.equals(Port.PortName.BALCHIK_PORT.name)) {
      return MooringDueTariff.MooringServiceProvider.BALCHIK;
    } else {
      return MooringDueTariff.MooringServiceProvider.VTC;
    }
  }
}
