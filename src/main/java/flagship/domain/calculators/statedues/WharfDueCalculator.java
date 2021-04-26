package flagship.domain.calculators.statedues;

import flagship.domain.calculators.tariffs.stateduestariffs.WharfDueTariff;
import flagship.domain.cases.dto.PdaCase;

import java.math.BigDecimal;

public class WharfDueCalculator extends StateDueCalculator<PdaCase, WharfDueTariff> {

  @Override
  protected BigDecimal calculateBaseDue(final PdaCase source, final WharfDueTariff tariff) {

    final BigDecimal lengthOverall =
        BigDecimal.valueOf(Math.ceil(source.getShip().getLengthOverall().doubleValue()));

    final BigDecimal wharfDuePerMeter =
        tariff
            .getWharfDuesByShipType()
            .getOrDefault(source.getShip().getType(), tariff.getDefaultWharfDue());

    final BigDecimal wharfDuePerLengthOverall = lengthOverall.multiply(wharfDuePerMeter);
    final BigDecimal alongsideHoursExpected =
        BigDecimal.valueOf(source.getAlongsideDaysExpected() * 24);

    return alongsideHoursExpected.multiply(wharfDuePerLengthOverall);
  }

  @Override
  protected BigDecimal evaluateDiscountCoefficient(
      final PdaCase source, final WharfDueTariff tariff) {

    BigDecimal discountCoefficient = BigDecimal.ZERO;

    if (isEligibleForDiscount(source, tariff)) {
      discountCoefficient =
          tariff.getDiscountCoefficientsByCallPurpose().get(source.getCallPurpose());
    }
    return discountCoefficient;
  }

  private boolean isEligibleForDiscount(PdaCase source, WharfDueTariff tariff) {
    return !tariff.getShipTypesNotEligibleForDiscount().contains(source.getShip().getType())
        && tariff.getDiscountCoefficientsByCallPurpose().containsKey(source.getCallPurpose());
  }
}
