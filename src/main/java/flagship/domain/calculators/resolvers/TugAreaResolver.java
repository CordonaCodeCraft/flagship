package flagship.domain.calculators.resolvers;

import flagship.domain.calculators.tariffs.serviceduestariffs.TugDueTariff;
import flagship.domain.cases.dto.PdaCase;

import java.util.Map;

import static flagship.domain.calculators.tariffs.serviceduestariffs.TugDueTariff.TugArea;

public class TugAreaResolver {

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
