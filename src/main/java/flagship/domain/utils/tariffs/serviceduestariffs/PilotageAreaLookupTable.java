package flagship.domain.utils.tariffs.serviceduestariffs;
import flagship.domain.cases.entities.enums.PilotageArea;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@Component
public class PilotageAreaLookupTable {
    private Map<PilotageArea, List<String>> portNamesInPilotageAreas;
}
