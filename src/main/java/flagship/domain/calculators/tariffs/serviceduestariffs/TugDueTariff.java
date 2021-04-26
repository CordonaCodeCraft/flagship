package flagship.domain.calculators.tariffs.serviceduestariffs;

import flagship.domain.cases.entities.enums.TugArea;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Component
@NoArgsConstructor
public class TugDueTariff {

  private Map<TugArea, List<String>> portNamesInTugAreas;
  private Map<Integer, Integer[]> tugDuesByArea;
}
