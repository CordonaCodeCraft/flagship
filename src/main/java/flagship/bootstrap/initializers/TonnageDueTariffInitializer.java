package flagship.bootstrap.initializers;

import flagship.domain.calculation.tariffs.state.TonnageDueTariff;
import flagship.domain.tuples.due.Due;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.*;

import static flagship.domain.caze.entity.Case.CallPurpose;
import static flagship.domain.caze.entity.Case.CallPurpose.*;
import static flagship.domain.caze.model.createrequest.resolvers.PortAreaResolver.PortArea;
import static flagship.domain.caze.model.createrequest.resolvers.PortAreaResolver.PortArea.*;
import static flagship.domain.ship.entity.Ship.ShipType;
import static flagship.domain.ship.entity.Ship.ShipType.*;

@Slf4j
public class TonnageDueTariffInitializer extends Initializer {

  public static TonnageDueTariff getTariff() {

    final Map<PortArea, Due> tonnageDuesByPortArea = new EnumMap<>(PortArea.class);

    tonnageDuesByPortArea.put(FIRST, new Due(0.55));
    tonnageDuesByPortArea.put(SECOND, new Due(0.40));
    tonnageDuesByPortArea.put(THIRD, new Due(0.55));
    tonnageDuesByPortArea.put(FOURTH, new Due(0.55));

    final Map<ShipType, Due> tonnageDuesByShipType = new EnumMap<>(ShipType.class);

    tonnageDuesByShipType.put(OIL_TANKER, new Due(0.50));
    tonnageDuesByShipType.put(RECREATIONAL, new Due(0.10));
    tonnageDuesByShipType.put(NAVY, new Due(0.25));
    tonnageDuesByShipType.put(WORK_SHIP, new Due(0.50));

    final Map<CallPurpose, Due> tonnageDuesByCallPurpose = new EnumMap<>(CallPurpose.class);

    tonnageDuesByCallPurpose.put(SPECIAL_PURPOSE_PORT_VISIT, new Due(0.05));

    final Map<CallPurpose, BigDecimal> discountCoefficientsByCallPurpose =
        new EnumMap<>(CallPurpose.class);

    discountCoefficientsByCallPurpose.put(RESUPPLY, BigDecimal.valueOf(0.65));
    discountCoefficientsByCallPurpose.put(RECRUITMENT, BigDecimal.valueOf(0.65));
    discountCoefficientsByCallPurpose.put(POSTAL, BigDecimal.valueOf(0.65));
    discountCoefficientsByCallPurpose.put(REPAIR, BigDecimal.valueOf(0.65));

    final Map<ShipType, BigDecimal> discountCoefficientByShipType = new EnumMap<>(ShipType.class);

    discountCoefficientByShipType.put(REEFER, BigDecimal.valueOf(0.6));
    discountCoefficientByShipType.put(CONTAINER, BigDecimal.valueOf(0.6));
    discountCoefficientByShipType.put(PASSENGER, BigDecimal.valueOf(0.4));

    final Set<ShipType> shipTypesNotEligibleForDiscount = EnumSet.of(RECREATIONAL, NAVY, WORK_SHIP);

    final Set<CallPurpose> callPurposesNotEligibleForDiscount =
        EnumSet.of(SPECIAL_PURPOSE_PORT_VISIT);

    final TonnageDueTariff tonnageDueTariff = new TonnageDueTariff();

    tonnageDueTariff.setTonnageDuesByPortArea(withImmutableMap(tonnageDuesByPortArea));

    tonnageDueTariff.setTonnageDuesByShipType(withImmutableMap(tonnageDuesByShipType));

    tonnageDueTariff.setTonnageDuesByCallPurpose(withImmutableMap(tonnageDuesByCallPurpose));

    tonnageDueTariff.setDiscountCoefficientsByCallPurpose(
        withImmutableMap(discountCoefficientsByCallPurpose));

    tonnageDueTariff.setDiscountCoefficientsByShipType(
        withImmutableMap(discountCoefficientByShipType));

    tonnageDueTariff.setShipTypesNotEligibleForDiscount(
        withImmutableSet(shipTypesNotEligibleForDiscount));

    tonnageDueTariff.setCallPurposesNotEligibleForDiscount(
        Collections.unmodifiableSet(callPurposesNotEligibleForDiscount));

    tonnageDueTariff.setCallCountThreshold(4);

    tonnageDueTariff.setCallCountDiscountCoefficient(BigDecimal.valueOf(0.7));

    tonnageDueTariff.setDiscountCoefficientForPortOfArrival(BigDecimal.valueOf(0.1));

    log.info("Tonnage due tariff initialized");

    return tonnageDueTariff;
  }
}
