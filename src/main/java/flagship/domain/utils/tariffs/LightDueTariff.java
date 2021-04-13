package flagship.domain.utils.tariffs;

import flagship.domain.cases.entities.enums.ShipType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.javatuples.Pair;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;

@Getter
@Setter
@Component
@NoArgsConstructor
public class LightDueTariff extends Tariff {

  private Map<Pair<Integer, Integer>, BigDecimal> lightDuesByGrossTonnage;
  private Map<ShipType, BigDecimal> lightDuesPerTonByShipType;
  private Map<ShipType, BigDecimal> discountCoefficientsByShipType;
  private BigDecimal lightDueMaximumValue;
  private Integer callCountThreshold;
  private BigDecimal callCountDiscountCoefficient;
}
