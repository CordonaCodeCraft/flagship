package flagship.bootstrap.initializers;

import flagship.domain.base.due.tuple.Due;
import flagship.domain.calculation.tariffs.state.CanalDueTariff;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

import static flagship.domain.caze.model.request.resolvers.PortAreaResolver.PortArea;
import static flagship.domain.caze.model.request.resolvers.PortAreaResolver.PortArea.*;
import static flagship.domain.ship.entity.Ship.ShipType;
import static flagship.domain.ship.entity.Ship.ShipType.NAVY;
import static flagship.domain.ship.entity.Ship.ShipType.PASSENGER;

@Component
@Slf4j
@RequiredArgsConstructor
public class CanalDueTariffInitializer {

  public static CanalDueTariff getTariff() {

    final CanalDueTariff canalDueTariff = new CanalDueTariff();

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

    final Set<ShipType> shipTypesNotEligibleForDiscount = EnumSet.of(NAVY);
    canalDueTariff.setShipTypesNotEligibleForDiscount(
        Collections.unmodifiableSet(shipTypesNotEligibleForDiscount));

    canalDueTariff.setCallCountThreshold(3);
    canalDueTariff.setDefaultCallCountDiscountCoefficient(BigDecimal.valueOf(0.8));

    log.info("Canal due tariff initialized");

    return canalDueTariff;
  }
}
