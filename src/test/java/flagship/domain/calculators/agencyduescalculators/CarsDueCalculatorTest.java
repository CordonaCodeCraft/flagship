package flagship.domain.calculators.agencyduescalculators;

import flagship.domain.calculators.BaseCalculatorTest;
import flagship.domain.cases.dto.PdaCase;
import flagship.domain.cases.dto.PdaPort;
import flagship.domain.cases.dto.PdaShip;
import flagship.domain.tariffs.PortName;
import flagship.domain.tariffs.Range;
import flagship.domain.tariffs.agencyduestariffs.AgencyDuesTariff;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Random;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("Cars due calculator tests")
class CarsDueCalculatorTest extends BaseCalculatorTest {

  private static AgencyDuesTariff tariff = new AgencyDuesTariff();

  static {
    try {
      tariff =
          mapper.readValue(
              new File(TARIFFS_PATH + "agencyDuesTariff.json"), AgencyDuesTariff.class);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private final CarsDueCalculator calculator = new CarsDueCalculator();

  @BeforeEach
  void setUp() {
    PdaShip testShip = PdaShip.builder().grossTonnage(getRandomGrossTonnage(150, 650000)).build();
    testCase =
        PdaCase.builder()
            .alongsideDaysExpected(getRandomNumber())
            .port(new PdaPort())
            .ship(testShip)
            .build();
  }

  @DisplayName("Should return car due by grossTonnage")
  @Test
  void testReturnCarDueByGrossTonnage() {

    calculator.set(testCase, tariff);

    final BigDecimal expected = getCarsDue();
    final BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Should return increased cars due by port name")
  @ParameterizedTest(name = "port name : {arguments}")
  @MethodSource("getPortNames")
  void testIncreaseCarsDueByPortName(final PortName portName) {

    testCase.getPort().setName(portName.name);

    calculator.set(testCase, tariff);

    final BigDecimal carsDue = getCarsDue();
    final BigDecimal increaseCoefficient =
        tariff.getCarsDuesIncreaseCoefficientByPortName().get(portName);

    final BigDecimal expected = carsDue.add(carsDue.multiply(increaseCoefficient));
    final BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }

  private BigDecimal getCarsDue() {
    return tariff.getCarsDueByGrossTonnageAndAlongsideDaysExpected().entrySet().stream()
        .filter(
            entry ->
                testCase.getShip().getGrossTonnage().intValue() >= entry.getKey().getMin()
                    && testCase.getShip().getGrossTonnage().intValue() <= entry.getKey().getMax())
        .map(this::getDue)
        .findFirst()
        .get();
  }

  private BigDecimal getDue(final Map.Entry<Range, Map<Range, BigDecimal>> entry) {
    return entry.getValue().entrySet().stream()
        .filter(
            e ->
                testCase.getAlongsideDaysExpected() >= e.getKey().getMin()
                    && testCase.getAlongsideDaysExpected() <= e.getKey().getMax())
        .map(Map.Entry::getValue)
        .findFirst()
        .get();
  }

  private Integer getRandomNumber() {
    Random random = new Random();
    return random.ints(1, 21).findFirst().getAsInt();
  }

  private static Stream<Arguments> getPortNames() {
    return tariff.getCarsDuesIncreaseCoefficientByPortName().keySet().stream().map(Arguments::of);
  }
}
