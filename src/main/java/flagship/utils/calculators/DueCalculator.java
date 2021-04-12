package flagship.utils.calculators;

import java.math.BigDecimal;

public interface DueCalculator<S, P> {

    BigDecimal calculate(S source, P properties);

}
