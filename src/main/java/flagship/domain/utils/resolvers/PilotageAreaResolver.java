package flagship.domain.utils.resolvers;

import flagship.domain.cases.entities.Case;
import flagship.domain.cases.entities.enums.PilotageArea;
import flagship.domain.utils.tariffs.serviceduestariffs.PilotageAreaLookupTable;

import java.util.List;
import java.util.Map;

public class PilotageAreaResolver {

    public static PilotageArea resolveArea(Case testCase, PilotageAreaLookupTable lookupTable) {

        String portName = testCase.getPort().getName();

        return lookupTable.getPortNamesInPilotageAreas()
                .entrySet()
                .stream()
                .filter(entry -> {
                    List<String> portNames = entry.getValue();
                    return portNames.contains(portName);
                })
                .map(Map.Entry::getKey)
                .findFirst().get();
    }
}
