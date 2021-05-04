package flagship.domain.calculators.stateduescalculators;

import flagship.domain.calculators.DueCalculator;

import java.math.BigDecimal;

public abstract class StateDueCalculator<S, T> implements DueCalculator<S, T> {

  protected abstract BigDecimal calculateBaseDue();

  protected abstract BigDecimal evaluateDiscountCoefficient();

  protected BigDecimal calculateDueAfterDiscount(
      final BigDecimal due, final BigDecimal discountCoefficient) {
    if (discountCoefficient.doubleValue() > 0) {
      return due.subtract(due.multiply(discountCoefficient));
    } else {
      return due;
    }
  }
}
