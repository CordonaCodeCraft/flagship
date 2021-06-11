package flagship.domain.calculation.calculators.agency;

import flagship.domain.calculation.calculators.BaseCalculatorTest;
import flagship.domain.caze.model.PdaCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("Basic clearance due calculator tests")
class ClearanceDueCalculatorTest extends BaseCalculatorTest {

  private final ClearanceDueCalculator calculator = new ClearanceDueCalculator();

  @BeforeEach
  void setUp() {
    testCase = PdaCase.builder().build();
  }

  @DisplayName("Should return clearance due")
  @Test
  void testCalculateClearanceDue() {

    calculator.set(testCase, agencyDuesTariff);

    final BigDecimal expected =
        agencyDuesTariff.getClearanceIn().add(agencyDuesTariff.getClearanceOut());
    final BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }
}
