package flagship.domain.calculation.calculators.state;

import flagship.domain.calculation.calculators.StateDueCalculator;
import flagship.domain.calculation.tariffs.Tariff;
import flagship.domain.calculation.tariffs.state.CanalDueTariff;
import flagship.domain.caze.model.PdaCase;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

import static flagship.domain.ship.entity.Ship.ShipType.CONTAINER;

@NoArgsConstructor
public class CanalDueCalculator extends StateDueCalculator {

  private PdaCase source;
  private CanalDueTariff tariff;

  @Override
  public void set(final PdaCase source, final Tariff tariff) {
    this.source = source;
    this.tariff = (CanalDueTariff) tariff;
  }

  @Override
  public BigDecimal calculate() {
    return getDueAfterDiscount(getBaseDue(), getDiscountCoefficient());
  }

  @Override
  protected BigDecimal getBaseDue() {
    return source
        .getShip()
        .getGrossTonnage()
        .multiply(tariff.getCanalDuesByPortArea().get(source.getPort().getPortArea()).getBase());
  }

  @Override
  protected BigDecimal getDiscountCoefficient() {

    BigDecimal discountCoefficient = BigDecimal.ZERO;

    if (isEligibleForDiscount()) {
      if (source.getShip().getType() == CONTAINER) {
        if (satisfiesCallCountThreshold()) {
          discountCoefficient =
              discountCoefficient.max(
                  tariff
                      .getDiscountCoefficientsByPortAreaPerCallCountForContainers()
                      .get(source.getPort().getPortArea()));
        }
        discountCoefficient =
            discountCoefficient.max(
                tariff
                    .getDiscountCoefficientsByPortAreaForContainers()
                    .get(source.getPort().getPortArea()));
      } else {
        if (satisfiesCallCountThreshold()) {
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

  private boolean isEligibleForDiscount() {
    return !tariff.getShipTypesNotEligibleForDiscount().contains(source.getShip().getType());
  }

  private boolean satisfiesCallCountThreshold() {
    return source.getCallCount() >= tariff.getCallCountThreshold();
  }
}
