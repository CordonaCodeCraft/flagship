package flagship.domain.calculators.stateduescalculators;

import flagship.domain.calculators.BaseCalculatorTest;
import flagship.domain.cases.dto.PdaCase;
import flagship.domain.cases.dto.PdaPort;
import flagship.domain.cases.dto.PdaShip;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static flagship.domain.cases.entities.enums.ShipType.GENERAL;
import static flagship.domain.cases.entities.enums.ShipType.OIL_TANKER;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("Boom containment due calculator tests")
class BoomContainmentCalculatorTest extends BaseCalculatorTest {

  private final BoomContainmentCalculator calculator = new BoomContainmentCalculator();

  @BeforeEach
  void setUp() {
    PdaPort testPort = PdaPort.builder().build();
    PdaShip testShip =
        PdaShip.builder().type(OIL_TANKER).grossTonnage(BigDecimal.valueOf(MIN_GT)).build();
    testCase = PdaCase.builder().port(testPort).ship(testShip).build();
  }

  @DisplayName("Should return boom containment due")
  @Test
  void testReturnsBoomContainmentDueWithinThreshold() {

    testCase.getShip().setGrossTonnage(getRandomGrossTonnage(MIN_GT, MAX_GT));

    calculator.set(testCase, boomContainmentTariff);

    final BigDecimal expected =
        getDueByRange(boomContainmentTariff.getBoomContainmentDuePerGrossTonnage());
    final BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Should return zero value if ship type is not oil tanker")
  @Test
  void testReturnsZeroIfShipTypeIsNotOilTanker() {

    testCase.getShip().setType(GENERAL);

    calculator.set(testCase, boomContainmentTariff);

    final BigDecimal expected = BigDecimal.ZERO;
    final BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }
}
