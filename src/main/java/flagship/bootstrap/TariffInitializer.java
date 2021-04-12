package flagship.bootstrap;

import flagship.domain.entities.enums.CallPurpose;
import flagship.domain.entities.enums.PortArea;
import flagship.domain.entities.enums.ShipType;
import flagship.utils.tariffs.CanalDueTariff;
import flagship.utils.tariffs.LightDueTariff;
import flagship.utils.tariffs.TonnageDueTariff;
import flagship.utils.tariffs.WharfDueTariff;
import org.javatuples.Pair;

import java.util.*;

import static flagship.domain.entities.enums.CallPurpose.*;
import static flagship.domain.entities.enums.PortArea.*;
import static flagship.domain.entities.enums.ShipType.*;

public class TariffInitializer {

    public static void initializeTariffs(TonnageDueTariff tonnageDueTariff, WharfDueTariff wharfDueTariff, CanalDueTariff canalDueTariff, LightDueTariff lightDueTariff) {
        initializeTonnageDueTariff(tonnageDueTariff);
        initializeWharfDueTariff(wharfDueTariff);
        initializeCanalDueTariff(canalDueTariff);
        initializeLightDueTariff(lightDueTariff);
    }


    private static void initializeTonnageDueTariff(TonnageDueTariff tonnageDueTariff) {

        Map<PortArea, Double> tonnageDuesByPortArea = new EnumMap<>(PortArea.class);
        tonnageDuesByPortArea.put(FIRST, 0.55);
        tonnageDuesByPortArea.put(SECOND, 0.40);
        tonnageDuesByPortArea.put(THIRD, 0.55);
        tonnageDuesByPortArea.put(FOURTH, 0.55);
        tonnageDueTariff.setTonnageDuesByPortArea(tonnageDuesByPortArea);

        Map<ShipType, Double> tonnageDuesByShipType = new EnumMap<>(ShipType.class);
        tonnageDuesByShipType.put(OIL_TANKER, 0.50);
        tonnageDuesByShipType.put(RECREATIONAL, 0.10);
        tonnageDuesByShipType.put(MILITARY, 0.25);
        tonnageDuesByShipType.put(SPECIAL, 0.50);
        tonnageDueTariff.setTonnageDuesByShipType(tonnageDuesByShipType);

        Map<CallPurpose, Double> tonnageDuesByCallPurpose = new EnumMap<>(CallPurpose.class);
        tonnageDuesByCallPurpose.put(SPECIAL_PURPOSE_PORT_VISIT, 0.05);
        tonnageDueTariff.setTonnageDuesByCallPurpose(tonnageDuesByCallPurpose);

        Map<CallPurpose, Double> discountCoefficientsByCallPurpose = new EnumMap<>(CallPurpose.class);
        discountCoefficientsByCallPurpose.put(RESUPPLY, 0.65);
        discountCoefficientsByCallPurpose.put(RECRUITMENT, 0.65);
        discountCoefficientsByCallPurpose.put(POSTAL, 0.65);
        discountCoefficientsByCallPurpose.put(REPAIR, 0.65);
        tonnageDueTariff.setDiscountCoefficientsByCallPurpose(discountCoefficientsByCallPurpose);

        Map<ShipType, Double> discountCoefficientByShipType = new EnumMap<>(ShipType.class);
        discountCoefficientByShipType.put(REEFER, 0.6);
        discountCoefficientByShipType.put(CONTAINER, 0.6);
        discountCoefficientByShipType.put(PASSENGER, 0.4);
        tonnageDueTariff.setDiscountCoefficientsByShipType(discountCoefficientByShipType);

        Set<ShipType> shipTypesNotEligibleForDiscount = EnumSet.of(RECREATIONAL, MILITARY, SPECIAL);
        tonnageDueTariff.setShipTypesNotEligibleForDiscount(shipTypesNotEligibleForDiscount);

        Set<CallPurpose> callPurposesNotEligibleForDiscount = EnumSet.of(SPECIAL_PURPOSE_PORT_VISIT);
        tonnageDueTariff.setCallPurposesNotEligibleForDiscount(callPurposesNotEligibleForDiscount);

        tonnageDueTariff.setCallCountThreshold(4);
        tonnageDueTariff.setCallCountDiscountCoefficient(0.7);
    }

    private static void initializeWharfDueTariff(WharfDueTariff wharfDueTariff) {

        Map<ShipType, Double> wharfDuesByShipType = new EnumMap<>(ShipType.class);
        wharfDuesByShipType.put(MILITARY, 0.5);
        wharfDueTariff.setWharfDuesByShipType(wharfDuesByShipType);

        Map<CallPurpose, Double> discountCoefficientsByCallPurpose = new EnumMap<>(CallPurpose.class);
        discountCoefficientsByCallPurpose.put(RESUPPLY, 0.5);
        discountCoefficientsByCallPurpose.put(RECRUITMENT, 0.5);
        discountCoefficientsByCallPurpose.put(POSTAL, 0.5);
        discountCoefficientsByCallPurpose.put(REPAIR, 0.5);
        wharfDueTariff.setDiscountCoefficientsByCallPurpose(discountCoefficientsByCallPurpose);

        Set<ShipType> shipTypesNotEligibleForDiscount = EnumSet.of(MILITARY);
        wharfDueTariff.setShipTypesNotEligibleForDiscount(shipTypesNotEligibleForDiscount);

        wharfDueTariff.setDefaultWharfDue(0.10);
    }

    private static void initializeCanalDueTariff(CanalDueTariff canalDueTariff) {

        Map<PortArea, Double> canalDuesByPortArea = new EnumMap<>(PortArea.class);
        canalDuesByPortArea.put(FIRST, 0.04);
        canalDuesByPortArea.put(SECOND, 0.13);
        canalDuesByPortArea.put(THIRD, 0.04);
        canalDuesByPortArea.put(FOURTH, 0.07);
        canalDueTariff.setCanalDuesByPortArea(canalDuesByPortArea);

        Map<ShipType, Double> discountCoefficientByShipType = new EnumMap<>(ShipType.class);
        discountCoefficientByShipType.put(PASSENGER, 0.5);
        canalDueTariff.setDiscountCoefficientByShipType(discountCoefficientByShipType);

        Map<PortArea, Double> discountCoefficientsByPortAreaForContainers = new EnumMap<>(PortArea.class);
        discountCoefficientsByPortAreaForContainers.put(FIRST, 0.25);
        discountCoefficientsByPortAreaForContainers.put(SECOND, 0.74);
        discountCoefficientsByPortAreaForContainers.put(THIRD, 0.25);
        discountCoefficientsByPortAreaForContainers.put(FOURTH, 0.74);
        canalDueTariff.setDiscountCoefficientsByPortAreaForContainers(discountCoefficientsByPortAreaForContainers);

        Map<PortArea, Double> discountCoefficientsByPortAreaPerCallCountForContainers = new EnumMap<>(PortArea.class);
        discountCoefficientsByPortAreaPerCallCountForContainers.put(FIRST, 0.20);
        discountCoefficientsByPortAreaPerCallCountForContainers.put(SECOND, 0.59);
        discountCoefficientsByPortAreaPerCallCountForContainers.put(THIRD, 0.20);
        discountCoefficientsByPortAreaPerCallCountForContainers.put(FOURTH, 0.59);
        canalDueTariff.setDiscountCoefficientsByPortAreaPerCallCountForContainers(discountCoefficientsByPortAreaPerCallCountForContainers);

        Set<ShipType> shipTypesNotEligibleForDiscount = EnumSet.of(MILITARY);
        canalDueTariff.setShipTypesNotEligibleForDiscount(shipTypesNotEligibleForDiscount);

        canalDueTariff.setCallCountThreshold(3);
        canalDueTariff.setDefaultCallCountDiscountCoefficient(0.8);

    }

    private static void initializeLightDueTariff(LightDueTariff lightDueTariff) {

        Map<Pair<Integer, Integer>, Double> lightDuesByGrossTonnage = new HashMap<>();

        Pair<Integer, Integer> pair1 = Pair.with(41, 500);
        Pair<Integer, Integer> pair2 = Pair.with(501, 1000);
        Pair<Integer, Integer> pair3 = Pair.with(1001, 5000);
        Pair<Integer, Integer> pair4 = Pair.with(5001, 10000);

        lightDuesByGrossTonnage.put(pair1, 15.0);
        lightDuesByGrossTonnage.put(pair2, 40.0);
        lightDuesByGrossTonnage.put(pair3, 70.0);
        lightDuesByGrossTonnage.put(pair4, 110.0);
        lightDueTariff.setLightDuesByGrossTonnage(lightDuesByGrossTonnage);

        Map<ShipType, Double> lightDuesByShipType = new EnumMap<>(ShipType.class);
        lightDuesByShipType.put(MILITARY, 0.15);
        lightDueTariff.setLightDuesByShipType(lightDuesByShipType);

        Map<ShipType, Double> discountCoefficientsByShipType = new EnumMap<>(ShipType.class);
        discountCoefficientsByShipType.put(PASSENGER, 0.5);
        lightDueTariff.setDiscountCoefficientsByShipType(discountCoefficientsByShipType);

        lightDueTariff.setLightDueMaximumValue(150);
        lightDueTariff.setCallCountThreshold(4);
        lightDueTariff.setCallCountDiscountCoefficient(0.7);
    }
}
