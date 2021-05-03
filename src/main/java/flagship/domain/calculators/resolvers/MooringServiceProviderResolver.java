package flagship.domain.calculators.resolvers;

import flagship.domain.calculators.tariffs.enums.PortName;
import flagship.domain.calculators.tariffs.serviceduestariffs.MooringDueTariff.MooringServiceProvider;

import java.util.EnumSet;
import java.util.Set;

public class MooringServiceProviderResolver {

  private final Set<PortName> privatePorts =
      EnumSet.of(PortName.LESPORT, PortName.ODESSOS_PBM, PortName.BALCHIK_PORT);

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
