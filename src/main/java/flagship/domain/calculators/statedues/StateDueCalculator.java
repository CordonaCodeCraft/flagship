package flagship.domain.calculators.statedues;

import flagship.domain.calculators.DueCalculator;

import java.math.BigDecimal;

public abstract class StateDueCalculator<S, P> implements DueCalculator<S, P> {

  public BigDecimal calculateFor(S source, P properties) {
    BigDecimal baseDue = calculateBaseDue(source, properties);
    BigDecimal discountCoefficient = evaluateDiscountCoefficient(source, properties);
    return calculateDueAfterDiscount(baseDue, discountCoefficient);
  }

  protected abstract BigDecimal calculateBaseDue(S source, P properties);

  protected abstract BigDecimal evaluateDiscountCoefficient(S source, P properties);

  protected BigDecimal calculateDueAfterDiscount(
      final BigDecimal due, final BigDecimal discountCoefficient) {
    if (discountCoefficient.doubleValue() > 0) {
      BigDecimal discount = due.multiply(discountCoefficient);
      return due.subtract(discount);
    } else {
      return due;
    }
  }
}
