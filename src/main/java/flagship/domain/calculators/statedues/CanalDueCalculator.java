package flagship.domain.calculators.statedues;

import flagship.domain.calculators.tariffs.stateduestariffs.CanalDueTariff;
import flagship.domain.cases.dto.PdaCase;

import java.math.BigDecimal;

import static flagship.domain.cases.entities.enums.ShipType.CONTAINER;

public class CanalDueCalculator extends StateDueCalculator<PdaCase, CanalDueTariff> {

  @Override
  protected BigDecimal calculateBaseDue(final PdaCase source, final CanalDueTariff tariff) {
    return source
        .getShip()
        .getGrossTonnage()
        .multiply(tariff.getCanalDuesByPortArea().get(source.getPort().getArea()));
  }

  @Override
  protected BigDecimal evaluateDiscountCoefficient(
      final PdaCase source, final CanalDueTariff tariff) {

    BigDecimal discountCoefficient = BigDecimal.ZERO;

    if (isEligibleForDiscount(source, tariff)) {
      if (source.getShip().getType() == CONTAINER) {
        if (satisfiesCallCountThreshold(source, tariff)) {
          discountCoefficient =
              discountCoefficient.max(
                  tariff
                      .getDiscountCoefficientsByPortAreaPerCallCountForContainers()
                      .get(source.getPort().getArea()));
        }
        discountCoefficient =
            discountCoefficient.max(
                tariff
                    .getDiscountCoefficientsByPortAreaForContainers()
                    .get(source.getPort().getArea()));
      } else {
        if (satisfiesCallCountThreshold(source, tariff)) {
          discountCoefficient =
              discountCoefficient.max(tariff.getDefaultCallCountDiscountCoefficient());
        }
        if (tariff.getDiscountCoefficientByShipType().containsKey(source.getShip().getType())) {
          discountCoefficient =
              discountCoefficient.max(
                  tariff.getDiscountCoefficientByShipType().get(source.getShip().getType()));
        }
      }
    }

    return discountCoefficient;
  }

  private boolean isEligibleForDiscount(PdaCase source, CanalDueTariff tariff) {
    return !tariff.getShipTypesNotEligibleForDiscount().contains(source.getShip().getType());
  }

  private boolean satisfiesCallCountThreshold(PdaCase source, CanalDueTariff tariff) {
    return source.getCallCount() >= tariff.getCallCountThreshold();
  }
}
