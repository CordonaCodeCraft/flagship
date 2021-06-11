package flagship.domain.calculation.tariffs.state;

import flagship.domain.base.due.tuple.Due;
import flagship.domain.base.range.tuple.Range;
import flagship.domain.calculation.tariffs.Tariff;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

import static flagship.domain.ship.entity.Ship.ShipType;

@Getter
@Setter
@NoArgsConstructor
public class LightDueTariff extends Tariff {

  private Map<Range, Due> lightDuesByGrossTonnage;
  private Map<ShipType, Due> lightDuesPerTonByShipType;
  private Map<ShipType, BigDecimal> discountCoefficientsByShipType;
  private Set<ShipType> shipTypesNotEligibleForDiscount;
  private BigDecimal lightDueMaximumValue;
  private Integer callCountThreshold;
  private BigDecimal callCountDiscountCoefficient;
}
