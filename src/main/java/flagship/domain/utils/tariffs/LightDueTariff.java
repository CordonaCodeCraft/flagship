package flagship.domain.utils.tariffs;

import flagship.domain.cases.entities.enums.ShipType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.javatuples.Pair;
import org.springframework.stereotype.Component;

import java.util.Map;

@Getter
@Setter
@Component
@NoArgsConstructor
public class LightDueTariff extends Tariff {

  private Map<Pair<Integer, Integer>, Double> lightDuesByGrossTonnage;
  private Map<ShipType, Double> lightDuesPerTonByShipType;
  private Map<ShipType, Double> discountCoefficientsByShipType;
  private Integer lightDueMaximumValue;
  private Integer callCountThreshold;
  private Double callCountDiscountCoefficient;
}
