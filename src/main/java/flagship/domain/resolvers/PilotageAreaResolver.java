package flagship.domain.resolvers;

import flagship.domain.cases.dto.PdaCase;
import flagship.domain.tariffs.PilotageDueTariff;
import flagship.domain.tariffs.PilotageDueTariff.PilotageArea;

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
