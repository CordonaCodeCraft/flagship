package flagship.domain;

import flagship.domain.calculators.DueCalculator;
import flagship.domain.cases.dto.PdaCase;
import flagship.domain.cases.entities.ProformaDisbursementAccount;
import flagship.domain.factories.CalculatorFactory;
import flagship.domain.factories.TariffsFactory;
import flagship.domain.tariffs.Tariff;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

import static flagship.domain.calculators.DueCalculator.CalculatorType;
import static flagship.domain.calculators.DueCalculator.CalculatorType.values;

@Component
@Getter
@Setter
@RequiredArgsConstructor
@PropertySource("classpath:application.yml")
public class PdaComposer {

  @Value("${flagship.commission-coefficient}")
  private BigDecimal commissionCoefficient;

  private PdaCase source;
  private TariffsFactory tariffsFactory;

  public ProformaDisbursementAccount composePda() {

    final ProformaDisbursementAccount pda = new ProformaDisbursementAccount();

    initializeDues(pda);
    initializeClientDiscount(pda);
    initializePayableTotal(pda);
    initializeTotalAfterDiscount(pda);
    initializeProfitExpected(pda);

    return pda;
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
    CalculatorFactory calculatorFactory = new CalculatorFactory();
    final DueCalculator calculator = calculatorFactory.getCalculator(calculatorType);
    calculator.set(source, tariff);
    field.set(pda, calculator.calculate());
  }

  private CalculatorType getCalculatorType(final Field field) {
    return Arrays.stream(values())
        .filter(calculator -> calculator.getType().equals(field.getName()))
        .findFirst()
        .get();
  }

  private void initializeClientDiscount(final ProformaDisbursementAccount pda) {

    if (Optional.ofNullable(source.getClientDiscountCoefficient()).isPresent()) {
      pda.setClientDiscount(
          getAgencyDuesTotal(pda).multiply(source.getClientDiscountCoefficient()));
    } else {
      pda.setClientDiscount(BigDecimal.ZERO);
    }
  }

  private void initializePayableTotal(final ProformaDisbursementAccount pda) {

    final BigDecimal stateAndServiceDuesTotal =
        Stream.of(
                pda.getTonnageDue(),
                pda.getWharfDue(),
                pda.getCanalDue(),
                pda.getLightDue(),
                pda.getMarpolDue(),
                pda.getMooringDue(),
                pda.getBoomContainmentDue(),
                pda.getSailingPermissionDue(),
                pda.getPilotageDue(),
                pda.getTugDue())
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    pda.setPayableTotal(stateAndServiceDuesTotal.add(getAgencyDuesTotal(pda)));
  }

  private void initializeTotalAfterDiscount(final ProformaDisbursementAccount pda) {
    pda.setTotalAfterDiscount(pda.getPayableTotal().subtract(pda.getClientDiscount()));
  }

  private void initializeProfitExpected(final ProformaDisbursementAccount pda) {
    pda.setProfitExpected(
        getAgencyDuesTotal(pda).add(pda.getTugDue().multiply(commissionCoefficient)));
  }

  private BigDecimal getAgencyDuesTotal(final ProformaDisbursementAccount pda) {
    return Stream.of(
            pda.getBasicAgencyDue(),
            pda.getCarsDue(),
            pda.getCommunicationsDue(),
            pda.getBankExpensesDue(),
            pda.getClearanceDue(),
            pda.getOvertimeDue())
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }
}
