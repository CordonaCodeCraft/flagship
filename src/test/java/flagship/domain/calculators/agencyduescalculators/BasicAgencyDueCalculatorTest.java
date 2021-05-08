package flagship.domain.calculators.agencyduescalculators;

import flagship.domain.calculators.BaseCalculatorTest;
import flagship.domain.cases.dto.PdaCase;
import flagship.domain.cases.dto.PdaShip;
import flagship.domain.cases.entities.enums.CallPurpose;
import flagship.domain.tariffs.agencyduestariffs.AgencyDuesTariff;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;

import static flagship.domain.cases.entities.enums.CallPurpose.LOADING;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("Basic agency due calculator tests")
class BasicAgencyDueCalculatorTest extends BaseCalculatorTest {

  BasicAgencyDueCalculatorTest() throws IOException {}

  private final AgencyDuesTariff tariff =
      mapper.readValue(new File(TARIFFS_PATH + "agencyDuesTariff.json"), AgencyDuesTariff.class);

  private final BasicAgencyDueCalculator calculator = new BasicAgencyDueCalculator();
  private final int grossTonnageThreshold =
      tariff.getBasicAgencyDueGrossTonnageThreshold().intValue();

  @BeforeEach
  void setUp() {
    testCase = PdaCase.builder().callPurpose(LOADING).ship(new PdaShip()).build();
  }

  @DisplayName("Should return basic agency due within threshold")
  @Test
  void testReturnsBasicAgencyDueWithinThreshold() {

    testCase
        .getShip()
        .setGrossTonnage(
            getRandomGrossTonnage(
                MIN_GT, tariff.getBasicAgencyDueGrossTonnageThreshold().intValue()));

    calculator.set(testCase, tariff);

    final BigDecimal expected = getFixedDue(tariff.getBasicAgencyDuePerGrossTonnage());

    final BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Should return increased basic agency due")
  @Test
  void testReturnsIncreasedAgencyDue() {

    testCase.getShip().setGrossTonnage(getRandomGrossTonnage(grossTonnageThreshold + 1, MAX_GT));

    calculator.set(testCase, tariff);

    final BigDecimal baseDue = getFixedDue(tariff.getBasicAgencyDuePerGrossTonnage());
    final BigDecimal addition = getAddition(tariff.getBasicAgencyDuePerGrossTonnage());
    final BigDecimal multiplier = getMultiplier(grossTonnageThreshold);

    System.out.println(multiplier);

    final BigDecimal expected = baseDue.add(addition.multiply(multiplier));
    final BigDecimal result = calculator.calculate();

    //    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Should return reduced due if call purpose is eligible for discount")
  @ParameterizedTest(name = "call purpose : {arguments}")
  @EnumSource(
      value = CallPurpose.class,
      names = {"LOADING", "UNLOADING", "LOADING_AND_UNLOADING"},
      mode = EnumSource.Mode.EXCLUDE)
  void testReturnsReducedDueIfEligibleForDiscount(CallPurpose callPurpose) {

    testCase.setCallPurpose(callPurpose);
    testCase.getShip().setGrossTonnage(getRandomGrossTonnage(MIN_GT, grossTonnageThreshold));

    calculator.set(testCase, tariff);

    final BigDecimal baseDue = getFixedDue(tariff.getBasicAgencyDuePerGrossTonnage());
    final BigDecimal discount =
        baseDue.multiply(tariff.getBasicAgencyDueDiscountCoefficientByCallPurpose());

    final BigDecimal expected = baseDue.subtract(discount);
    final BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }
}
