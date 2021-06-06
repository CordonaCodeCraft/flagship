package flagship.domain.calculation.tariffs;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;

import static flagship.domain.calculation.calculators.DueCalculator.CalculatorType;

@Getter
@Setter
@RequiredArgsConstructor
@Component
public class TariffsFactory {

  private Map<CalculatorType, Tariff> tariffs = new EnumMap<>(CalculatorType.class);

  public Tariff getTariff(CalculatorType calculatorType) {
    return tariffs.get(calculatorType);
  }
}
