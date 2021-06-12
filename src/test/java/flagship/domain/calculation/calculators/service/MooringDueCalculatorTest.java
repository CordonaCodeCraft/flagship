package flagship.domain.calculation.calculators.service;

import flagship.domain.calculation.calculators.BaseCalculatorTest;
import flagship.domain.caze.model.PdaCase;
import flagship.domain.caze.model.createrequest.resolvers.MooringServiceProviderResolver.MooringServiceProvider;
import flagship.domain.port.model.PdaPort;
import flagship.domain.ship.model.PdaShip;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.math.BigDecimal;

import static flagship.domain.caze.model.createrequest.resolvers.MooringServiceProviderResolver.MooringServiceProvider.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("Mooring due calculator tests")
class MooringDueCalculatorTest extends BaseCalculatorTest {

  private final MooringDueCalculator calculator = new MooringDueCalculator();

  @BeforeEach
  void setUp() {
    final PdaShip testShip = PdaShip.builder().grossTonnage(BigDecimal.valueOf(MIN_GT)).build();
    final PdaPort testPort = PdaPort.builder().mooringServiceProvider(VTC).build();
    testCase = PdaCase.builder().ship(testShip).port(testPort).build();
  }

  @DisplayName("Should return fixed mooring due for VTC and PortFleet within threshold")
  @ParameterizedTest(name = "provider : {arguments}")
  @EnumSource(
      value = MooringServiceProvider.class,
      names = {"VTC", "PORTFLEET", "PCHMV"})
  void testReturnsFixedMooringDueForVtcAndPortFleet(final MooringServiceProvider provider) {

    testCase.getPort().setMooringServiceProvider(provider);
    testCase
        .getShip()
        .setGrossTonnage(
            getRandomGrossTonnage(
                MIN_GT, mooringDueTariff.getVtcGrossTonnageThreshold().intValue()));

    calculator.set(testCase, mooringDueTariff);

    final BigDecimal fixedDue =
        getDueByRange(mooringDueTariff.getMooringDuesByProvider().get(provider));

    final BigDecimal expected = fixedDue.multiply(BigDecimal.valueOf(2.0));
    final BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Should return increased mooring due for VTC and PortFleet")
  @ParameterizedTest(name = "provider : {arguments}")
  @EnumSource(
      value = MooringServiceProvider.class,
      names = {"VTC", "PORTFLEET", "PCHMV"})
  void testReturnsIncreasedMooringDueForVtcAndPortFleet(final MooringServiceProvider provider) {

    testCase.getPort().setMooringServiceProvider(provider);
    testCase
        .getShip()
        .setGrossTonnage(
            getRandomGrossTonnage(
                mooringDueTariff.getVtcGrossTonnageThreshold().intValue() + 1, MAX_GT));

    calculator.set(testCase, mooringDueTariff);

    final BigDecimal fixedDue =
        getDueByRange(mooringDueTariff.getMooringDuesByProvider().get(provider));
    final BigDecimal addition =
        getAddition(mooringDueTariff.getMooringDuesByProvider().get(provider));
    final BigDecimal multiplier = getMultiplier(mooringDueTariff.getVtcGrossTonnageThreshold());

    final BigDecimal expected =
        fixedDue.add(addition.multiply(multiplier)).multiply(BigDecimal.valueOf(2));
    final BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Should return fixed mooring due for Lesport within threshold")
  @Test
  void testReturnsFixedMooringDueForLesport() {

    testCase.getPort().setMooringServiceProvider(LESPORT);
    testCase
        .getShip()
        .setGrossTonnage(
            getRandomGrossTonnage(
                MIN_GT, mooringDueTariff.getLesportGrossTonnageThreshold().intValue()));

    calculator.set(testCase, mooringDueTariff);

    final BigDecimal fixedDue =
        getDueByRange(mooringDueTariff.getMooringDuesByProvider().get(LESPORT));

    final BigDecimal expected = fixedDue.multiply(BigDecimal.valueOf(2.0));
    final BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Should return increased mooring due for Lesport")
  @Test
  void testReturnsIncreasedMooringDueForLesport() {

    testCase.getPort().setMooringServiceProvider(LESPORT);
    testCase
        .getShip()
        .setGrossTonnage(
            getRandomGrossTonnage(
                mooringDueTariff.getLesportGrossTonnageThreshold().intValue() + 1, MAX_GT));

    calculator.set(testCase, mooringDueTariff);

    final BigDecimal fixedDue =
        getDueByRange(mooringDueTariff.getMooringDuesByProvider().get(LESPORT));
    final BigDecimal addition =
        getAddition(mooringDueTariff.getMooringDuesByProvider().get(LESPORT));
    final BigDecimal multiplier = getMultiplier(mooringDueTariff.getLesportGrossTonnageThreshold());

    final BigDecimal expected =
        fixedDue.add(addition.multiply(multiplier)).multiply(BigDecimal.valueOf(2));
    final BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Should return fixed mooring due for Port Balchik within threshold")
  @Test
  void testReturnsMooringDueForBalchikWithinThreshold() {

    testCase.getPort().setMooringServiceProvider(BALCHIK);
    testCase
        .getShip()
        .setGrossTonnage(
            getRandomGrossTonnage(
                MIN_GT, mooringDueTariff.getBalchikGrossTonnageThreshold().intValue()));

    calculator.set(testCase, mooringDueTariff);

    final BigDecimal fixedDue =
        getDueByRange(mooringDueTariff.getMooringDuesByProvider().get(BALCHIK));

    final BigDecimal expected = fixedDue.multiply(BigDecimal.valueOf(2));
    final BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Should return fixed maximum mooring due for Port Balchik over threshold")
  @Test
  void testReturnsMooringDueForBalchikOverThreshold() {

    testCase.getPort().setMooringServiceProvider(BALCHIK);
    testCase
        .getShip()
        .setGrossTonnage(
            getRandomGrossTonnage(
                mooringDueTariff.getBalchikGrossTonnageThreshold().intValue() + 1, MAX_GT));

    calculator.set(testCase, mooringDueTariff);

    final BigDecimal fixedDue =
        getDueByRange(mooringDueTariff.getMooringDuesByProvider().get(BALCHIK));

    final BigDecimal expected = fixedDue.multiply(BigDecimal.valueOf(2));
    final BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Should return fixed mooring due for Odessos PBM within threshold")
  @Test
  void testReturnsMooringDueForOdessosWithinThreshold() {

    testCase.getPort().setMooringServiceProvider(ODESSOS);
    testCase
        .getShip()
        .setGrossTonnage(
            getRandomGrossTonnage(
                MIN_GT, mooringDueTariff.getOdessosGrossTonnageThreshold().intValue()));

    calculator.set(testCase, mooringDueTariff);

    final BigDecimal expected =
        getDueByRange(mooringDueTariff.getMooringDuesByProvider().get(ODESSOS));
    final BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Should return fixed maximum mooring due for Odessos PBM over threshold")
  @Test
  void testReturnsMooringDueForOdessosOverThreshold() {

    testCase.getPort().setMooringServiceProvider(ODESSOS);
    testCase
        .getShip()
        .setGrossTonnage(
            getRandomGrossTonnage(
                mooringDueTariff.getOdessosGrossTonnageThreshold().intValue() + 1, MAX_GT));

    calculator.set(testCase, mooringDueTariff);

    final BigDecimal expected =
        getDueByRange(mooringDueTariff.getMooringDuesByProvider().get(ODESSOS));
    final BigDecimal result = calculator.calculate();

    assertThat(result).isEqualByComparingTo(expected);
  }
}
