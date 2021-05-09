package flagship.domain.calculators.agencyduescalculators;

import flagship.domain.calculators.BaseCalculatorTest;
import flagship.domain.cases.dto.PdaCase;
import flagship.domain.cases.dto.PdaShip;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("Communications due calculator tests")
class CommunicationsDueCalculatorTest extends BaseCalculatorTest {

  private final CommunicationsDueCalculator calculator = new CommunicationsDueCalculator();

  @BeforeEach
  void setUp() {
    PdaShip testShip = PdaShip.builder().build();
    testCase = PdaCase.builder().ship(testShip).build();
  }

  @DisplayName("Should return base communications due")
  @Test
  void testReturnsBaseCommunicationsDue() {

    testCase
        .getShip()
        .setGrossTonnage(
            getRandomGrossTonnage(
                MIN_GT, agencyDuesTariff.getCommunicationsDueGrossTonnageThreshold().intValue()));

    calculator.set(testCase, agencyDuesTariff);

    final BigDecimal expected = agencyDuesTariff.getBaseCommunicationsDue();
    final BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Should return increased communications due")
  @Test
  void testReturnsIncreasedCommunicationsDue() {

    testCase
        .getShip()
        .setGrossTonnage(
            getRandomGrossTonnage(
                agencyDuesTariff.getCommunicationsDueGrossTonnageThreshold().intValue() + 1,
                MAX_GT));

    calculator.set(testCase, agencyDuesTariff);

    final BigDecimal baseDue = agencyDuesTariff.getBaseCommunicationsDue();
    final BigDecimal addition = agencyDuesTariff.getCommunicationsAdditionalDue();
    final BigDecimal multiplier =
        getMultiplier(agencyDuesTariff.getCommunicationsDueGrossTonnageThreshold());

    final BigDecimal expected = baseDue.add(addition.multiply(multiplier));
    final BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }
}
