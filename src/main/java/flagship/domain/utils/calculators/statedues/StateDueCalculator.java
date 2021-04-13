package flagship.domain.utils.calculators.statedues;

import java.math.BigDecimal;

public abstract class StateDueCalculator<S, P> {

    public abstract BigDecimal calculate(S source, P properties);

    protected abstract BigDecimal calculateBaseDue(S source, P properties);

    protected abstract double evaluateDiscountCoefficient(S source, P properties);

    protected BigDecimal calculateDueTotal(BigDecimal baseDue, Double discountCoefficient) {
        if (discountCoefficient > 0) {
            BigDecimal discount = BigDecimal.valueOf(baseDue.doubleValue() * discountCoefficient);
            return baseDue.subtract(discount);
        } else {
            return baseDue;
        }
    }
}
