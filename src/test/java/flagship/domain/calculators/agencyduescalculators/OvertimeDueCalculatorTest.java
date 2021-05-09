package flagship.domain.calculators.agencyduescalculators;

import flagship.domain.calculators.BaseCalculatorTest;
import flagship.domain.cases.dto.PdaCase;
import flagship.domain.cases.dto.PdaShip;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static flagship.domain.cases.entities.enums.CallPurpose.LOADING;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("Bank overtime due calculator tests")
class OvertimeDueCalculatorTest extends BaseCalculatorTest {

  private final OvertimeDueCalculator calculator = new OvertimeDueCalculator();

  @BeforeEach
  void setUp() {
    testCase = PdaCase.builder().callPurpose(LOADING).ship(new PdaShip()).build();
  }

  @DisplayName("Should calculate bank expenses due")
  @Test
  void testCalculateBankExpensesDue() {

    testCase
        .getShip()
        .setGrossTonnage(
            getRandomGrossTonnage(
                MIN_GT, agencyDuesTariff.getBasicAgencyDueGrossTonnageThreshold().intValue()));

    calculator.set(testCase, agencyDuesTariff);

    final BigDecimal baseDue = getDueByRange(agencyDuesTariff.getBasicAgencyDuePerGrossTonnage());
    final BigDecimal expected = baseDue.multiply(agencyDuesTariff.getOvertimeCoefficient());
    final BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }
}
