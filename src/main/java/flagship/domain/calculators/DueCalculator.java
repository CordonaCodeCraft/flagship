package flagship.domain.calculators;

import java.math.BigDecimal;

public interface DueCalculator<S, P> {

  BigDecimal calculateFor(S source, P properties);
}
