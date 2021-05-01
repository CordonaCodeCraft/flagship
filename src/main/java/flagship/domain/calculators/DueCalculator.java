package flagship.domain.calculators;

import java.math.BigDecimal;

public interface DueCalculator<S, T> {

  void set(S source, T tariff);

  BigDecimal calculate();
}
