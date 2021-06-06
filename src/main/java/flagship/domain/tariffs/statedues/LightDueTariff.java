package flagship.domain.tariffs.statedues;

import flagship.domain.cases.entities.Ship;
import flagship.domain.tariffs.Tariff;
import flagship.domain.tariffs.mix.Due;
import flagship.domain.tariffs.mix.Range;
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
