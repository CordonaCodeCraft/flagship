package flagship.domain.calculators.tariffs.serviceduestariffs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
@Component
@NoArgsConstructor
public class MooringDueTariff {

  private List<String> privatePorts;
}
