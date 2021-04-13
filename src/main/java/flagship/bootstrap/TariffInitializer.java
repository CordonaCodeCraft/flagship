package flagship.bootstrap;

import flagship.domain.cases.entities.enums.CallPurpose;
import flagship.domain.cases.entities.enums.PortArea;
import flagship.domain.cases.entities.enums.ShipType;
import flagship.domain.utils.tariffs.CanalDueTariff;
import flagship.domain.utils.tariffs.LightDueTariff;
import flagship.domain.utils.tariffs.TonnageDueTariff;
import flagship.domain.utils.tariffs.WharfDueTariff;
import org.javatuples.Pair;

import java.util.*;

import static flagship.domain.cases.entities.enums.CallPurpose.*;
import static flagship.domain.cases.entities.enums.PortArea.*;
import static flagship.domain.cases.entities.enums.ShipType.*;

public class TariffInitializer {

    public static void initializeTariffs(
            final TonnageDueTariff tonnageDueTariff,
            final WharfDueTariff wharfDueTariff,
            final CanalDueTariff canalDueTariff,
            final LightDueTariff lightDueTariff) {
        initializeTonnageDueTariff(tonnageDueTariff);
        initializeWharfDueTariff(wharfDueTariff);
        initializeCanalDueTariff(canalDueTariff);
        initializeLightDueTariff(lightDueTariff);
    }

    private static void initializeTonnageDueTariff(final TonnageDueTariff tonnageDueTariff) {

        final Map<PortArea, Double> tonnageDuesByPortArea = new EnumMap<>(PortArea.class);
        tonnageDuesByPortArea.put(FIRST, 0.55);
        tonnageDuesByPortArea.put(SECOND, 0.40);
        tonnageDuesByPortArea.put(THIRD, 0.55);
        tonnageDuesByPortArea.put(FOURTH, 0.55);
        tonnageDueTariff.setTonnageDuesByPortArea(Collections.unmodifiableMap(tonnageDuesByPortArea));

        final Map<ShipType, Double> tonnageDuesByShipType = new EnumMap<>(ShipType.class);
        tonnageDuesByShipType.put(OIL_TANKER, 0.50);
        tonnageDuesByShipType.put(RECREATIONAL, 0.10);
        tonnageDuesByShipType.put(MILITARY, 0.25);
        tonnageDuesByShipType.put(SPECIAL, 0.50);
        tonnageDueTariff.setTonnageDuesByShipType(Collections.unmodifiableMap(tonnageDuesByShipType));

        final Map<CallPurpose, Double> tonnageDuesByCallPurpose = new EnumMap<>(CallPurpose.class);
        tonnageDuesByCallPurpose.put(SPECIAL_PURPOSE_PORT_VISIT, 0.05);
        tonnageDueTariff.setTonnageDuesByCallPurpose(Collections.unmodifiableMap(tonnageDuesByCallPurpose));

        final Map<CallPurpose, Double> discountCoefficientsByCallPurpose = new EnumMap<>(CallPurpose.class);
        discountCoefficientsByCallPurpose.put(RESUPPLY, 0.65);
        discountCoefficientsByCallPurpose.put(RECRUITMENT, 0.65);
        discountCoefficientsByCallPurpose.put(POSTAL, 0.65);
        discountCoefficientsByCallPurpose.put(REPAIR, 0.65);
        tonnageDueTariff.setDiscountCoefficientsByCallPurpose(Collections.unmodifiableMap(discountCoefficientsByCallPurpose));

        final Map<ShipType, Double> discountCoefficientByShipType = new EnumMap<>(ShipType.class);
        discountCoefficientByShipType.put(REEFER, 0.6);
        discountCoefficientByShipType.put(CONTAINER, 0.6);
        discountCoefficientByShipType.put(PASSENGER, 0.4);
        tonnageDueTariff.setDiscountCoefficientsByShipType(Collections.unmodifiableMap(discountCoefficientByShipType));

        final Set<ShipType> shipTypesNotEligibleForDiscount = EnumSet.of(RECREATIONAL, MILITARY, SPECIAL);
        tonnageDueTariff.setShipTypesNotEligibleForDiscount(Collections.unmodifiableSet(shipTypesNotEligibleForDiscount));

        final Set<CallPurpose> callPurposesNotEligibleForDiscount = EnumSet.of(SPECIAL_PURPOSE_PORT_VISIT);
        tonnageDueTariff.setCallPurposesNotEligibleForDiscount(Collections.unmodifiableSet(callPurposesNotEligibleForDiscount));

        tonnageDueTariff.setCallCountThreshold(4);
        tonnageDueTariff.setCallCountDiscountCoefficient(0.7);
    }

    private static void initializeWharfDueTariff(final WharfDueTariff wharfDueTariff) {

        final Map<ShipType, Double> wharfDuesByShipType = new EnumMap<>(ShipType.class);
        wharfDuesByShipType.put(MILITARY, 0.5);
        wharfDueTariff.setWharfDuesByShipType(Collections.unmodifiableMap(wharfDuesByShipType));

        final Map<CallPurpose, Double> discountCoefficientsByCallPurpose = new EnumMap<>(CallPurpose.class);
        discountCoefficientsByCallPurpose.put(RESUPPLY, 0.5);
        discountCoefficientsByCallPurpose.put(RECRUITMENT, 0.5);
        discountCoefficientsByCallPurpose.put(POSTAL, 0.5);
        discountCoefficientsByCallPurpose.put(REPAIR, 0.5);
        wharfDueTariff.setDiscountCoefficientsByCallPurpose(Collections.unmodifiableMap(discountCoefficientsByCallPurpose));

        final Set<ShipType> shipTypesNotEligibleForDiscount = EnumSet.of(MILITARY);
        wharfDueTariff.setShipTypesNotEligibleForDiscount(Collections.unmodifiableSet(shipTypesNotEligibleForDiscount));

        wharfDueTariff.setDefaultWharfDue(0.10);
    }

    private static void initializeCanalDueTariff(final CanalDueTariff canalDueTariff) {

        final Map<PortArea, Double> canalDuesByPortArea = new EnumMap<>(PortArea.class);
        canalDuesByPortArea.put(FIRST, 0.04);
        canalDuesByPortArea.put(SECOND, 0.13);
        canalDuesByPortArea.put(THIRD, 0.04);
        canalDuesByPortArea.put(FOURTH, 0.07);
        canalDueTariff.setCanalDuesByPortArea(Collections.unmodifiableMap(canalDuesByPortArea));

        final Map<ShipType, Double> discountCoefficientByShipType = new EnumMap<>(ShipType.class);
        discountCoefficientByShipType.put(PASSENGER, 0.5);
        canalDueTariff.setDiscountCoefficientByShipType(Collections.unmodifiableMap(discountCoefficientByShipType));

        final Map<PortArea, Double> discountCoefficientsByPortAreaForContainers = new EnumMap<>(PortArea.class);
        discountCoefficientsByPortAreaForContainers.put(FIRST, 0.25);
        discountCoefficientsByPortAreaForContainers.put(SECOND, 0.74);
        discountCoefficientsByPortAreaForContainers.put(THIRD, 0.25);
        discountCoefficientsByPortAreaForContainers.put(FOURTH, 0.74);
        canalDueTariff.setDiscountCoefficientsByPortAreaForContainers(Collections.unmodifiableMap(discountCoefficientsByPortAreaForContainers));

        final Map<PortArea, Double> discountCoefficientsByPortAreaPerCallCountForContainers = new EnumMap<>(PortArea.class);
        discountCoefficientsByPortAreaPerCallCountForContainers.put(FIRST, 0.20);
        discountCoefficientsByPortAreaPerCallCountForContainers.put(SECOND, 0.59);
        discountCoefficientsByPortAreaPerCallCountForContainers.put(THIRD, 0.20);
        discountCoefficientsByPortAreaPerCallCountForContainers.put(FOURTH, 0.59);
        canalDueTariff.setDiscountCoefficientsByPortAreaPerCallCountForContainers(Collections.unmodifiableMap(discountCoefficientsByPortAreaPerCallCountForContainers));

        final Set<ShipType> shipTypesNotEligibleForDiscount = EnumSet.of(MILITARY);
        canalDueTariff.setShipTypesNotEligibleForDiscount(Collections.unmodifiableSet(shipTypesNotEligibleForDiscount));

        canalDueTariff.setCallCountThreshold(3);
        canalDueTariff.setDefaultCallCountDiscountCoefficient(0.8);
    }

    private static void initializeLightDueTariff(final LightDueTariff lightDueTariff) {

        final Map<Pair<Integer, Integer>, Double> lightDuesByGrossTonnage = new HashMap<>();

        final Pair<Integer, Integer> pair1 = Pair.with(41, 500);
        final Pair<Integer, Integer> pair2 = Pair.with(501, 1000);
        final Pair<Integer, Integer> pair3 = Pair.with(1001, 5000);
        final Pair<Integer, Integer> pair4 = Pair.with(5001, 10000);

        lightDuesByGrossTonnage.put(pair1, 15.0);
        lightDuesByGrossTonnage.put(pair2, 40.0);
        lightDuesByGrossTonnage.put(pair3, 70.0);
        lightDuesByGrossTonnage.put(pair4, 110.0);
        lightDueTariff.setLightDuesByGrossTonnage(Collections.unmodifiableMap(lightDuesByGrossTonnage));

        final Map<ShipType, Double> lightDuesByShipType = new EnumMap<>(ShipType.class);
        lightDuesByShipType.put(MILITARY, 0.15);
        lightDueTariff.setLightDuesPerTonByShipType(Collections.unmodifiableMap(lightDuesByShipType));

        final Map<ShipType, Double> discountCoefficientsByShipType = new EnumMap<>(ShipType.class);
        discountCoefficientsByShipType.put(PASSENGER, 0.5);
        lightDueTariff.setDiscountCoefficientsByShipType(Collections.unmodifiableMap(discountCoefficientsByShipType));

        lightDueTariff.setLightDueMaximumValue(150);
        lightDueTariff.setCallCountThreshold(4);
        lightDueTariff.setCallCountDiscountCoefficient(0.7);
    }
}
