package flagship.domain.calculators.stateduescalculators;

import flagship.domain.calculators.StateDueCalculator;
import flagship.domain.cases.dto.PdaCase;
import flagship.domain.tariffs.Tariff;
import flagship.domain.tariffs.stateduestariffs.WharfDueTariff;
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
    return getDueAfterDiscount(getBaseDue(), getDiscountCoefficient());
  }

  @Override
  protected BigDecimal getBaseDue() {

    final BigDecimal lengthOverall =
        BigDecimal.valueOf(Math.ceil(source.getShip().getLengthOverall().doubleValue()));

    final BigDecimal wharfDuePerMeter =
        tariff
            .getWharfDuesByShipType()
            .getOrDefault(source.getShip().getType(), tariff.getDefaultWharfDue())
            .getBase();

    final BigDecimal alongsideHoursExpected =
        BigDecimal.valueOf(source.getAlongsideDaysExpected() * 24);

    return alongsideHoursExpected.multiply(lengthOverall.multiply(wharfDuePerMeter));
  }

  @Override
  protected BigDecimal getDiscountCoefficient() {

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
