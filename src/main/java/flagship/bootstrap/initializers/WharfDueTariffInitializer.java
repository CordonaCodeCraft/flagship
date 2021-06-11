package flagship.bootstrap.initializers;

import flagship.domain.base.due.tuple.Due;
import flagship.domain.calculation.tariffs.state.WharfDueTariff;
import flagship.domain.caze.entity.Case;
import flagship.domain.ship.entity.Ship;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

import static flagship.domain.caze.entity.Case.CallPurpose.*;
import static flagship.domain.ship.entity.Ship.ShipType.NAVY;

@Component
@Slf4j
@RequiredArgsConstructor
public class WharfDueTariffInitializer {

  public static WharfDueTariff getTariff() {

    final WharfDueTariff wharfDueTariff = new WharfDueTariff();

    final Map<Ship.ShipType, Due> wharfDuesByShipType = new EnumMap<>(Ship.ShipType.class);
    wharfDuesByShipType.put(NAVY, new Due(0.5));
    wharfDueTariff.setWharfDuesByShipType(Collections.unmodifiableMap(wharfDuesByShipType));

    final Map<Case.CallPurpose, BigDecimal> discountCoefficientsByCallPurpose =
        new EnumMap<>(Case.CallPurpose.class);
    discountCoefficientsByCallPurpose.put(RESUPPLY, BigDecimal.valueOf(0.5));
    discountCoefficientsByCallPurpose.put(RECRUITMENT, BigDecimal.valueOf(0.5));
    discountCoefficientsByCallPurpose.put(POSTAL, BigDecimal.valueOf(0.5));
    discountCoefficientsByCallPurpose.put(REPAIR, BigDecimal.valueOf(0.5));
    wharfDueTariff.setDiscountCoefficientsByCallPurpose(
        Collections.unmodifiableMap(discountCoefficientsByCallPurpose));

    final Set<Ship.ShipType> shipTypesNotEligibleForDiscount = EnumSet.of(NAVY);
    wharfDueTariff.setShipTypesNotEligibleForDiscount(
        Collections.unmodifiableSet(shipTypesNotEligibleForDiscount));

    wharfDueTariff.setDefaultWharfDue(new Due(0.10));

    log.info("Wharf due tariff initialized");

    return wharfDueTariff;
  }
}
