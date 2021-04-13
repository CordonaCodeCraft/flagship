package flagship.domain.utils.calculators.statedues;

import java.math.BigDecimal;

public abstract class StateDueCalculator<S, P> {

    public abstract BigDecimal calculate(S source, P properties);

    protected abstract BigDecimal calculateBaseDue(S source, P properties);

    protected abstract BigDecimal evaluateDiscountCoefficient(S source, P properties);

    protected BigDecimal calculateDueTotal(BigDecimal baseDue, BigDecimal discountCoefficient) {
        if (discountCoefficient.doubleValue() > 0) {
            BigDecimal discount = baseDue.multiply(discountCoefficient);
            return baseDue.subtract(discount);
        } else {
            return baseDue;
        }
    }
}
