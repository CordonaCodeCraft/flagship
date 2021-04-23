package flagship.domain.utils.calculators.resolvers;

import flagship.domain.cases.entities.Case;
import flagship.domain.cases.entities.enums.PilotageArea;
import flagship.domain.utils.tariffs.serviceduestariffs.PilotageDueTariff;

import java.util.Map;

public class PilotageAreaResolver {

    public static PilotageArea resolveArea(final Case source, final PilotageDueTariff tariff) {

        final String portName = source.getPort().getName();

        return tariff.getPortNamesInPilotageAreas()
                .entrySet()
                .stream()
                .filter(e -> e.getValue().contains(portName))
                .map(Map.Entry::getKey)
                .findFirst()
                .get();
    }
}
