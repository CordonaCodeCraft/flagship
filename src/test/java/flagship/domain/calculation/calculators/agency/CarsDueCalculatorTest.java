package flagship.domain.calculation.calculators.agency;

import flagship.domain.base.due.tuple.Due;
import flagship.domain.base.range.tuple.Range;
import flagship.domain.calculation.calculators.BaseCalculatorTest;
import flagship.domain.caze.model.PdaCase;
import flagship.domain.port.entity.Port;
import flagship.domain.port.model.PdaPort;
import flagship.domain.ship.model.PdaShip;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Random;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("Cars due calculator tests")
class CarsDueCalculatorTest extends BaseCalculatorTest {

  private final CarsDueCalculator calculator = new CarsDueCalculator();

  private static Stream<Arguments> getPortNames() {
    return agencyDuesTariff.getCarsDuesIncreaseCoefficientByPortName().keySet().stream()
        .map(Arguments::of);
  }

  @BeforeEach
  void setUp() {
    final PdaShip testShip = PdaShip.builder().grossTonnage(BigDecimal.valueOf(MIN_GT)).build();
    final PdaPort testPort =
        PdaPort.builder().name(Port.PortName.SHIFTING_SRY_ODESSOS.name).build();
    testCase =
        PdaCase.builder()
            .alongsideDaysExpected(getRandomNumber())
            .port(testPort)
            .ship(testShip)
            .build();
  }

  @DisplayName("Should return car due by grossTonnage")
  @Test
  void testReturnCarDueByGrossTonnage() {

    calculator.set(testCase, agencyDuesTariff);

    final BigDecimal expected = getCarsDue();
    final BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Should return increased cars due by port name")
  @ParameterizedTest(name = "port name : {arguments}")
  @MethodSource("getPortNames")
  void testIncreaseCarsDueByPortName(final Port.PortName portName) {

    testCase.getPort().setName(portName.name);

    calculator.set(testCase, agencyDuesTariff);

    final BigDecimal carsDue = getCarsDue();
    final BigDecimal increaseCoefficient =
        agencyDuesTariff.getCarsDuesIncreaseCoefficientByPortName().get(portName);

    final BigDecimal expected = carsDue.add(carsDue.multiply(increaseCoefficient));
    final BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }

  private BigDecimal getCarsDue() {
    return agencyDuesTariff.getCarsDueByGrossTonnageAndAlongsideDaysExpected().entrySet().stream()
        .filter(this::grossTonnageIsInRange)
        .map(this::getDue)
        .findFirst()
        .get();
  }

  private boolean grossTonnageIsInRange(final Map.Entry<Range, Map<Range, Due>> entry) {
    return testCase.getShip().getGrossTonnage().intValue() >= entry.getKey().getMin()
        && testCase.getShip().getGrossTonnage().intValue() <= entry.getKey().getMax();
  }

  private BigDecimal getDue(final Map.Entry<Range, Map<Range, Due>> entry) {
    return entry.getValue().entrySet().stream()
        .filter(this::daysAreInRange)
        .map(e -> e.getValue().getBase())
        .findFirst()
        .get();
  }

  private boolean daysAreInRange(final Map.Entry<Range, Due> entry) {
    return testCase.getAlongsideDaysExpected() >= entry.getKey().getMin()
        && testCase.getAlongsideDaysExpected() <= entry.getKey().getMax();
  }

  private Integer getRandomNumber() {
    final Random random = new Random();
    return random.ints(1, 21).findFirst().getAsInt();
  }
}
