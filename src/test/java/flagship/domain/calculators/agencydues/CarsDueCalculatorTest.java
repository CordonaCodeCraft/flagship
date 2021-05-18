package flagship.domain.calculators.agencydues;

import flagship.domain.calculators.BaseCalculatorTest;
import flagship.domain.cases.dto.PdaCase;
import flagship.domain.cases.dto.PdaPort;
import flagship.domain.cases.dto.PdaShip;
import flagship.domain.tariffs.mix.Due;
import flagship.domain.tariffs.mix.PortName;
import flagship.domain.tariffs.mix.Range;
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
    PdaShip testShip = PdaShip.builder().grossTonnage(BigDecimal.valueOf(MIN_GT)).build();
    PdaPort testPort = PdaPort.builder().name(PortName.SHIFTING_SRY_ODESSOS.name).build();
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
  void testIncreaseCarsDueByPortName(final PortName portName) {

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
    Random random = new Random();
    return random.ints(1, 21).findFirst().getAsInt();
  }
}
