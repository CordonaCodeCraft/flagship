package flagship.domain.calculators.statedues;

import flagship.domain.calculators.tariffs.Tariff;
import flagship.domain.calculators.tariffs.stateduestariffs.WharfDueTariff;
import flagship.domain.cases.dto.PdaCase;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
public class WharfDueCalculator extends StateDueCalculator<PdaCase, Tariff> {

  private PdaCase source;
  private WharfDueTariff tariff;

  @Override
  public void set(final PdaCase source, final Tariff tariff) {
    this.source = source;
    this.tariff = (WharfDueTariff) tariff;
  }

  @Override
  public BigDecimal calculate() {
    return calculateDueAfterDiscount(calculateBaseDue(), evaluateDiscountCoefficient());
  }

  @Override
  protected BigDecimal calculateBaseDue() {

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
  protected BigDecimal evaluateDiscountCoefficient() {

    BigDecimal discountCoefficient = BigDecimal.ZERO;

    if (isEligibleForDiscount()) {
      discountCoefficient =
          tariff.getDiscountCoefficientsByCallPurpose().get(source.getCallPurpose());
    }
    return discountCoefficient;
  }

  private boolean isEligibleForDiscount() {
    return !tariff.getShipTypesNotEligibleForDiscount().contains(source.getShip().getType())
        && tariff.getDiscountCoefficientsByCallPurpose().containsKey(source.getCallPurpose());
  }
}
