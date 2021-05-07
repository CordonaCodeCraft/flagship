package flagship.domain.calculators.agencyduescalculators;

import flagship.domain.calculators.BaseCalculatorTest;
import flagship.domain.cases.dto.PdaCase;
import flagship.domain.cases.dto.PdaShip;
import flagship.domain.cases.entities.enums.CallPurpose;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("Basic agency due calculator tests")
class BasicAgencyDueCalculatorTest extends BaseCalculatorTest {

  private final BasicAgencyDueCalculator calculator = new BasicAgencyDueCalculator();
  private final int grossTonnageThreshold =
      basicAgencyDueTariff.getGrossTonnageThreshold().intValue();

  @BeforeEach
  void setUp() {
    testCase = PdaCase.builder().ship(new PdaShip()).build();
  }

  @DisplayName("Should return basic agency due within threshold")
  @Test
  void testReturnsBasicAgencyDueWithinThreshold() {

    testCase
        .getShip()
        .setGrossTonnage(
            getRandomGrossTonnage(150, basicAgencyDueTariff.getGrossTonnageThreshold().intValue()));

    calculator.set(testCase, basicAgencyDueTariff);

    BigDecimal expected = getFixedDue(basicAgencyDueTariff.getBasicAgencyDuePerGrossTonnage());

    BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Should return increased basic agency due within threshold")
  @Test
  void testReturnsIncreasedAgencyDue() {

    testCase.getShip().setGrossTonnage(getRandomGrossTonnage(grossTonnageThreshold + 1, 200000));

    calculator.set(testCase, basicAgencyDueTariff);

    BigDecimal baseDue = getFixedDue(basicAgencyDueTariff.getBasicAgencyDuePerGrossTonnage());
    BigDecimal addition = getAddition(basicAgencyDueTariff.getBasicAgencyDuePerGrossTonnage());
    BigDecimal multiplier = getMultiplier(grossTonnageThreshold);

    BigDecimal expected = baseDue.add(addition.multiply(multiplier));
    BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Should return reduced due if call purpose is eligible for discount")
  @ParameterizedTest(name = "call purpose : {arguments}")
  @EnumSource(
      value = CallPurpose.class,
      names = {"LOADING", "UNLOADING", "LOADING_AND_UNLOADING"},
      mode = EnumSource.Mode.EXCLUDE)
  void testReturnsReducedDueIfEligibleForDiscount(CallPurpose callPurpose) {

    testCase.setCallPurpose(callPurpose);
    testCase.getShip().setGrossTonnage(getRandomGrossTonnage(150, grossTonnageThreshold));

    calculator.set(testCase, basicAgencyDueTariff);

    BigDecimal baseDue = getFixedDue(basicAgencyDueTariff.getBasicAgencyDuePerGrossTonnage());
    BigDecimal discount = baseDue.multiply(basicAgencyDueTariff.getDiscountCoefficientForCallPurpose());

    BigDecimal expected = baseDue.subtract(discount);
    BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }



















































}
