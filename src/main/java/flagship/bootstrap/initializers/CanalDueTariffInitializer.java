package flagship.bootstrap.initializers;

import flagship.domain.calculation.tariffs.state.CanalDueTariff;
import flagship.domain.tuples.due.Due;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

import static flagship.domain.caze.model.createrequest.resolvers.PortAreaResolver.PortArea;
import static flagship.domain.caze.model.createrequest.resolvers.PortAreaResolver.PortArea.*;
import static flagship.domain.ship.entity.Ship.ShipType;
import static flagship.domain.ship.entity.Ship.ShipType.NAVY;
import static flagship.domain.ship.entity.Ship.ShipType.PASSENGER;

@Slf4j
public class CanalDueTariffInitializer extends Initializer {

  public static CanalDueTariff getTariff() {

    final Map<PortArea, Due> canalDuesByPortArea = new EnumMap<>(PortArea.class);

    canalDuesByPortArea.put(FIRST, new Due(0.04));
    canalDuesByPortArea.put(SECOND, new Due(0.13));
    canalDuesByPortArea.put(THIRD, new Due(0.04));
    canalDuesByPortArea.put(FOURTH, new Due(0.07));

    final Map<ShipType, BigDecimal> discountCoefficientByShipType = new EnumMap<>(ShipType.class);

    discountCoefficientByShipType.put(PASSENGER, BigDecimal.valueOf(0.5));

    final Map<PortArea, BigDecimal> discountCoefficientsByPortAreaForContainers =
        new EnumMap<>(PortArea.class);

    discountCoefficientsByPortAreaForContainers.put(FIRST, BigDecimal.valueOf(0.25));
    discountCoefficientsByPortAreaForContainers.put(SECOND, BigDecimal.valueOf(0.74));
    discountCoefficientsByPortAreaForContainers.put(THIRD, BigDecimal.valueOf(0.25));
    discountCoefficientsByPortAreaForContainers.put(FOURTH, BigDecimal.valueOf(0.74));

    final Map<PortArea, BigDecimal> discountCoefficientsByPortAreaPerCallCountForContainers =
        new EnumMap<>(PortArea.class);

    discountCoefficientsByPortAreaPerCallCountForContainers.put(FIRST, BigDecimal.valueOf(0.20));
    discountCoefficientsByPortAreaPerCallCountForContainers.put(SECOND, BigDecimal.valueOf(0.59));
    discountCoefficientsByPortAreaPerCallCountForContainers.put(THIRD, BigDecimal.valueOf(0.20));
    discountCoefficientsByPortAreaPerCallCountForContainers.put(FOURTH, BigDecimal.valueOf(0.59));

    final Set<ShipType> shipTypesNotEligibleForDiscount = EnumSet.of(NAVY);

    final CanalDueTariff canalDueTariff = new CanalDueTariff();

    canalDueTariff.setCanalDuesByPortArea(withImmutableMap(canalDuesByPortArea));

    canalDueTariff.setDiscountCoefficientByShipType(
        withImmutableMap(discountCoefficientByShipType));

    canalDueTariff.setDiscountCoefficientsByPortAreaForContainers(
        withImmutableMap(discountCoefficientsByPortAreaForContainers));

    canalDueTariff.setDiscountCoefficientsByPortAreaPerCallCountForContainers(
        withImmutableMap(discountCoefficientsByPortAreaPerCallCountForContainers));

    canalDueTariff.setCallCountThreshold(3);

    canalDueTariff.setDefaultCallCountDiscountCoefficient(BigDecimal.valueOf(0.8));

    canalDueTariff.setShipTypesNotEligibleForDiscount(
        withImmutableSet(shipTypesNotEligibleForDiscount));

    log.info("Canal due tariff initialized");

    return canalDueTariff;
  }
}
