package flagship.domain.resolvers;

import flagship.domain.cases.dto.PdaCase;
import flagship.domain.tariffs.TugDueTariff;

import java.util.Map;

import static flagship.domain.tariffs.TugDueTariff.TugArea;

public abstract class TugAreaResolver {

  public static TugArea resolveTugArea(final PdaCase source, final TugDueTariff tariff) {
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
