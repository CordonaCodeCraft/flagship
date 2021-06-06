package flagship.domain.calculators.agencydues;

import flagship.domain.calculators.BaseCalculatorTest;
import flagship.domain.cases.dto.PdaCase;
import flagship.domain.cases.dto.PdaShip;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static flagship.domain.cases.entities.Case.CallPurpose.LOADING;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("Bank expenses due calculator tests")
class BankExpensesDueCalculatorTest extends BaseCalculatorTest {

  private final BankExpensesDueCalculator calculator = new BankExpensesDueCalculator();

  @BeforeEach
  void setUp() {
    testCase = PdaCase.builder().callPurpose(LOADING).ship(new PdaShip()).build();
  }

  @DisplayName("Should return overtime due")
  @Test
  void testCalculateBankExpensesDue() {

    testCase
        .getShip()
        .setGrossTonnage(
            getRandomGrossTonnage(
                MIN_GT, agencyDuesTariff.getBasicAgencyDueGrossTonnageThreshold().intValue()));

    calculator.set(testCase, agencyDuesTariff);

    final BigDecimal baseDue = getDueByRange(agencyDuesTariff.getBasicAgencyDuePerGrossTonnage());
    final BigDecimal expected = baseDue.multiply(agencyDuesTariff.getBankExpensesCoefficient());
    final BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }
}
