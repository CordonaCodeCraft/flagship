package flagship.domain;

import flagship.domain.calculators.TariffsInitializer;
import flagship.domain.calculators.agencydues.*;
import flagship.domain.calculators.servicedues.MooringDueCalculator;
import flagship.domain.calculators.servicedues.PilotageDueCalculator;
import flagship.domain.calculators.servicedues.TugDueCalculator;
import flagship.domain.calculators.statedues.*;
import flagship.domain.cases.dto.PdaCase;
import flagship.domain.cases.dto.PdaPort;
import flagship.domain.cases.dto.PdaShip;
import flagship.domain.cases.entities.ProformaDisbursementAccount;
import flagship.domain.tariffs.TariffsFactory;
import flagship.domain.tariffs.Tariff;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.stream.Stream;

import static flagship.domain.calculators.DueCalculator.CalculatorType;
import static flagship.domain.calculators.DueCalculator.CalculatorType.*;
import static flagship.domain.cases.entities.Ship.ShipType.BULK_CARRIER;
import static flagship.domain.tariffs.PortArea.FIRST;
import static flagship.domain.tariffs.servicedues.MooringDueTariff.MooringServiceProvider;
import static flagship.domain.tariffs.servicedues.PilotageDueTariff.PilotageArea.BOURGAS_FIRST;
import static flagship.domain.tariffs.servicedues.TugDueTariff.TugArea;
import static flagship.domain.tariffs.servicedues.TugDueTariff.TugServiceProvider;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pda composer tests")
public class PdaComposerTest extends TariffsInitializer {

  static final TonnageDueCalculator tonnageCalculator = new TonnageDueCalculator();
  static final WharfDueCalculator wharfCalculator = new WharfDueCalculator();
  static final CanalDueCalculator canalCalculator = new CanalDueCalculator();
  static final LightDueCalculator lightCalculator = new LightDueCalculator();
  static final MarpolDueCalculator marpolCalculator = new MarpolDueCalculator();
  static final MooringDueCalculator mooringCalculator = new MooringDueCalculator();
  static final BoomContainmentCalculator boomCalculator = new BoomContainmentCalculator();
  static final SailingPermissionCalculator sailingCalculator = new SailingPermissionCalculator();
  static final PilotageDueCalculator pilotageCalculator = new PilotageDueCalculator();
  static final TugDueCalculator tugCalculator = new TugDueCalculator();
  static final BasicAgencyDueCalculator basicAgencyCalculator = new BasicAgencyDueCalculator();
  static final CarsDueCalculator carsCalculator = new CarsDueCalculator();
  static final ClearanceDueCalculator clearanceCalculator = new ClearanceDueCalculator();
  static final CommunicationsDueCalculator commCalculator = new CommunicationsDueCalculator();
  static final BankExpensesDueCalculator bankExpensesCalculator = new BankExpensesDueCalculator();
  static final OvertimeDueCalculator overtimeCalculator = new OvertimeDueCalculator();
  private static final TariffsFactory tariffsFactory = new TariffsFactory();
  private static final PdaComposer pdaComposer = new PdaComposer();
  private static PdaCase testCase;
  private static BigDecimal tonnageDue;
  private static BigDecimal wharfDue;
  private static BigDecimal canalDue;
  private static BigDecimal lightDue;
  private static BigDecimal marpolDue;
  private static BigDecimal mooringDue;
  private static BigDecimal boomContainmentDue;
  private static BigDecimal sailingPermissionDue;
  private static BigDecimal pilotageDue;
  private static BigDecimal tugDue;
  private static BigDecimal basicAgencyDue;
  private static BigDecimal carsDue;
  private static BigDecimal clearanceDue;
  private static BigDecimal communicationsDue;
  private static BigDecimal bankExpensesDue;
  private static BigDecimal overTimeDue;

  @BeforeAll
  static void beforeClass() {

    final PdaShip testShip =
        PdaShip.builder()
            .grossTonnage(BigDecimal.valueOf(1650))
            .hasIncreasedManeuverability(true)
            .lengthOverall(BigDecimal.valueOf(160.00))
            .type(BULK_CARRIER)
            .build();

    final PdaPort testPort =
        PdaPort.builder()
            .name("Varna West")
            .portArea(FIRST)
            .tugArea(TugArea.VTC_FIRST)
            .mooringServiceProvider(MooringServiceProvider.VTC)
            .pilotageArea(BOURGAS_FIRST)
            .tugServiceProvider(TugServiceProvider.VTC)
            .build();

    testCase =
        PdaCase.builder()
            .ship(testShip)
            .port(testPort)
            .arrivesFromBulgarianPort(true)
            .estimatedDateOfArrival(LocalDate.now())
            .estimatedDateOfDeparture(LocalDate.now().plusDays(5))
            .alongsideDaysExpected(5)
            .callCount(3)
            .clientDiscountCoefficient(BigDecimal.ZERO)
            .warningTypes(new HashSet<>())
            .build();

    tonnageCalculator.set(testCase, tonnageDueTariff);
    wharfCalculator.set(testCase, wharfDueTariff);
    canalCalculator.set(testCase, canalDueTariff);
    lightCalculator.set(testCase, lightDueTariff);
    marpolCalculator.set(testCase, marpolDueTariff);
    mooringCalculator.set(testCase, mooringDueTariff);
    boomCalculator.set(testCase, boomContainmentTariff);
    sailingCalculator.set(testCase, sailingPermissionTariff);
    pilotageCalculator.set(testCase, pilotageDueTariff);
    tugCalculator.set(testCase, tugDueTariff);
    basicAgencyCalculator.set(testCase, agencyDuesTariff);
    carsCalculator.set(testCase, agencyDuesTariff);
    clearanceCalculator.set(testCase, agencyDuesTariff);
    commCalculator.set(testCase, agencyDuesTariff);
    bankExpensesCalculator.set(testCase, agencyDuesTariff);
    overtimeCalculator.set(testCase, agencyDuesTariff);

    tonnageDue = tonnageCalculator.calculate();
    wharfDue = wharfCalculator.calculate();
    canalDue = canalCalculator.calculate();
    lightDue = lightCalculator.calculate();
    marpolDue = marpolCalculator.calculate();
    mooringDue = mooringCalculator.calculate();
    boomContainmentDue = boomCalculator.calculate();
    sailingPermissionDue = sailingCalculator.calculate();
    pilotageDue = pilotageCalculator.calculate();
    tugDue = tugCalculator.calculate();
    basicAgencyDue = basicAgencyCalculator.calculate();
    carsDue = carsCalculator.calculate();
    clearanceDue = clearanceCalculator.calculate();
    communicationsDue = commCalculator.calculate();
    bankExpensesDue = bankExpensesCalculator.calculate();
    overTimeDue = overtimeCalculator.calculate();

    final Map<CalculatorType, Tariff> tariffs = new EnumMap<>(CalculatorType.class);

    tariffs.put(TONNAGE_DUE_CALCULATOR, tonnageDueTariff);
    tariffs.put(WHARF_DUE_CALCULATOR, wharfDueTariff);
    tariffs.put(CANAL_DUE_CALCULATOR, canalDueTariff);
    tariffs.put(LIGHT_DUE_CALCULATOR, lightDueTariff);
    tariffs.put(MARPOL_DUE_CALCULATOR, marpolDueTariff);
    tariffs.put(MOORING_DUE_CALCULATOR, mooringDueTariff);
    tariffs.put(BOOM_CONTAINMENT_DUE_CALCULATOR, boomContainmentTariff);
    tariffs.put(SAILING_PERMISSION_CALCULATOR, sailingPermissionTariff);
    tariffs.put(PILOTAGE_DUE_CALCULATOR, pilotageDueTariff);
    tariffs.put(TUG_DUE_CALCULATOR, tugDueTariff);
    tariffs.put(BASIC_AGENCY_DUE_CALCULATOR, agencyDuesTariff);
    tariffs.put(BANK_EXPENSES_DUE_CALCULATOR, agencyDuesTariff);
    tariffs.put(CARS_DUE_CALCULATOR, agencyDuesTariff);
    tariffs.put(CLEARANCE_DUE_CALCULATOR, agencyDuesTariff);
    tariffs.put(COMMUNICATIONS_DUE_CALCULATOR, agencyDuesTariff);
    tariffs.put(OVERTIME_DUE_CALCULATOR, agencyDuesTariff);

    tariffsFactory.setTariffs(tariffs);

    pdaComposer.setSource(testCase);
    pdaComposer.setTariffsFactory(tariffsFactory);
    pdaComposer.setCommissionCoefficient(BigDecimal.valueOf(0.3));
  }

  @BeforeEach
  void setUp() {}

  @DisplayName("Should set PDA due fields with correct values")
  @Test
  void testSetsDuesFieldsWithCorrectCalculations() {

    final ProformaDisbursementAccount pda = pdaComposer.composePda();

    SoftAssertions assertions = new SoftAssertions();

    assertThat(tonnageDue).isEqualByComparingTo(pda.getTonnageDue());
    assertThat(wharfDue).isEqualByComparingTo(pda.getWharfDue());
    assertThat(canalDue).isEqualByComparingTo(pda.getCanalDue());
    assertThat(lightDue).isEqualByComparingTo(pda.getLightDue());
    assertThat(marpolDue).isEqualByComparingTo(pda.getMarpolDue());
    assertThat(mooringDue).isEqualByComparingTo(pda.getMooringDue());
    assertThat(boomContainmentDue).isEqualByComparingTo(pda.getBoomContainmentDue());
    assertThat(sailingPermissionDue).isEqualByComparingTo(pda.getSailingPermissionDue());
    assertThat(pilotageDue).isEqualByComparingTo(pda.getPilotageDue());
    assertThat(tugDue).isEqualByComparingTo(pda.getTugDue());
    assertThat(basicAgencyDue).isEqualByComparingTo(pda.getBasicAgencyDue());
    assertThat(bankExpensesDue).isEqualByComparingTo(pda.getBankExpensesDue());
    assertThat(carsDue).isEqualByComparingTo(pda.getCarsDue());
    assertThat(clearanceDue).isEqualByComparingTo(pda.getClearanceDue());
    assertThat(communicationsDue).isEqualByComparingTo(pda.getCommunicationsDue());
    assertThat(overTimeDue).isEqualByComparingTo(pda.getAgencyOvertimeDue());

    assertions.assertAll();
  }

  @DisplayName("Should set client discount with correct value")
  @Test
  void testSetsClientDiscountWithCorrectValue() {

    testCase.setClientDiscountCoefficient(BigDecimal.valueOf(0.5));

    final BigDecimal agencyDuesTotal =
        Stream.of(
                basicAgencyDue,
                carsDue,
                clearanceDue,
                communicationsDue,
                bankExpensesDue,
                overTimeDue)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    final BigDecimal expected = agencyDuesTotal.multiply(testCase.getClientDiscountCoefficient());
    final BigDecimal result = pdaComposer.composePda().getClientDiscount();

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Should set client discount with zero")
  @Test
  void testSetsClientDiscountToZero() {

    testCase.setClientDiscountCoefficient(BigDecimal.ZERO);

    final BigDecimal expected = BigDecimal.ZERO;
    final BigDecimal result = pdaComposer.composePda().getClientDiscount();

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Should set payable total with correct value")
  @Test
  void testSetsPayableTotal() {

    final BigDecimal expected =
        Stream.of(
                tonnageDue,
                wharfDue,
                canalDue,
                lightDue,
                marpolDue,
                mooringDue,
                boomContainmentDue,
                sailingPermissionDue,
                pilotageDue,
                tugDue,
                basicAgencyDue,
                carsDue,
                clearanceDue,
                communicationsDue,
                bankExpensesDue,
                overTimeDue)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    final BigDecimal result = pdaComposer.composePda().getPayableTotal();

    assertThat(result).isEqualByComparingTo(expected);
  }

  @DisplayName("Should set profit expected with correct value")
  @Test
  void testSetsProfitExpected() {

    final BigDecimal agencyDuesTotal =
        Stream.of(
                basicAgencyDue,
                carsDue,
                clearanceDue,
                communicationsDue,
                bankExpensesDue,
                overTimeDue)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    final BigDecimal expected =
        agencyDuesTotal.add(tugDue.multiply(pdaComposer.getCommissionCoefficient()));

    final BigDecimal result = pdaComposer.composePda().getProfitExpected();

    assertThat(result).isEqualByComparingTo(expected);
  }
}
