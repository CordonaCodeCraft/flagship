package flagship.domain.caze.model.resolver;

import flagship.domain.calculation.tariffs.service.TugDueTariff;
import flagship.domain.caze.model.PdaCase;

import java.util.Map;

public class TugAreaResolver {

  public static TugDueTariff.TugArea resolveTugArea(
      final PdaCase source, final TugDueTariff tariff) {
    return tariff
        .getPortNamesInTugAreas()
        .get(source.getPort().getTugServiceProvider())
        .entrySet()
        .stream()
        .filter(
            entry ->
                entry.getValue().stream().anyMatch(v -> v.name.equals(source.getPort().getName())))
        .map(Map.Entry::getKey)
        .findFirst()
        .get();
  }
}
