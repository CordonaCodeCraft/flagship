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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("Communications due calculator tests")
class CommunicationsDueCalculatorTest extends BaseCalculatorTest {

  private AgencyDuesTariff tariff = new AgencyDuesTariff();
  private CommunicationsDueCalculator calculator = new CommunicationsDueCalculator();

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
                MIN_GT, tariff.getCommunicationsDueGrossTonnageThreshold().intValue()));

    calculator.set(testCase, tariff);

    final BigDecimal expected = tariff.getBaseCommunicationsDue();
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
                tariff.getCommunicationsDueGrossTonnageThreshold().intValue() + 1, MAX_GT));

    calculator.set(testCase, tariff);

    final BigDecimal baseDue = tariff.getBaseCommunicationsDue();
    final BigDecimal addition = tariff.getCommunicationsAdditionalDue();
    final BigDecimal multiplier =
        getMultiplier(tariff.getCommunicationsDueGrossTonnageThreshold().intValue());

    final BigDecimal expected = baseDue.add(addition.multiply(multiplier));
    final BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }
}
