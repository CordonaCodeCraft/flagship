package flagship.domain.resolvers;

import flagship.domain.tariffs.serviceduestariffs.PilotageDueTariff;
import flagship.domain.tariffs.serviceduestariffs.PilotageDueTariff.PilotageArea;
import flagship.domain.cases.dto.PdaCase;

import java.util.Map;

public abstract class PilotageAreaResolver {

  public static PilotageArea resolvePilotageArea(
      final PdaCase source, final PilotageDueTariff tariff) {
    return tariff.getPortNamesInPilotageAreas().entrySet().stream()
        .filter(
            entry ->
                entry.getValue().stream().anyMatch(v -> v.name.equals(source.getPort().getName())))
        .map(Map.Entry::getKey)
        .findFirst()
        .get();
  }
}
