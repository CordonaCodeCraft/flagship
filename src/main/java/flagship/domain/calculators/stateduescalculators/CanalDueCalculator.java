package flagship.domain.calculators.stateduescalculators;

import flagship.domain.calculators.StateDueCalculator;
import flagship.domain.cases.dto.PdaCase;
import flagship.domain.tariffs.CanalDueTariff;
import flagship.domain.tariffs.Tariff;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

import static flagship.domain.cases.entities.enums.ShipType.CONTAINER;

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
