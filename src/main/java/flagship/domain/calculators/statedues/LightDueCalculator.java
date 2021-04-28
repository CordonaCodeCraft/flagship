package flagship.domain.calculators.statedues;

import flagship.domain.calculators.tariffs.stateduestariffs.LightDueTariff;
import flagship.domain.cases.dto.PdaCase;

import java.math.BigDecimal;
import java.util.Map;

public class LightDueCalculator extends StateDueCalculator<PdaCase, LightDueTariff> {

  // todo: Validate that gross tonnage is no less than 41, otherwise fixed due per year should be
  // applied.
  @Override
  protected BigDecimal calculateBaseDue(final PdaCase source, final LightDueTariff tariff) {

    BigDecimal lightDue;

    if (lightDueIsDependantOnShipType(source, tariff)) {
      lightDue =
          source
              .getShip()
              .getGrossTonnage()
              .multiply(tariff.getLightDuesPerTonByShipType().get(source.getShip().getType()));
    } else {
      lightDue =
          tariff.getLightDuesByGrossTonnage().entrySet().stream()
              .filter(
                  entry ->
                      source.getShip().getGrossTonnage().intValue() >= entry.getValue()[0]
                          && source.getShip().getGrossTonnage().intValue() <= entry.getValue()[1])
              .findFirst()
              .map(Map.Entry::getKey)
              .orElse(tariff.getLightDueMaximumValue());
    }
    return lightDue.doubleValue() <= tariff.getLightDueMaximumValue().doubleValue()
        ? lightDue
        : tariff.getLightDueMaximumValue();
  }

  private boolean lightDueIsDependantOnShipType(PdaCase source, LightDueTariff tariff) {
    return tariff.getLightDuesPerTonByShipType().containsKey(source.getShip().getType());
  }

  @Override
  protected BigDecimal evaluateDiscountCoefficient(
      final PdaCase source, final LightDueTariff tariff) {

    BigDecimal discountCoefficient = BigDecimal.ZERO;

    if (shipTypeIsEligibleForDiscount(source, tariff)) {
      if (isEligibleForCallCountDiscount(source, tariff)) {
        discountCoefficient = discountCoefficient.max(tariff.getCallCountDiscountCoefficient());
      }
      if (isEligibleForShipTypeDiscount(source, tariff)) {
        discountCoefficient =
            discountCoefficient.max(
                tariff.getDiscountCoefficientsByShipType().get(source.getShip().getType()));
      }
    }

    return discountCoefficient;
  }

  private boolean shipTypeIsEligibleForDiscount(PdaCase source, LightDueTariff tariff) {
    return !tariff.getShipTypesNotEligibleForDiscount().contains(source.getShip().getType());
  }

  private boolean isEligibleForCallCountDiscount(PdaCase source, LightDueTariff tariff) {
    return source.getCallCount() >= tariff.getCallCountThreshold();
  }

  private boolean isEligibleForShipTypeDiscount(PdaCase source, LightDueTariff tariff) {
    return tariff.getDiscountCoefficientsByShipType().containsKey(source.getShip().getType());
  }
}
