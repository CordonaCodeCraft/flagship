package flagship.domain.calculation.calculators.state;

import flagship.domain.base.due.tuple.Due;
import flagship.domain.base.range.tuple.Range;
import flagship.domain.calculation.calculators.BaseCalculatorTest;
import flagship.domain.caze.model.PdaCase;
import flagship.domain.port.model.PdaPort;
import flagship.domain.ship.model.PdaShip;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;

import static flagship.domain.port.entity.Port.PortName.ODESSOS_PBM;
import static flagship.domain.port.entity.Port.PortName.VARNA_WEST;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("Marpol due calculator tests")
class MarpolDueCalculatorTest extends BaseCalculatorTest {

  private final MarpolDueCalculator calculator = new MarpolDueCalculator();

  @BeforeEach
  void setUp() {
    final PdaPort testPort = PdaPort.builder().name(VARNA_WEST.name).build();
    final PdaShip testShip = PdaShip.builder().grossTonnage(BigDecimal.valueOf(MIN_GT)).build();
    testCase = PdaCase.builder().port(testPort).ship(testShip).build();
  }

  @DisplayName("Should return fixed marpol due")
  @Test
  void testCalculateMarpolDueWithinThreshold() {

    testCase.getShip().setGrossTonnage(getRandomGrossTonnage(MIN_GT, MAX_GT));

    calculator.set(testCase, marpolDueTariff);

    final BigDecimal expected =
        marpolDueTariff.getMarpolDuePerGrossTonnage().entrySet().stream()
            .filter(this::grossTonnageIsWithinMarpolDueRange)
            .flatMap(e -> Arrays.stream(e.getValue()))
            .map(Due::getBase)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    final BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Should return Odessos fixed Marpol due")
  @Test
  void testReturnsOdessosFixedMarpolDue() {

    final PdaPort port = PdaPort.builder().name(ODESSOS_PBM.name).build();
    testCase.setPort(port);

    calculator.set(testCase, marpolDueTariff);

    final BigDecimal expected = marpolDueTariff.getOdessosFixedMarpolDue();
    final BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Should return free sewage disposal quantity")
  @Test
  void testReturnsFreeSewageDisposalPerGrossTonnage() {

    testCase.getShip().setGrossTonnage(getRandomGrossTonnage(MIN_GT, MAX_GT));

    calculator.set(testCase, marpolDueTariff);

    final BigDecimal expected =
        getDueByRange(marpolDueTariff.getFreeSewageDisposalQuantitiesPerGrossTonnage());
    final BigDecimal result = calculator.getFreeSewageDisposalQuantity();

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Should return free garbage disposal quantity")
  @Test
  void testReturnsFreeGarbageDisposalPerGrossTonnage() {

    testCase.getShip().setGrossTonnage(getRandomGrossTonnage(MIN_GT, MAX_GT));

    calculator.set(testCase, marpolDueTariff);

    final BigDecimal expected =
        getDueByRange(marpolDueTariff.getFreeGarbageDisposalQuantitiesPerGrossTonnage());
    final BigDecimal result = calculator.getFreeGarbageDisposalQuantity();

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Should return Odessos free sewage value")
  @Test
  void testReturnsOdessosMaximumFreeSewageValue() {

    testCase.getPort().setName(ODESSOS_PBM.name);

    calculator.set(testCase, marpolDueTariff);

    final BigDecimal expected = marpolDueTariff.getOdessosFreeSewageDisposalQuantity();
    final BigDecimal result = calculator.getFreeSewageDisposalQuantity();

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Should return Odessos free garbage value")
  @Test
  void testReturnsOdessosMaximumFreeGarbageValue() {

    testCase.getPort().setName(ODESSOS_PBM.name);

    calculator.set(testCase, marpolDueTariff);

    final BigDecimal expected = marpolDueTariff.getOdessosFreeGarbageDisposalQuantity();
    final BigDecimal result = calculator.getFreeGarbageDisposalQuantity();

    assertThat(result).isEqualByComparingTo(expected);
  }

  private boolean grossTonnageIsWithinMarpolDueRange(final Map.Entry<Range, Due[]> entry) {
    return testCase.getShip().getGrossTonnage().intValue() >= entry.getKey().getMin()
        && testCase.getShip().getGrossTonnage().intValue() <= entry.getKey().getMax();
  }
}
