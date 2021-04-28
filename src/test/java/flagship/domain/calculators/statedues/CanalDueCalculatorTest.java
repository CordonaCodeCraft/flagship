package flagship.domain.calculators.statedues;

import com.fasterxml.jackson.databind.ObjectMapper;
import flagship.domain.calculators.DueCalculatorTest;
import flagship.domain.calculators.tariffs.stateduestariffs.CanalDueTariff;
import flagship.domain.cases.dto.PdaCase;
import flagship.domain.cases.dto.PdaPort;
import flagship.domain.cases.dto.PdaShip;
import flagship.domain.calculators.tariffs.enums.PortArea;
import flagship.domain.cases.entities.enums.ShipType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Random;
import java.util.stream.Stream;

import static flagship.domain.calculators.tariffs.enums.PortArea.FIRST;
import static flagship.domain.cases.entities.enums.ShipType.CONTAINER;
import static flagship.domain.cases.entities.enums.ShipType.GENERAL;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("Canal due calculator tests")
class CanalDueCalculatorTest implements DueCalculatorTest {

  private static CanalDueTariff tariff;
  private final CanalDueCalculator canalDueCalculator = new CanalDueCalculator();
  private PdaCase testCase;
  private BigDecimal grossTonnage;

  @BeforeAll
  public static void beforeClass() throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    tariff =
        mapper.readValue(new File("src/main/resources/canalDueTariff.json"), CanalDueTariff.class);
  }

  private static Stream<Arguments> getShipTypesEligibleForDiscount() {
    return tariff.getDiscountCoefficientByShipType().keySet().stream().map(Arguments::of);
  }

  private static Stream<Arguments> getShipTypesNotEligibleForDiscount() {
    return tariff.getShipTypesNotEligibleForDiscount().stream().map(Arguments::of);
  }

  @BeforeEach
  void setUp() {
    BigDecimal randomGrossTonnage = getRandomGrossTonnage();
    PdaPort testPort = PdaPort.builder().area(FIRST).build();
    PdaShip testShip = PdaShip.builder().grossTonnage(randomGrossTonnage).type(GENERAL).build();
    testCase = PdaCase.builder().ship(testShip).port(testPort).callCount(1).build();
    grossTonnage = testShip.getGrossTonnage();
  }

  @DisplayName("Canal due by port area")
  @ParameterizedTest(name = "port area : {arguments}")
  @EnumSource(PortArea.class)
  void testDefaultCanalDue(PortArea portArea) {

    testCase.getPort().setArea(portArea);

    BigDecimal canalDue = tariff.getCanalDuesByPortArea().get(testCase.getPort().getArea());

    BigDecimal expected = canalDue.multiply(grossTonnage);
    BigDecimal result = canalDueCalculator.calculateFor(testCase, tariff);

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Canal due with default discount by call count")
  @Test
  void testCanalDueWithDefaultDiscountByCallCount() {

    testCase.setCallCount(tariff.getCallCountThreshold());

    BigDecimal discountCoefficient = tariff.getDefaultCallCountDiscountCoefficient();

    BigDecimal expected = calculateDueAfterDiscount(discountCoefficient);
    BigDecimal result = canalDueCalculator.calculateFor(testCase, tariff);

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Canal due with discount by ship type")
  @ParameterizedTest(name = "ship type: {arguments}")
  @MethodSource(value = "getShipTypesEligibleForDiscount")
  void testCanalDueWithDiscountByShipType(ShipType shipType) {

    testCase.getShip().setType(shipType);

    BigDecimal discountCoefficient =
        tariff.getDiscountCoefficientByShipType().get(testCase.getShip().getType());

    BigDecimal expected = calculateDueAfterDiscount(discountCoefficient);
    BigDecimal result = canalDueCalculator.calculateFor(testCase, tariff);

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Canal due with biggest discount")
  @ParameterizedTest(name = "ship type: {arguments}")
  @MethodSource(value = "getShipTypesEligibleForDiscount")
  void testCanalDueWithBiggestDiscount(ShipType shipType) {

    testCase.getShip().setType(shipType);
    testCase.setCallCount(tariff.getCallCountThreshold());

    BigDecimal callCountDiscountCoefficient = tariff.getDefaultCallCountDiscountCoefficient();
    BigDecimal shipTypeDiscountCoefficient =
        tariff.getDiscountCoefficientByShipType().get(shipType);

    BigDecimal discountCoefficient = callCountDiscountCoefficient.max(shipTypeDiscountCoefficient);

    BigDecimal expected = calculateDueAfterDiscount(discountCoefficient);
    BigDecimal result = canalDueCalculator.calculateFor(testCase, tariff);

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Canal due for containers with discount by port area")
  @ParameterizedTest(name = "port area : {arguments}")
  @EnumSource(PortArea.class)
  void testCanalDueForContainersWithDiscountByPortArea(PortArea portArea) {

    testCase.getPort().setArea(portArea);
    testCase.getShip().setType(CONTAINER);

    BigDecimal discountCoefficient =
        tariff.getDiscountCoefficientsByPortAreaForContainers().get(portArea);

    BigDecimal expected = calculateDueAfterDiscount(discountCoefficient);
    BigDecimal result = canalDueCalculator.calculateFor(testCase, tariff);

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Canal due for containers with discount by port area and call count")
  @ParameterizedTest(name = "port area : {arguments}")
  @EnumSource(PortArea.class)
  void testCanalDueForContainersWithDiscountByPortAreaAndCallCount(PortArea portArea) {

    testCase.getPort().setArea(portArea);
    testCase.getShip().setType(CONTAINER);
    testCase.setCallCount(tariff.getCallCountThreshold());

    BigDecimal discountCoefficientByPortArea =
            tariff.getDiscountCoefficientsByPortAreaForContainers().get(testCase.getPort().getArea());

    BigDecimal discountCoefficientByPortAreaPerCallCount =
        tariff
            .getDiscountCoefficientsByPortAreaPerCallCountForContainers()
            .get(testCase.getPort().getArea());

    BigDecimal discountCoefficient =
        discountCoefficientByPortArea.max(discountCoefficientByPortAreaPerCallCount);

    BigDecimal expected = calculateDueAfterDiscount(discountCoefficient);
    BigDecimal result = canalDueCalculator.calculateFor(testCase, tariff);

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Canal due without discount when ship type is not eligible for discount")
  @ParameterizedTest(name = "ship type: {arguments}")
  @MethodSource(value = "getShipTypesNotEligibleForDiscount")
  void testCanalDueWithoutDiscountWhenShipTypeIsNotEligibleForDiscount(ShipType shipType) {

    testCase.getShip().setType(shipType);
    testCase.setCallCount(tariff.getCallCountThreshold());

    BigDecimal discountCoefficient = BigDecimal.ZERO;

    BigDecimal expected = calculateDueAfterDiscount(discountCoefficient);
    BigDecimal result = canalDueCalculator.calculateFor(testCase, tariff);

    assertThat(result).isEqualByComparingTo(expected);
  }

  BigDecimal calculateDueAfterDiscount(BigDecimal discountCoefficient) {
    BigDecimal duePerGrossTon = tariff.getCanalDuesByPortArea().get(testCase.getPort().getArea());
    BigDecimal dueTotal = testCase.getShip().getGrossTonnage().multiply(duePerGrossTon);
    BigDecimal discount = dueTotal.multiply(discountCoefficient);
    return dueTotal.subtract(discount);
  }

  private BigDecimal getRandomGrossTonnage() {
    Random random = new Random();
    return BigDecimal.valueOf(random.ints(500, 200000).findFirst().getAsInt());
  }
}
