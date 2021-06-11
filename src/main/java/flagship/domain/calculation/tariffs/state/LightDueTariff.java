package flagship.domain.calculation.tariffs.state;

import flagship.domain.base.due.tuple.Due;
import flagship.domain.base.range.tuple.Range;
import flagship.domain.calculation.tariffs.Tariff;
import flagship.domain.ship.entity.Ship;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class LightDueTariff extends Tariff {

  private Map<Range, Due> lightDuesByGrossTonnage;
  private Map<Ship.ShipType, Due> lightDuesPerTonByShipType;
  private Map<Ship.ShipType, BigDecimal> discountCoefficientsByShipType;
  private Set<Ship.ShipType> shipTypesNotEligibleForDiscount;
  private BigDecimal lightDueMaximumValue;
  private Integer callCountThreshold;
  private BigDecimal callCountDiscountCoefficient;
}
