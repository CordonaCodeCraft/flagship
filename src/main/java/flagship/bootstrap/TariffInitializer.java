package flagship.bootstrap;

import flagship.domain.entities.enums.CallPurpose;
import flagship.domain.entities.enums.PortArea;
import flagship.domain.entities.enums.ShipType;
import flagship.utils.tariffs.CanalDueTariff;
import flagship.utils.tariffs.LightDueTariff;
import flagship.utils.tariffs.TonnageDueTariff;
import flagship.utils.tariffs.WharfDueTariff;
import org.javatuples.Pair;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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

        Map<ShipType, Double> tonnageDuesByShipType = new HashMap<>();
        tonnageDuesByShipType.put(OIL_TANKER, 0.50);
        tonnageDuesByShipType.put(RECREATIONAL, 0.10);
        tonnageDuesByShipType.put(MILITARY, 0.25);
        tonnageDuesByShipType.put(SPECIAL, 0.50);
        tonnageDueTariff.setTonnageDuesByShipType(tonnageDuesByShipType);

        Map<CallPurpose, Double> tonnageDuesByCallPurpose = new HashMap<>();
        tonnageDuesByCallPurpose.put(SPECIAL_PURPOSE_PORT_VISIT, 0.05);
        tonnageDueTariff.setTonnageDuesByCallPurpose(tonnageDuesByCallPurpose);

        Map<PortArea, Double> tonnageDuesByPortArea = new HashMap<>();
        tonnageDuesByPortArea.put(FIRST, 0.55);
        tonnageDuesByPortArea.put(SECOND, 0.40);
        tonnageDuesByPortArea.put(THIRD, 0.55);
        tonnageDuesByPortArea.put(FOURTH, 0.55);
        tonnageDueTariff.setTonnageDuesByPortArea(tonnageDuesByPortArea);

        Map<ShipType, Double> discountCoefficientByShipType = new HashMap<>();
        discountCoefficientByShipType.put(REEFER, 0.6);
        discountCoefficientByShipType.put(CONTAINER, 0.6);
        discountCoefficientByShipType.put(PASSENGER, 0.4);
        tonnageDueTariff.setDiscountCoefficientsByShipType(discountCoefficientByShipType);

        Map<CallPurpose, Double> discountCoefficientsByCallPurpose = new HashMap<>();
        discountCoefficientsByCallPurpose.put(RESUPPLY, 0.65);
        discountCoefficientsByCallPurpose.put(RECRUITMENT, 0.65);
        discountCoefficientsByCallPurpose.put(POSTAL, 0.65);
        discountCoefficientsByCallPurpose.put(REPAIR, 0.65);
        tonnageDueTariff.setDiscountCoefficientsByCallPurpose(discountCoefficientsByCallPurpose);

        tonnageDueTariff.setCallCountThreshold(4);
        tonnageDueTariff.setCallCountDiscountCoefficient(0.7);
    }

    private static void initializeWharfDueTariff(WharfDueTariff wharfDueTariff) {

        Map<CallPurpose, Double> discountCoefficientsByCallPurpose = new HashMap<>();
        discountCoefficientsByCallPurpose.put(RESUPPLY, 0.5);
        discountCoefficientsByCallPurpose.put(RECRUITMENT, 0.5);
        discountCoefficientsByCallPurpose.put(POSTAL, 0.5);
        discountCoefficientsByCallPurpose.put(REPAIR, 0.5);
        wharfDueTariff.setDiscountCoefficientsByCallPurpose(discountCoefficientsByCallPurpose);

        Set<ShipType> shipTypesNotEligibleForDiscount = new HashSet<>();
        shipTypesNotEligibleForDiscount.add(MILITARY);
        wharfDueTariff.setShipTypesNotEligibleForDiscount(shipTypesNotEligibleForDiscount);

        Map<ShipType, Double> wharfDuesByShipType = new HashMap<>();
        wharfDuesByShipType.put(MILITARY, 0.5);
        wharfDueTariff.setWharfDuesByShipType(wharfDuesByShipType);

        wharfDueTariff.setDefaultWharfDue(0.10);
    }

    private static void initializeCanalDueTariff(CanalDueTariff canalDueTariff) {

        Map<PortArea, Double> canalDuesByPortArea = new HashMap<>();
        canalDuesByPortArea.put(FIRST, 0.04);
        canalDuesByPortArea.put(SECOND, 0.13);
        canalDuesByPortArea.put(THIRD, 0.04);
        canalDuesByPortArea.put(FOURTH, 0.07);
        canalDueTariff.setCanalDuesByPortArea(canalDuesByPortArea);

        Map<ShipType, Double> discountCoefficientByShipType = new HashMap<>();
        discountCoefficientByShipType.put(PASSENGER, 0.5);
        canalDueTariff.setDiscountCoefficientByShipType(discountCoefficientByShipType);

        Map<PortArea, Double> discountCoefficientsByPortAreaForContainers = new HashMap<>();
        discountCoefficientsByPortAreaForContainers.put(FIRST, 0.25);
        discountCoefficientsByPortAreaForContainers.put(SECOND, 0.74);
        discountCoefficientsByPortAreaForContainers.put(THIRD, 0.25);
        discountCoefficientsByPortAreaForContainers.put(FOURTH, 0.74);
        canalDueTariff.setDiscountCoefficientsByPortAreaForContainers(discountCoefficientsByPortAreaForContainers);

        Map<PortArea, Double> discountCoefficientsByPortAreaPerCallCountForContainers = new HashMap<>();
        discountCoefficientsByPortAreaPerCallCountForContainers.put(FIRST, 0.20);
        discountCoefficientsByPortAreaPerCallCountForContainers.put(SECOND, 0.59);
        discountCoefficientsByPortAreaPerCallCountForContainers.put(THIRD, 0.20);
        discountCoefficientsByPortAreaPerCallCountForContainers.put(FOURTH, 0.59);
        canalDueTariff.setDiscountCoefficientsByPortAreaPerCallCountForContainers(discountCoefficientsByPortAreaPerCallCountForContainers);

        canalDueTariff.setCallCountThreshold(3);
        canalDueTariff.setDefaultCallCountDiscountCoefficient(0.8);

    }

    private static void initializeLightDueTariff(LightDueTariff lightDueTariff) {

        Map<Pair<Integer, Integer>, Integer> lightDuesByGrossTonnage = new HashMap<>();
        Pair<Integer, Integer> pair1 = Pair.with(41, 500);
        Pair<Integer, Integer> pair2 = Pair.with(501, 1000);
        Pair<Integer, Integer> pair3 = Pair.with(1001, 5000);
        Pair<Integer, Integer> pair4 = Pair.with(5001, 10000);
        lightDuesByGrossTonnage.put(pair1, 15);
        lightDuesByGrossTonnage.put(pair2, 40);
        lightDuesByGrossTonnage.put(pair3, 70);
        lightDuesByGrossTonnage.put(pair4, 110);
        lightDueTariff.setLightDuesByGrossTonnage(lightDuesByGrossTonnage);

        Map<ShipType, Double> lightDuesByShipType = new HashMap<>();
        lightDuesByShipType.put(MILITARY, 0.15);
        lightDueTariff.setLightDuesByShipType(lightDuesByShipType);

        Map<ShipType, Double> discountCoefficientByShipType = new HashMap<>();
        discountCoefficientByShipType.put(PASSENGER, 0.5);
        lightDueTariff.setDiscountCoefficientByShipType(discountCoefficientByShipType);

        lightDueTariff.setLightDueMaximumValue(150);
        lightDueTariff.setCallCountThreshold(4);
        lightDueTariff.setCallCountDiscountCoefficient(0.7);
    }
}
