package flagship.domain.utils.calculators.statedues;

import flagship.domain.cases.entities.Case;

import java.math.BigDecimal;

public abstract class StateDueCalculator<S, P> {

    public abstract BigDecimal calculate(S source, P properties);

    protected abstract BigDecimal calculateDueTotal(S source, P properties);

    protected abstract BigDecimal evaluateDiscountCoefficient(S source, P properties);

    protected BigDecimal calculateDueFinal(BigDecimal baseDue, BigDecimal discountCoefficient) {
        if (discountCoefficient.doubleValue() > 0) {
            BigDecimal discount = baseDue.multiply(discountCoefficient);
            return baseDue.subtract(discount);
        } else {
            return baseDue;
        }
    }
}
