package flagship.domain.calculators.agencyduescalculators;

import flagship.domain.calculators.BaseCalculatorTest;
import flagship.domain.cases.dto.PdaCase;
import flagship.domain.cases.dto.PdaShip;
import flagship.domain.tariffs.agencyduestariffs.AgencyDuesTariff;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;

import static flagship.domain.cases.entities.enums.CallPurpose.LOADING;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("Bank expenses due calculator tests")
class BankExpensesDueCalculatorTest extends BaseCalculatorTest {

  private AgencyDuesTariff tariff = new AgencyDuesTariff();
  private final BankExpensesDueCalculator calculator = new BankExpensesDueCalculator();

  {
    try {
      tariff =
          mapper.readValue(
              new File(TARIFFS_PATH + "agencyDuesTariff.json"), AgencyDuesTariff.class);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @BeforeEach
  void setUp() {
    testCase = PdaCase.builder().callPurpose(LOADING).ship(new PdaShip()).build();
  }

  @DisplayName("Should overtime due")
  @Test
  void testCalculateBankExpensesDue() {

    testCase
        .getShip()
        .setGrossTonnage(
            getRandomGrossTonnage(MIN_GT, tariff.getBasicAgencyDueGrossTonnageThreshold().intValue()));

    calculator.set(testCase, tariff);

    final BigDecimal baseDue = getFixedDue(tariff.getBasicAgencyDuePerGrossTonnage());
    final BigDecimal expected = baseDue.multiply(tariff.getBankExpensesCoefficient());
    final BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);

  }
}
