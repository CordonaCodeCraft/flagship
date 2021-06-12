package flagship.bootstrap.initializers;

import flagship.domain.calculation.tariffs.state.WharfDueTariff;
import flagship.domain.tuples.due.Due;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

import static flagship.domain.caze.entity.Case.CallPurpose;
import static flagship.domain.caze.entity.Case.CallPurpose.*;
import static flagship.domain.ship.entity.Ship.ShipType;
import static flagship.domain.ship.entity.Ship.ShipType.NAVY;

@Slf4j
public class WharfDueTariffInitializer extends Initializer {

  public static WharfDueTariff getTariff() {

    final Map<ShipType, Due> wharfDuesByShipType = new EnumMap<>(ShipType.class);

    wharfDuesByShipType.put(NAVY, new Due(0.5));

    final Map<CallPurpose, BigDecimal> discountCoefficientsByCallPurpose =
        new EnumMap<>(CallPurpose.class);

    discountCoefficientsByCallPurpose.put(RESUPPLY, BigDecimal.valueOf(0.5));
    discountCoefficientsByCallPurpose.put(RECRUITMENT, BigDecimal.valueOf(0.5));
    discountCoefficientsByCallPurpose.put(POSTAL, BigDecimal.valueOf(0.5));
    discountCoefficientsByCallPurpose.put(REPAIR, BigDecimal.valueOf(0.5));

    final Set<ShipType> shipTypesNotEligibleForDiscount = EnumSet.of(NAVY);

    final WharfDueTariff wharfDueTariff = new WharfDueTariff();

    wharfDueTariff.setWharfDuesByShipType(withImmutableMap(wharfDuesByShipType));

    wharfDueTariff.setDiscountCoefficientsByCallPurpose(
        withImmutableMap(discountCoefficientsByCallPurpose));

    wharfDueTariff.setShipTypesNotEligibleForDiscount(
        withImmutableSet(shipTypesNotEligibleForDiscount));

    wharfDueTariff.setDefaultWharfDue(new Due(0.10));

    log.info("Wharf due tariff initialized");

    return wharfDueTariff;
  }
}
