package flagship.domain.calculators.stateduescalculators;

import flagship.domain.calculators.PrivateDueCalculator;
import flagship.domain.cases.dto.PdaCase;
import flagship.domain.tariffs.Tariff;
import flagship.domain.tariffs.stateduestariffs.LightDueTariff;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

// todo: Validate that gross tonnage is no less than 41, otherwise fixed due per year should be
// applied

@NoArgsConstructor
public class LightDueCalculator extends PrivateDueCalculator<PdaCase, Tariff> {

  private LightDueTariff tariff;

  @Override
  public void set(final PdaCase source, final Tariff tariff) {
    super.source = source;
    this.tariff = (LightDueTariff) tariff;
  }

  @Override
  public BigDecimal calculate() {

    BigDecimal lightDue;

    if (lightDueIsDependantOnShipType()) {
      final BigDecimal due =
          tariff
              .getLightDuesPerTonByShipType()
              .get(source.getShip().getType())
              .getBase()
              .multiply(source.getShip().getGrossTonnage());
      lightDue =
          due.doubleValue() <= tariff.getLightDueMaximumValue().doubleValue()
              ? due
              : tariff.getLightDueMaximumValue();
    } else {
      lightDue = getBaseDue(tariff.getLightDuesByGrossTonnage());
    }

    final BigDecimal discountCoefficient = evaluateDiscountCoefficient();

    if (discountCoefficient.doubleValue() > 0) {
      lightDue = lightDue.subtract(lightDue.multiply(discountCoefficient));
    }

    return lightDue;
  }

  private boolean lightDueIsDependantOnShipType() {
    return tariff.getLightDuesPerTonByShipType().containsKey(source.getShip().getType());
  }

  protected BigDecimal evaluateDiscountCoefficient() {

    BigDecimal discountCoefficient = BigDecimal.ZERO;

    if (shipTypeIsEligibleForDiscount()) {
      if (isEligibleForCallCountDiscount()) {
        discountCoefficient = discountCoefficient.max(tariff.getCallCountDiscountCoefficient());
      }
      if (isEligibleForShipTypeDiscount()) {
        discountCoefficient =
            discountCoefficient.max(
                tariff.getDiscountCoefficientsByShipType().get(source.getShip().getType()));
      }
    }

    return discountCoefficient;
  }

  private boolean shipTypeIsEligibleForDiscount() {
    return !tariff.getShipTypesNotEligibleForDiscount().contains(source.getShip().getType());
  }

  private boolean isEligibleForCallCountDiscount() {
    return source.getCallCount() >= tariff.getCallCountThreshold();
  }

  private boolean isEligibleForShipTypeDiscount() {
    return tariff.getDiscountCoefficientsByShipType().containsKey(source.getShip().getType());
  }
}
