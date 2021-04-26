package flagship.domain.calculators.resolvers;

import flagship.domain.calculators.tariffs.serviceduestariffs.PilotageDueTariff;
import flagship.domain.cases.dto.PdaCase;
import flagship.domain.cases.entities.enums.PilotageArea;

import java.util.Map;

public class PilotageAreaResolver {

  public static PilotageArea resolvePilotageArea(
      final PdaCase source, final PilotageDueTariff tariff) {

    final String portName = source.getPort().getName();

    return tariff.getPortNamesInPilotageAreas().entrySet().stream()
        .filter(e -> e.getValue().contains(portName))
        .map(Map.Entry::getKey)
        .findFirst()
        .get();
  }
}
