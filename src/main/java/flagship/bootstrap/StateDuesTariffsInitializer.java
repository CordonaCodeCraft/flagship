package flagship.bootstrap;

import flagship.domain.cases.entities.enums.CallPurpose;
import flagship.domain.cases.entities.enums.ShipType;
import flagship.domain.tariffs.*;
import flagship.domain.tariffs.mix.Due;
import flagship.domain.tariffs.mix.Range;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

import static flagship.domain.cases.entities.enums.CallPurpose.*;
import static flagship.domain.cases.entities.enums.ShipType.*;
import static flagship.domain.tariffs.PortArea.*;
import static flagship.domain.tariffs.Tariff.MAX_GT;
import static flagship.domain.tariffs.Tariff.MIN_GT;

@Component
public class StateDuesTariffsInitializer {

  public static void initializeTariffs(
          final TonnageDueTariff tonnageDueTariff,
          final WharfDueTariff wharfDueTariff,
          final CanalDueTariff canalDueTariff,
          final LightDueTariff lightDueTariff,
          MarpolDueTariff marpolDueTariff,
          BoomContainmentTariff boomContainmentTariff, final SailingPermissionTariff sailingPermissionTariff) {
    initializeTonnageDueTariff(tonnageDueTariff);
    initializeWharfDueTariff(wharfDueTariff);
    initializeCanalDueTariff(canalDueTariff);
    initializeLightDueTariff(lightDueTariff);
    initializeMarpolDueTariff(marpolDueTariff);
    initializeBoomContainmentTariff(boomContainmentTariff);
    initializeSailingPermissionTariff(sailingPermissionTariff);
  }

  private static void initializeSailingPermissionTariff(final SailingPermissionTariff sailingPermissionTariff) {
    sailingPermissionTariff.setSailingPermissionDue(BigDecimal.valueOf(50.00));
  }

  private static void initializeTonnageDueTariff(final TonnageDueTariff tonnageDueTariff) {

    final Map<PortArea, Due> tonnageDuesByPortArea = new EnumMap<>(PortArea.class);
    tonnageDuesByPortArea.put(FIRST, new Due(0.55));
    tonnageDuesByPortArea.put(SECOND, new Due(0.40));
    tonnageDuesByPortArea.put(THIRD, new Due(0.55));
    tonnageDuesByPortArea.put(FOURTH, new Due(0.55));

    final Map<ShipType, Due> tonnageDuesByShipType = new EnumMap<>(ShipType.class);
    tonnageDuesByShipType.put(OIL_TANKER, new Due(0.50));
    tonnageDuesByShipType.put(RECREATIONAL, new Due(0.10));
    tonnageDuesByShipType.put(MILITARY, new Due(0.25));
    tonnageDuesByShipType.put(SPECIAL, new Due(0.50));

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

    final Set<ShipType> shipTypesNotEligibleForDiscount =
        EnumSet.of(RECREATIONAL, MILITARY, SPECIAL);

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
  }

  private static void initializeWharfDueTariff(final WharfDueTariff wharfDueTariff) {

    final Map<ShipType, Due> wharfDuesByShipType = new EnumMap<>(ShipType.class);
    wharfDuesByShipType.put(MILITARY, new Due(0.5));
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

    wharfDueTariff.setDefaultWharfDue(new Due(0.10));
  }

  private static void initializeCanalDueTariff(final CanalDueTariff canalDueTariff) {

    final Map<PortArea, Due> canalDuesByPortArea = new EnumMap<>(PortArea.class);
    canalDuesByPortArea.put(FIRST, new Due(0.04));
    canalDuesByPortArea.put(SECOND, new Due(0.13));
    canalDuesByPortArea.put(THIRD, new Due(0.04));
    canalDuesByPortArea.put(FOURTH, new Due(0.07));
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

    final Map<Range, Due> lightDuesByGrossTonnage = new LinkedHashMap<>();
    lightDuesByGrossTonnage.put(new Range(41, 500), new Due(15.00));
    lightDuesByGrossTonnage.put(new Range(501, 1000), new Due(40.00));
    lightDuesByGrossTonnage.put(new Range(1001, 5000), new Due(70.00));
    lightDuesByGrossTonnage.put(new Range(5001, 10000), new Due(110.00));
    lightDuesByGrossTonnage.put(new Range(10001, MAX_GT), new Due(150.00));

    final Map<ShipType, Due> lightDuesByShipType = new EnumMap<>(ShipType.class);
    lightDuesByShipType.put(MILITARY, new Due(0.15));

    final Map<ShipType, BigDecimal> discountCoefficientsByShipType = new EnumMap<>(ShipType.class);
    discountCoefficientsByShipType.put(PASSENGER, BigDecimal.valueOf(0.5));

    final Set<ShipType> shipTypesNotEligibleForDiscount = EnumSet.of(MILITARY);
    lightDueTariff.setShipTypesNotEligibleForDiscount(
        Collections.unmodifiableSet(shipTypesNotEligibleForDiscount));

    lightDueTariff.setLightDuesByGrossTonnage(Collections.unmodifiableMap(lightDuesByGrossTonnage));
    lightDueTariff.setLightDuesPerTonByShipType(Collections.unmodifiableMap(lightDuesByShipType));
    lightDueTariff.setDiscountCoefficientsByShipType(
        Collections.unmodifiableMap(discountCoefficientsByShipType));
    lightDueTariff.setCallCountThreshold(4);
    lightDueTariff.setCallCountDiscountCoefficient(BigDecimal.valueOf(0.7));
    lightDueTariff.setLightDueMaximumValue(BigDecimal.valueOf(150.00));
  }

  private static void initializeMarpolDueTariff(MarpolDueTariff marpolDueTariff) {

    Map<Range, Due> freeSewageDisposalQuantitiesPerGT = new LinkedHashMap<>();

    freeSewageDisposalQuantitiesPerGT.put(new Range(MIN_GT, 2000), new Due(8.6508));
    freeSewageDisposalQuantitiesPerGT.put(new Range(2001, 3000), new Due(10.5314));
    freeSewageDisposalQuantitiesPerGT.put(new Range(3001, 6000), new Due(10.5315));
    freeSewageDisposalQuantitiesPerGT.put(new Range(6001, 10000), new Due(15.0448));
    freeSewageDisposalQuantitiesPerGT.put(new Range(10001, 20000), new Due(19.5583));
    freeSewageDisposalQuantitiesPerGT.put(new Range(20001, 30000), new Due(21.0628));
    freeSewageDisposalQuantitiesPerGT.put(new Range(30001, 40000), new Due(27.0807));
    freeSewageDisposalQuantitiesPerGT.put(new Range(40001, 50000), new Due(28.5852));
    freeSewageDisposalQuantitiesPerGT.put(new Range(50001, MAX_GT), new Due(30.0897));

    Map<Range, Due> freeGarbageDisposalQuantitiesPerGT = new LinkedHashMap<>();

    freeGarbageDisposalQuantitiesPerGT.put(new Range(MIN_GT, 2000), new Due(10.72));
    freeGarbageDisposalQuantitiesPerGT.put(new Range(2001, 3000), new Due(11.43));
    freeGarbageDisposalQuantitiesPerGT.put(new Range(3001, 6000), new Due(12.86));
    freeGarbageDisposalQuantitiesPerGT.put(new Range(6001, 10000), new Due(23.58));
    freeGarbageDisposalQuantitiesPerGT.put(new Range(10001, 20000), new Due(26.43));
    freeGarbageDisposalQuantitiesPerGT.put(new Range(20001, 30000), new Due(32.15));
    freeGarbageDisposalQuantitiesPerGT.put(new Range(30001, 40000), new Due(50.01));
    freeGarbageDisposalQuantitiesPerGT.put(new Range(40001, 50000), new Due(71.45));
    freeGarbageDisposalQuantitiesPerGT.put(new Range(50001, MAX_GT), new Due(107.17));

    Map<Range, Due[]> marpolDuePerGrossTonnage = new LinkedHashMap<>();
    marpolDuePerGrossTonnage.put(
        new Range(MIN_GT, 2000), new Due[] {new Due(35.00), new Due(5.00), new Due(25.00)});
    marpolDuePerGrossTonnage.put(
        new Range(2001, 3000), new Due[] {new Due(100.00), new Due(10.00), new Due(50.00)});
    marpolDuePerGrossTonnage.put(
        new Range(3001, 6000), new Due[] {new Due(130.00), new Due(15.00), new Due(65.00)});
    marpolDuePerGrossTonnage.put(
        new Range(6001, 10000), new Due[] {new Due(200.00), new Due(20.00), new Due(85.00)});
    marpolDuePerGrossTonnage.put(
        new Range(10001, 20000), new Due[] {new Due(220.00), new Due(25.00), new Due(120.00)});
    marpolDuePerGrossTonnage.put(
        new Range(20001, 30000), new Due[] {new Due(250.00), new Due(30.00), new Due(180.00)});
    marpolDuePerGrossTonnage.put(
        new Range(30001, 40000), new Due[] {new Due(450.00), new Due(35.00), new Due(250.00)});
    marpolDuePerGrossTonnage.put(
        new Range(40001, 50000), new Due[] {new Due(700.00), new Due(40.00), new Due(400.00)});
    marpolDuePerGrossTonnage.put(
        new Range(50001, MAX_GT), new Due[] {new Due(900.00), new Due(50.00), new Due(550.00)});

    marpolDueTariff.setFreeSewageDisposalQuantitiesPerGrossTonnage(
        Collections.unmodifiableMap(freeSewageDisposalQuantitiesPerGT));
    marpolDueTariff.setFreeGarbageDisposalQuantitiesPerGrossTonnage(
        Collections.unmodifiableMap(freeGarbageDisposalQuantitiesPerGT));
    marpolDueTariff.setMarpolDuePerGrossTonnage(
        Collections.unmodifiableMap(marpolDuePerGrossTonnage));

    marpolDueTariff.setOdessosFixedMarpolDue(BigDecimal.valueOf(120.00));
    marpolDueTariff.setOdessosFreeGarbageDisposalQuantity(BigDecimal.valueOf(10.00));
    marpolDueTariff.setOdessosFreeSewageDisposalQuantity(BigDecimal.valueOf(1.00));
  }

  private static void initializeBoomContainmentTariff(BoomContainmentTariff boomContainmentTariff) {

    Map<Range, Due> boomContainmentDuePerGrossTonnage = new LinkedHashMap<>();
    boomContainmentDuePerGrossTonnage.put(new Range(MIN_GT, 3000), new Due(700.00));
    boomContainmentDuePerGrossTonnage.put(new Range(3001, 5000), new Due(1000.00));
    boomContainmentDuePerGrossTonnage.put(new Range(5001, 10000), new Due(1500.00));
    boomContainmentDuePerGrossTonnage.put(new Range(10001, 20000), new Due(1800.00));
    boomContainmentDuePerGrossTonnage.put(new Range(20001, MAX_GT), new Due(2500.00));

    boomContainmentTariff.setBoomContainmentDuePerGrossTonnage(
        Collections.unmodifiableMap(boomContainmentDuePerGrossTonnage));
  }
}
