package flagship.domain.calculation.tariffs.service;

import flagship.domain.pda.model.PdaCase;

import java.util.Map;

public class PilotageAreaResolver {

  public static PilotageDueTariff.PilotageArea resolvePilotageArea(
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
