package flagship.domain.calculators.stateduescalculators;

import flagship.domain.tariffs.Tariff;
import flagship.domain.tariffs.stateduestariffs.LightDueTariff;
import flagship.domain.cases.dto.PdaCase;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

// todo: Validate that gross tonnage is no less than 41, otherwise fixed due per year should be
// applied

@NoArgsConstructor
public class LightDueCalculator extends StateDueCalculator<PdaCase, Tariff> {

  private PdaCase source;
  private LightDueTariff tariff;

  @Override
  public void set(final PdaCase source, final Tariff tariff) {
    this.source = source;
    this.tariff = (LightDueTariff) tariff;
  }

  @Override
  public BigDecimal calculate() {
    return calculateDueAfterDiscount(calculateBaseDue(), evaluateDiscountCoefficient());
  }

  @Override
  protected BigDecimal calculateBaseDue() {

    BigDecimal lightDue;

    if (lightDueIsDependantOnShipType()) {
      lightDue =
          source
              .getShip()
              .getGrossTonnage()
              .multiply(tariff.getLightDuesPerTonByShipType().get(source.getShip().getType()));
    } else {
      lightDue =
          tariff.getLightDuesByGrossTonnage().entrySet().stream()
              .filter(this::shipGrossTonnageInRange)
              .findFirst()
              .map(Map.Entry::getKey)
              .orElse(tariff.getLightDueMaximumValue());
    }
    return lightDue.doubleValue() <= tariff.getLightDueMaximumValue().doubleValue()
        ? lightDue
        : tariff.getLightDueMaximumValue();
  }

  private boolean lightDueIsDependantOnShipType() {
    return tariff.getLightDuesPerTonByShipType().containsKey(source.getShip().getType());
  }

  @Override
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

  private boolean shipGrossTonnageInRange(final Map.Entry<BigDecimal, Integer[]> entry) {
    return source.getShip().getGrossTonnage().intValue() >= entry.getValue()[0]
        && source.getShip().getGrossTonnage().intValue() <= entry.getValue()[1];
  }
}
