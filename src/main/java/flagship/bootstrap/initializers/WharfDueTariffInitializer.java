package flagship.bootstrap.initializers;

import flagship.domain.calculation.tariffs.state.WharfDueTariff;
import flagship.domain.tuples.due.Due;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

import static flagship.domain.caze.entity.Case.CallPurpose;
import static flagship.domain.caze.entity.Case.CallPurpose.*;
import static flagship.domain.ship.entity.Ship.ShipType;
import static flagship.domain.ship.entity.Ship.ShipType.NAVY;

@Component
@Slf4j
@RequiredArgsConstructor
public class WharfDueTariffInitializer {

  public static WharfDueTariff getTariff() {

    final WharfDueTariff wharfDueTariff = new WharfDueTariff();

    final Map<ShipType, Due> wharfDuesByShipType = new EnumMap<>(ShipType.class);
    wharfDuesByShipType.put(NAVY, new Due(0.5));
    wharfDueTariff.setWharfDuesByShipType(Collections.unmodifiableMap(wharfDuesByShipType));

    final Map<CallPurpose, BigDecimal> discountCoefficientsByCallPurpose =
        new EnumMap<>(CallPurpose.class);
    discountCoefficientsByCallPurpose.put(RESUPPLY, BigDecimal.valueOf(0.5));
    discountCoefficientsByCallPurpose.put(RECRUITMENT, BigDecimal.valueOf(0.5));
    discountCoefficientsByCallPurpose.put(POSTAL, BigDecimal.valueOf(0.5));
    discountCoefficientsByCallPurpose.put(REPAIR, BigDecimal.valueOf(0.5));
    wharfDueTariff.setDiscountCoefficientsByCallPurpose(
        Collections.unmodifiableMap(discountCoefficientsByCallPurpose));

    final Set<ShipType> shipTypesNotEligibleForDiscount = EnumSet.of(NAVY);
    wharfDueTariff.setShipTypesNotEligibleForDiscount(
        Collections.unmodifiableSet(shipTypesNotEligibleForDiscount));

    wharfDueTariff.setDefaultWharfDue(new Due(0.10));

    log.info("Wharf due tariff initialized");

    return wharfDueTariff;
  }
}
