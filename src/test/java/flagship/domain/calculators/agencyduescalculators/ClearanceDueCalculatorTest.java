package flagship.domain.calculators.agencyduescalculators;

import flagship.domain.calculators.BaseCalculatorTest;
import flagship.domain.cases.dto.PdaCase;
import flagship.domain.tariffs.agencyduestariffs.AgencyDuesTariff;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("Basic clearance due calculator tests")
class ClearanceDueCalculatorTest extends BaseCalculatorTest {

  ClearanceDueCalculatorTest() throws IOException {}

  private final AgencyDuesTariff tariff =
      mapper.readValue(new File(TARIFFS_PATH + "agencyDuesTariff.json"), AgencyDuesTariff.class);

  private final ClearanceDueCalculator calculator = new ClearanceDueCalculator();

  @BeforeEach
  void setUp() {
    testCase = PdaCase.builder().build();
  }

  @DisplayName("Should return clearance due")
  @Test
  void testCalculateClearanceDue() {

      calculator.set(testCase,tariff);

      BigDecimal expected = tariff.getClearanceIn().add(tariff.getClearanceOut());
      BigDecimal result = calculator.calculate();

      assertThat(result).isEqualByComparingTo(expected);
  }
}
