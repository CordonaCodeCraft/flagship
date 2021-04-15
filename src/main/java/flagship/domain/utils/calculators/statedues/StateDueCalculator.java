package flagship.domain.utils.calculators.statedues;

import flagship.domain.utils.calculators.DueCalculator;

import java.math.BigDecimal;

public abstract class StateDueCalculator<S, P> implements DueCalculator<S, P> {

    public BigDecimal calculate(S source, P properties) {
        BigDecimal due = calculateDue(source, properties);
        BigDecimal discountCoefficient = evaluateDiscountCoefficient(source, properties);
        return calculateDueAfterDiscount(due, discountCoefficient);
    }

    protected abstract BigDecimal calculateDue(S source, P properties);

    protected abstract BigDecimal evaluateDiscountCoefficient(S source, P properties);

    protected BigDecimal calculateDueAfterDiscount(BigDecimal due, BigDecimal discountCoefficient) {
        if (discountCoefficient.doubleValue() > 0) {
            BigDecimal discount = due.multiply(discountCoefficient);
            return due.subtract(discount);
        } else {
            return due;
        }
    }
}
