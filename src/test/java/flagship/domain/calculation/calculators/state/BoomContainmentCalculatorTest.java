package flagship.domain.calculation.calculators.state;

import flagship.domain.calculation.calculators.BaseCalculatorTest;
import flagship.domain.caze.model.PdaCase;
import flagship.domain.port.model.PdaPort;
import flagship.domain.ship.model.PdaShip;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static flagship.domain.ship.entity.Ship.ShipType.BULK_CARRIER;
import static flagship.domain.ship.entity.Ship.ShipType.OIL_TANKER;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("Boom containment due calculator tests")
class BoomContainmentCalculatorTest extends BaseCalculatorTest {

  private final BoomContainmentCalculator calculator = new BoomContainmentCalculator();

  @BeforeEach
  void setUp() {
    final PdaPort testPort = PdaPort.builder().build();
    final PdaShip testShip =
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

    testCase.getShip().setType(BULK_CARRIER);

    calculator.set(testCase, boomContainmentTariff);

    final BigDecimal expected = BigDecimal.ZERO;
    final BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }
}
