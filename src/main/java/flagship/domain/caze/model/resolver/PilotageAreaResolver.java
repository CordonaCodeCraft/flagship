package flagship.domain.caze.model.resolver;

import flagship.domain.calculation.tariffs.service.PilotageDueTariff;
import flagship.domain.caze.model.PdaCase;

import java.util.Map;

import static flagship.domain.calculation.tariffs.service.PilotageDueTariff.PilotageArea;

public class PilotageAreaResolver {

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
