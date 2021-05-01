package flagship.domain.calculators.statedues;

import flagship.domain.calculators.tariffs.Tariff;
import flagship.domain.calculators.tariffs.stateduestariffs.CanalDueTariff;
import flagship.domain.cases.dto.PdaCase;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

import static flagship.domain.cases.entities.enums.ShipType.CONTAINER;

@NoArgsConstructor
public class CanalDueCalculator extends StateDueCalculator<PdaCase, Tariff> {

  private PdaCase source;
  private CanalDueTariff tariff;

  @Override
  public void set(final PdaCase source, final Tariff tariff) {
    this.source = source;
    this.tariff = (CanalDueTariff) tariff;
  }

  @Override
  public BigDecimal calculate() {
    return calculateDueAfterDiscount(calculateBaseDue(), evaluateDiscountCoefficient());
  }

  @Override
  protected BigDecimal calculateBaseDue() {
    return source
        .getShip()
        .getGrossTonnage()
        .multiply(tariff.getCanalDuesByPortArea().get(source.getPort().getArea()));
  }

  @Override
  protected BigDecimal evaluateDiscountCoefficient() {

    BigDecimal discountCoefficient = BigDecimal.ZERO;

    if (isEligibleForDiscount()) {
      if (source.getShip().getType() == CONTAINER) {
        if (satisfiesCallCountThreshold()) {
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
