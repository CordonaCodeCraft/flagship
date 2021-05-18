package flagship.bootstrap.newinstalation;

import flagship.domain.cases.entities.enums.CallPurpose;
import flagship.domain.cases.entities.enums.ShipType;
import flagship.domain.tariffs.PortArea;
import flagship.domain.tariffs.mix.Due;
import flagship.domain.tariffs.statedues.TonnageDueTariff;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

import static flagship.domain.cases.entities.enums.CallPurpose.*;
import static flagship.domain.cases.entities.enums.ShipType.*;
import static flagship.domain.tariffs.PortArea.*;

@Component
@Slf4j
@RequiredArgsConstructor
public class TonnageDueTariffInitializer {

  public static TonnageDueTariff getTariff() {

    final TonnageDueTariff tonnageDueTariff = new TonnageDueTariff();

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

    tonnageDueTariff.setTonnageDuesByPortArea(Collections.unmodifiableMap(tonnageDuesByPortArea));
    tonnageDueTariff.setTonnageDuesByShipType(Collections.unmodifiableMap(tonnageDuesByShipType));
    tonnageDueTariff.setTonnageDuesByCallPurpose(
        Collections.unmodifiableMap(tonnageDuesByCallPurpose));
    tonnageDueTariff.setDiscountCoefficientsByCallPurpose(
        Collections.unmodifiableMap(discountCoefficientsByCallPurpose));
    tonnageDueTariff.setDiscountCoefficientsByShipType(
        Collections.unmodifiableMap(discountCoefficientByShipType));
    tonnageDueTariff.setShipTypesNotEligibleForDiscount(
        Collections.unmodifiableSet(shipTypesNotEligibleForDiscount));
    tonnageDueTariff.setCallPurposesNotEligibleForDiscount(
        Collections.unmodifiableSet(callPurposesNotEligibleForDiscount));
    tonnageDueTariff.setCallCountThreshold(4);
    tonnageDueTariff.setCallCountDiscountCoefficient(BigDecimal.valueOf(0.7));
    tonnageDueTariff.setDiscountCoefficientForPortOfArrival(BigDecimal.valueOf(0.1));

    log.info("Tonnage due tariff initialized");

    return tonnageDueTariff;
  }
}
