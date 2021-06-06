package flagship.domain.calculation.calculators;

import java.math.BigDecimal;

public abstract class StateDueCalculator implements DueCalculator {

  protected abstract BigDecimal getBaseDue();

  protected abstract BigDecimal getDiscountCoefficient();

  protected BigDecimal getDueAfterDiscount(
      final BigDecimal due, final BigDecimal discountCoefficient) {
    if (discountCoefficient.doubleValue() > 0) {
      return due.subtract(due.multiply(discountCoefficient));
    } else {
      return due;
    }
  }
}
