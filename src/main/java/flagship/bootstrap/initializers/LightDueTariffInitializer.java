package flagship.bootstrap.initializers;

import flagship.domain.calculation.tariffs.state.LightDueTariff;
import flagship.domain.tuples.due.Due;
import flagship.domain.tuples.range.Range;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.*;

import static flagship.domain.calculation.tariffs.Tariff.MAX_GT;
import static flagship.domain.ship.entity.Ship.ShipType;
import static flagship.domain.ship.entity.Ship.ShipType.NAVY;
import static flagship.domain.ship.entity.Ship.ShipType.PASSENGER;

@Slf4j
public class LightDueTariffInitializer extends Initializer {

  public static LightDueTariff getTariff() {

    final Map<Range, Due> lightDuesByGrossTonnage = new LinkedHashMap<>();

    lightDuesByGrossTonnage.put(new Range(41, 500), new Due(15.00));
    lightDuesByGrossTonnage.put(new Range(501, 1000), new Due(40.00));
    lightDuesByGrossTonnage.put(new Range(1001, 5000), new Due(70.00));
    lightDuesByGrossTonnage.put(new Range(5001, 10000), new Due(110.00));
    lightDuesByGrossTonnage.put(new Range(10001, MAX_GT), new Due(150.00));

    final Map<ShipType, Due> lightDuesByShipType = new EnumMap<>(ShipType.class);

    lightDuesByShipType.put(NAVY, new Due(0.15));

    final Map<ShipType, BigDecimal> discountCoefficientsByShipType = new EnumMap<>(ShipType.class);

    discountCoefficientsByShipType.put(PASSENGER, BigDecimal.valueOf(0.5));

    final Set<ShipType> shipTypesNotEligibleForDiscount = EnumSet.of(NAVY);

    final LightDueTariff lightDueTariff = new LightDueTariff();

    lightDueTariff.setLightDuesByGrossTonnage(withImmutableMap(lightDuesByGrossTonnage));

    lightDueTariff.setLightDuesPerTonByShipType(withImmutableMap(lightDuesByShipType));

    lightDueTariff.setDiscountCoefficientsByShipType(
        withImmutableMap(discountCoefficientsByShipType));

    lightDueTariff.setShipTypesNotEligibleForDiscount(
        withImmutableSet(shipTypesNotEligibleForDiscount));

    lightDueTariff.setCallCountThreshold(4);

    lightDueTariff.setCallCountDiscountCoefficient(BigDecimal.valueOf(0.7));

    lightDueTariff.setLightDueMaximumValue(BigDecimal.valueOf(150.00));

    log.info("Light due tariff initialized");

    return lightDueTariff;
  }
}
