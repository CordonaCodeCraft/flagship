package flagship.domain;

import flagship.domain.calculators.DueCalculator;
import flagship.domain.cases.dto.PdaCase;
import flagship.domain.cases.entities.ProformaDisbursementAccount;
import flagship.domain.cases.entities.Warning;
import flagship.domain.tariffs.Tariff;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Stream;

import static flagship.domain.calculators.DueCalculator.CalculatorType;
import static flagship.domain.calculators.DueCalculator.CalculatorType.values;

@Setter
@RequiredArgsConstructor
@Component
public class PdaComposer {

  private final PdaCase source;
  private final TariffsFactory tariffsFactory;

  public ProformaDisbursementAccount composePda() {

    final ProformaDisbursementAccount pda = new ProformaDisbursementAccount();

    initializeDues(pda);
    initializeWarnings(pda);
    initializeProfit(pda);

    return pda;
  }

  private void initializeProfit(final ProformaDisbursementAccount pda) {

    BigDecimal agencyDuesTotal =
        Stream.of(
                pda.getBankExpensesDue(),
                pda.getCarsDue(),
                pda.getCommunicationsDue(),
                pda.getBankExpensesDue(),
                pda.getOvertimeDue())
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    System.out.println();

//    pda.setProfitExpected(agencyDuesTotal.add(agencyDuesTotal.multiply(commission)));
  }

  private void initializeDues(final ProformaDisbursementAccount pda) {
    Arrays.stream(pda.getClass().getDeclaredFields())
        .filter(field -> field.getName().contains("Due"))
        .forEach(
            field -> {
              try {
                field.setAccessible(true);
                initializeField(pda, field);
              } catch (IllegalAccessException e) {
                e.printStackTrace();
              }
            });
  }

  private void initializeField(final ProformaDisbursementAccount pda, final Field field)
      throws IllegalAccessException {
    final CalculatorType calculatorType = getCalculatorType(field);
    final Tariff tariff = tariffsFactory.getTariff(calculatorType);
    final DueCalculator calculator = CalculatorFactory.getCalculator(calculatorType);
    calculator.set(source, tariff);
    field.set(pda, calculator.calculate());
  }

  private CalculatorType getCalculatorType(final Field field) {
    return Arrays.stream(values())
        .filter(calculator -> calculator.getType().equals(field.getName()))
        .findFirst()
        .get();
  }

  private void initializeWarnings(final ProformaDisbursementAccount pda) {
    PdaWarningsGenerator pdaWarningsGenerator = new PdaWarningsGenerator(source, tariffsFactory);
    Set<Warning> warnings = pdaWarningsGenerator.generateWarnings();
    pda.setWarnings(warnings);
  }
}
