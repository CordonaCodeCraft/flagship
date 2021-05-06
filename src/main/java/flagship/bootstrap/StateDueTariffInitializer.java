package flagship.bootstrap;

import flagship.domain.cases.entities.enums.CallPurpose;
import flagship.domain.cases.entities.enums.ShipType;
import flagship.domain.tariffs.GtRange;
import flagship.domain.tariffs.stateduestariffs.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

import static flagship.domain.cases.entities.enums.CallPurpose.*;
import static flagship.domain.cases.entities.enums.ShipType.*;
import static flagship.domain.tariffs.stateduestariffs.PortArea.*;

@Component
public class StateDueTariffInitializer {

  public static void initializeTariffs(
      final TonnageDueTariff tonnageDueTariff,
      final WharfDueTariff wharfDueTariff,
      final CanalDueTariff canalDueTariff,
      final LightDueTariff lightDueTariff,
      MarpolDueTariff marpolDueTariff) {
    initializeTonnageDueTariff(tonnageDueTariff);
    initializeWharfDueTariff(wharfDueTariff);
    initializeCanalDueTariff(canalDueTariff);
    initializeLightDueTariff(lightDueTariff);
    initializeMarpolDueTariff(marpolDueTariff);
  }

  private static void initializeTonnageDueTariff(final TonnageDueTariff tonnageDueTariff) {

    final Map<PortArea, BigDecimal> tonnageDuesByPortArea = new EnumMap<>(PortArea.class);
    tonnageDuesByPortArea.put(FIRST, BigDecimal.valueOf(0.55));
    tonnageDuesByPortArea.put(SECOND, BigDecimal.valueOf(0.40));
    tonnageDuesByPortArea.put(THIRD, BigDecimal.valueOf(0.55));
    tonnageDuesByPortArea.put(FOURTH, BigDecimal.valueOf(0.55));
    tonnageDueTariff.setTonnageDuesByPortArea(Collections.unmodifiableMap(tonnageDuesByPortArea));

    final Map<ShipType, BigDecimal> tonnageDuesByShipType = new EnumMap<>(ShipType.class);
    tonnageDuesByShipType.put(OIL_TANKER, BigDecimal.valueOf(0.50));
    tonnageDuesByShipType.put(RECREATIONAL, BigDecimal.valueOf(0.10));
    tonnageDuesByShipType.put(MILITARY, BigDecimal.valueOf(0.25));
    tonnageDuesByShipType.put(SPECIAL, BigDecimal.valueOf(0.50));
    tonnageDueTariff.setTonnageDuesByShipType(Collections.unmodifiableMap(tonnageDuesByShipType));

    final Map<CallPurpose, BigDecimal> tonnageDuesByCallPurpose = new EnumMap<>(CallPurpose.class);
    tonnageDuesByCallPurpose.put(SPECIAL_PURPOSE_PORT_VISIT, BigDecimal.valueOf(0.05));
    tonnageDueTariff.setTonnageDuesByCallPurpose(
        Collections.unmodifiableMap(tonnageDuesByCallPurpose));

    final Map<CallPurpose, BigDecimal> discountCoefficientsByCallPurpose =
        new EnumMap<>(CallPurpose.class);
    discountCoefficientsByCallPurpose.put(RESUPPLY, BigDecimal.valueOf(0.65));
    discountCoefficientsByCallPurpose.put(RECRUITMENT, BigDecimal.valueOf(0.65));
    discountCoefficientsByCallPurpose.put(POSTAL, BigDecimal.valueOf(0.65));
    discountCoefficientsByCallPurpose.put(REPAIR, BigDecimal.valueOf(0.65));
    tonnageDueTariff.setDiscountCoefficientsByCallPurpose(
        Collections.unmodifiableMap(discountCoefficientsByCallPurpose));

    final Map<ShipType, BigDecimal> discountCoefficientByShipType = new EnumMap<>(ShipType.class);
    discountCoefficientByShipType.put(REEFER, BigDecimal.valueOf(0.6));
    discountCoefficientByShipType.put(CONTAINER, BigDecimal.valueOf(0.6));
    discountCoefficientByShipType.put(PASSENGER, BigDecimal.valueOf(0.4));
    tonnageDueTariff.setDiscountCoefficientsByShipType(
        Collections.unmodifiableMap(discountCoefficientByShipType));

    final Set<ShipType> shipTypesNotEligibleForDiscount =
        EnumSet.of(RECREATIONAL, MILITARY, SPECIAL);
    tonnageDueTariff.setShipTypesNotEligibleForDiscount(
        Collections.unmodifiableSet(shipTypesNotEligibleForDiscount));

    final Set<CallPurpose> callPurposesNotEligibleForDiscount =
        EnumSet.of(SPECIAL_PURPOSE_PORT_VISIT);
    tonnageDueTariff.setCallPurposesNotEligibleForDiscount(
        Collections.unmodifiableSet(callPurposesNotEligibleForDiscount));

    tonnageDueTariff.setCallCountThreshold(4);
    tonnageDueTariff.setCallCountDiscountCoefficient(BigDecimal.valueOf(0.7));
    tonnageDueTariff.setDiscountCoefficientForPortOfArrival(BigDecimal.valueOf(0.1));
  }

  private static void initializeWharfDueTariff(final WharfDueTariff wharfDueTariff) {

    final Map<ShipType, BigDecimal> wharfDuesByShipType = new EnumMap<>(ShipType.class);
    wharfDuesByShipType.put(MILITARY, BigDecimal.valueOf(0.5));
    wharfDueTariff.setWharfDuesByShipType(Collections.unmodifiableMap(wharfDuesByShipType));

    final Map<CallPurpose, BigDecimal> discountCoefficientsByCallPurpose =
        new EnumMap<>(CallPurpose.class);
    discountCoefficientsByCallPurpose.put(RESUPPLY, BigDecimal.valueOf(0.5));
    discountCoefficientsByCallPurpose.put(RECRUITMENT, BigDecimal.valueOf(0.5));
    discountCoefficientsByCallPurpose.put(POSTAL, BigDecimal.valueOf(0.5));
    discountCoefficientsByCallPurpose.put(REPAIR, BigDecimal.valueOf(0.5));
    wharfDueTariff.setDiscountCoefficientsByCallPurpose(
        Collections.unmodifiableMap(discountCoefficientsByCallPurpose));

    final Set<ShipType> shipTypesNotEligibleForDiscount = EnumSet.of(MILITARY);
    wharfDueTariff.setShipTypesNotEligibleForDiscount(
        Collections.unmodifiableSet(shipTypesNotEligibleForDiscount));

    wharfDueTariff.setDefaultWharfDue(BigDecimal.valueOf(0.10));
  }

  private static void initializeCanalDueTariff(final CanalDueTariff canalDueTariff) {

    final Map<PortArea, BigDecimal> canalDuesByPortArea = new EnumMap<>(PortArea.class);
    canalDuesByPortArea.put(FIRST, BigDecimal.valueOf(0.04));
    canalDuesByPortArea.put(SECOND, BigDecimal.valueOf(0.13));
    canalDuesByPortArea.put(THIRD, BigDecimal.valueOf(0.04));
    canalDuesByPortArea.put(FOURTH, BigDecimal.valueOf(0.07));
    canalDueTariff.setCanalDuesByPortArea(Collections.unmodifiableMap(canalDuesByPortArea));

    final Map<ShipType, BigDecimal> discountCoefficientByShipType = new EnumMap<>(ShipType.class);
    discountCoefficientByShipType.put(PASSENGER, BigDecimal.valueOf(0.5));
    canalDueTariff.setDiscountCoefficientByShipType(
        Collections.unmodifiableMap(discountCoefficientByShipType));

    final Map<PortArea, BigDecimal> discountCoefficientsByPortAreaForContainers =
        new EnumMap<>(PortArea.class);
    discountCoefficientsByPortAreaForContainers.put(FIRST, BigDecimal.valueOf(0.25));
    discountCoefficientsByPortAreaForContainers.put(SECOND, BigDecimal.valueOf(0.74));
    discountCoefficientsByPortAreaForContainers.put(THIRD, BigDecimal.valueOf(0.25));
    discountCoefficientsByPortAreaForContainers.put(FOURTH, BigDecimal.valueOf(0.74));
    canalDueTariff.setDiscountCoefficientsByPortAreaForContainers(
        Collections.unmodifiableMap(discountCoefficientsByPortAreaForContainers));

    final Map<PortArea, BigDecimal> discountCoefficientsByPortAreaPerCallCountForContainers =
        new EnumMap<>(PortArea.class);
    discountCoefficientsByPortAreaPerCallCountForContainers.put(FIRST, BigDecimal.valueOf(0.20));
    discountCoefficientsByPortAreaPerCallCountForContainers.put(SECOND, BigDecimal.valueOf(0.59));
    discountCoefficientsByPortAreaPerCallCountForContainers.put(THIRD, BigDecimal.valueOf(0.20));
    discountCoefficientsByPortAreaPerCallCountForContainers.put(FOURTH, BigDecimal.valueOf(0.59));
    canalDueTariff.setDiscountCoefficientsByPortAreaPerCallCountForContainers(
        Collections.unmodifiableMap(discountCoefficientsByPortAreaPerCallCountForContainers));

    final Set<ShipType> shipTypesNotEligibleForDiscount = EnumSet.of(MILITARY);
    canalDueTariff.setShipTypesNotEligibleForDiscount(
        Collections.unmodifiableSet(shipTypesNotEligibleForDiscount));

    canalDueTariff.setCallCountThreshold(3);
    canalDueTariff.setDefaultCallCountDiscountCoefficient(BigDecimal.valueOf(0.8));
  }

  private static void initializeLightDueTariff(final LightDueTariff lightDueTariff) {

    final Map<BigDecimal, Integer[]> lightDuesByGrossTonnage = new TreeMap<>();

    lightDuesByGrossTonnage.put(BigDecimal.valueOf(15.00), new Integer[] {41, 500});
    lightDuesByGrossTonnage.put(BigDecimal.valueOf(40.00), new Integer[] {501, 1000});
    lightDuesByGrossTonnage.put(BigDecimal.valueOf(70.00), new Integer[] {1001, 5000});
    lightDuesByGrossTonnage.put(BigDecimal.valueOf(110.00), new Integer[] {5001, 10000});

    lightDueTariff.setLightDuesByGrossTonnage(Collections.unmodifiableMap(lightDuesByGrossTonnage));

    final Map<ShipType, BigDecimal> lightDuesByShipType = new EnumMap<>(ShipType.class);
    lightDuesByShipType.put(MILITARY, BigDecimal.valueOf(0.15));
    lightDueTariff.setLightDuesPerTonByShipType(Collections.unmodifiableMap(lightDuesByShipType));

    final Map<ShipType, BigDecimal> discountCoefficientsByShipType = new EnumMap<>(ShipType.class);
    discountCoefficientsByShipType.put(PASSENGER, BigDecimal.valueOf(0.5));
    lightDueTariff.setDiscountCoefficientsByShipType(
        Collections.unmodifiableMap(discountCoefficientsByShipType));

    final Set<ShipType> shipTypesNotEligibleForDiscount = EnumSet.of(MILITARY);
    lightDueTariff.setShipTypesNotEligibleForDiscount(
        Collections.unmodifiableSet(shipTypesNotEligibleForDiscount));

    lightDueTariff.setLightDueMaximumValue(BigDecimal.valueOf(150.00));
    lightDueTariff.setCallCountThreshold(4);
    lightDueTariff.setCallCountDiscountCoefficient(BigDecimal.valueOf(0.7));
  }

  private static void initializeMarpolDueTariff(MarpolDueTariff marpolDueTariff) {

    Map<GtRange, BigDecimal> freeSewageDisposalQuantitiesPerGT = new LinkedHashMap<>();

    freeSewageDisposalQuantitiesPerGT.put(new GtRange(150, 2000), BigDecimal.valueOf(8.6508));
    freeSewageDisposalQuantitiesPerGT.put(new GtRange(2001, 3000), BigDecimal.valueOf(10.5314));
    freeSewageDisposalQuantitiesPerGT.put(new GtRange(3001, 6000), BigDecimal.valueOf(10.5315));
    freeSewageDisposalQuantitiesPerGT.put(new GtRange(6001, 10000), BigDecimal.valueOf(15.0448));
    freeSewageDisposalQuantitiesPerGT.put(new GtRange(10001, 20000), BigDecimal.valueOf(19.5583));
    freeSewageDisposalQuantitiesPerGT.put(new GtRange(20001, 30000), BigDecimal.valueOf(21.0628));
    freeSewageDisposalQuantitiesPerGT.put(new GtRange(30001, 40000), BigDecimal.valueOf(27.0807));
    freeSewageDisposalQuantitiesPerGT.put(new GtRange(40001, 50000), BigDecimal.valueOf(28.5852));

    Map<GtRange, BigDecimal> freeGarbageDisposalQuantitiesPerGT = new LinkedHashMap<>();

    freeGarbageDisposalQuantitiesPerGT.put(new GtRange(150, 2000), BigDecimal.valueOf(10.72));
    freeGarbageDisposalQuantitiesPerGT.put(new GtRange(2001, 3000), BigDecimal.valueOf(11.43));
    freeGarbageDisposalQuantitiesPerGT.put(new GtRange(3001, 6000), BigDecimal.valueOf(12.86));
    freeGarbageDisposalQuantitiesPerGT.put(new GtRange(6001, 10000), BigDecimal.valueOf(23.58));
    freeGarbageDisposalQuantitiesPerGT.put(new GtRange(10001, 20000), BigDecimal.valueOf(26.43));
    freeGarbageDisposalQuantitiesPerGT.put(new GtRange(20001, 30000), BigDecimal.valueOf(32.15));
    freeGarbageDisposalQuantitiesPerGT.put(new GtRange(30001, 40000), BigDecimal.valueOf(50.01));
    freeGarbageDisposalQuantitiesPerGT.put(new GtRange(40001, 50000), BigDecimal.valueOf(71.45));

    Map<GtRange, BigDecimal[]> marpolDuePerGrossTonnage = new LinkedHashMap<>();
    marpolDuePerGrossTonnage.put(
        new GtRange(150, 2000),
        new BigDecimal[] {
          BigDecimal.valueOf(35.00), BigDecimal.valueOf(5.00), BigDecimal.valueOf(25.00)
        });
    marpolDuePerGrossTonnage.put(
        new GtRange(2001, 3000),
        new BigDecimal[] {
          BigDecimal.valueOf(100.00), BigDecimal.valueOf(10.00), BigDecimal.valueOf(50.00)
        });
    marpolDuePerGrossTonnage.put(
        new GtRange(3001, 6000),
        new BigDecimal[] {
          BigDecimal.valueOf(130.00), BigDecimal.valueOf(15.00), BigDecimal.valueOf(65.00)
        });
    marpolDuePerGrossTonnage.put(
        new GtRange(6001, 10000),
        new BigDecimal[] {
          BigDecimal.valueOf(200.00), BigDecimal.valueOf(20.00), BigDecimal.valueOf(85.00)
        });
    marpolDuePerGrossTonnage.put(
        new GtRange(10001, 20000),
        new BigDecimal[] {
          BigDecimal.valueOf(220.00), BigDecimal.valueOf(25.00), BigDecimal.valueOf(120.00)
        });
    marpolDuePerGrossTonnage.put(
        new GtRange(20001, 30000),
        new BigDecimal[] {
          BigDecimal.valueOf(250.00), BigDecimal.valueOf(30.00), BigDecimal.valueOf(180.00)
        });
    marpolDuePerGrossTonnage.put(
        new GtRange(30001, 40000),
        new BigDecimal[] {
          BigDecimal.valueOf(450.00), BigDecimal.valueOf(35.00), BigDecimal.valueOf(250.00)
        });
    marpolDuePerGrossTonnage.put(
        new GtRange(40001, 50000),
        new BigDecimal[] {
          BigDecimal.valueOf(700.00), BigDecimal.valueOf(40.00), BigDecimal.valueOf(400.00)
        });

    marpolDueTariff.setFreeSewageDisposalQuantitiesPerGrossTonnage(
        Collections.unmodifiableMap(freeSewageDisposalQuantitiesPerGT));
    marpolDueTariff.setFreeGarbageDisposalQuantitiesPerGrossTonnage(
        Collections.unmodifiableMap(freeGarbageDisposalQuantitiesPerGT));
    marpolDueTariff.setMarpolDuePerGrossTonnage(
        Collections.unmodifiableMap(marpolDuePerGrossTonnage));

    marpolDueTariff.setMaximumFreeSewageDisposalQuantity(BigDecimal.valueOf(30.0897));
    marpolDueTariff.setMaximumFreeGarbageDisposalQuantity(BigDecimal.valueOf(107.17));
    marpolDueTariff.setMaximumMarpolDueValues(
        new BigDecimal[] {
          BigDecimal.valueOf(900.00), BigDecimal.valueOf(50.00), BigDecimal.valueOf(550.00)
        });
    marpolDueTariff.setOdessosFixedMarpolDue(BigDecimal.valueOf(120.00));
    marpolDueTariff.setOdessosFreeGarbageDisposalQuantity(BigDecimal.valueOf(10.00));
    marpolDueTariff.setOdessosFreeSewageDisposalQuantity(BigDecimal.valueOf(1.00));
  }
}
