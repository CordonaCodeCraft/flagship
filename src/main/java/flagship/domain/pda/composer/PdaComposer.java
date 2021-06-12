package flagship.domain.pda.composer;

import flagship.domain.calculation.calculators.CalculatorFactory;
import flagship.domain.calculation.calculators.DueCalculator;
import flagship.domain.calculation.calculators.state.MarpolDueCalculator;
import flagship.domain.calculation.tariffs.Tariff;
import flagship.domain.calculation.tariffs.TariffsFactory;
import flagship.domain.caze.model.PdaCase;
import flagship.domain.pda.entity.ProformaDisbursementAccount;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

import static flagship.domain.calculation.calculators.DueCalculator.CalculatorType;
import static flagship.domain.calculation.calculators.DueCalculator.CalculatorType.MARPOL_DUE_CALCULATOR;
import static flagship.domain.calculation.calculators.DueCalculator.CalculatorType.values;

@Component
@Getter
@Setter
@RequiredArgsConstructor
public class PdaComposer {

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
    initializeFreeDisposalsQuantity(pda);

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
              } catch (final IllegalAccessException e) {
                e.printStackTrace();
              }
            });
  }

  private void initializeField(final ProformaDisbursementAccount pda, final Field field)
      throws IllegalAccessException {
    final CalculatorType calculatorType = getCalculatorType(field);
    final Tariff tariff = tariffsFactory.getTariff(calculatorType);
    final CalculatorFactory calculatorFactory = new CalculatorFactory();
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

  private void initializeFreeDisposalsQuantity(final ProformaDisbursementAccount pda) {
    final MarpolDueCalculator calculator = new MarpolDueCalculator();
    calculator.set(source, tariffsFactory.getTariff(MARPOL_DUE_CALCULATOR));
    pda.setFreeSweageDisposalQuantity(calculator.getFreeSewageDisposalQuantity());
    pda.setFreeGarbageDisposalQuantity(calculator.getFreeGarbageDisposalQuantity());
  }

  private BigDecimal getAgencyDuesTotal(final ProformaDisbursementAccount pda) {
    return Stream.of(
            pda.getBasicAgencyDue(),
            pda.getCarsDue(),
            pda.getCommunicationsDue(),
            pda.getBankExpensesDue(),
            pda.getClearanceDue(),
            pda.getAgencyOvertimeDue())
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }
}
