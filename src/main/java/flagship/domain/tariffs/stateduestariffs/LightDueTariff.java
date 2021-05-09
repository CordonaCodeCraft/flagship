package flagship.domain.tariffs.stateduestariffs;

import flagship.domain.cases.entities.enums.ShipType;
import flagship.domain.tariffs.Due;
import flagship.domain.tariffs.Range;
import flagship.domain.tariffs.Tariff;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
@Component
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
