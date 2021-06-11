package flagship.bootstrap.initializers;

import flagship.domain.base.due.tuple.Due;
import flagship.domain.base.range.tuple.Range;
import flagship.domain.calculation.tariffs.state.LightDueTariff;
import flagship.domain.ship.entity.Ship;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

import static flagship.domain.calculation.tariffs.Tariff.MAX_GT;
import static flagship.domain.ship.entity.Ship.ShipType.NAVY;
import static flagship.domain.ship.entity.Ship.ShipType.PASSENGER;

@Component
@Slf4j
@RequiredArgsConstructor
public class LightDueTariffInitializer {

  public static LightDueTariff getTariff() {

    final LightDueTariff lightDueTariff = new LightDueTariff();

    final Map<Range, Due> lightDuesByGrossTonnage = new LinkedHashMap<>();

    lightDuesByGrossTonnage.put(new Range(41, 500), new Due(15.00));
    lightDuesByGrossTonnage.put(new Range(501, 1000), new Due(40.00));
    lightDuesByGrossTonnage.put(new Range(1001, 5000), new Due(70.00));
    lightDuesByGrossTonnage.put(new Range(5001, 10000), new Due(110.00));
    lightDuesByGrossTonnage.put(new Range(10001, MAX_GT), new Due(150.00));

    final Map<Ship.ShipType, Due> lightDuesByShipType = new EnumMap<>(Ship.ShipType.class);

    lightDuesByShipType.put(NAVY, new Due(0.15));

    final Map<Ship.ShipType, BigDecimal> discountCoefficientsByShipType =
        new EnumMap<>(Ship.ShipType.class);

    discountCoefficientsByShipType.put(PASSENGER, BigDecimal.valueOf(0.5));

    final Set<Ship.ShipType> shipTypesNotEligibleForDiscount = EnumSet.of(NAVY);

    lightDueTariff.setShipTypesNotEligibleForDiscount(
        Collections.unmodifiableSet(shipTypesNotEligibleForDiscount));

    lightDueTariff.setLightDuesByGrossTonnage(Collections.unmodifiableMap(lightDuesByGrossTonnage));
    lightDueTariff.setLightDuesPerTonByShipType(Collections.unmodifiableMap(lightDuesByShipType));
    lightDueTariff.setDiscountCoefficientsByShipType(
        Collections.unmodifiableMap(discountCoefficientsByShipType));
    lightDueTariff.setCallCountThreshold(4);
    lightDueTariff.setCallCountDiscountCoefficient(BigDecimal.valueOf(0.7));
    lightDueTariff.setLightDueMaximumValue(BigDecimal.valueOf(150.00));

    log.info("Light due tariff initialized");

    return lightDueTariff;
  }
}
