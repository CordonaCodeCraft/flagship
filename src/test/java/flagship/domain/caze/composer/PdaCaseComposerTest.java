package flagship.domain.caze.composer;

import flagship.domain.calculation.calculators.TariffsInitializer;
import flagship.domain.calculation.tariffs.Tariff;
import flagship.domain.calculation.tariffs.TariffsFactory;
import flagship.domain.caze.model.PdaCase;
import flagship.domain.caze.model.createrequest.CreateCaseRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static flagship.domain.calculation.calculators.DueCalculator.CalculatorType;
import static flagship.domain.calculation.calculators.DueCalculator.CalculatorType.*;
import static flagship.domain.caze.model.createrequest.resolvers.TugAreaResolver.TugServiceProvider;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("Pda Case Composer tests")
class PdaCaseComposerTest extends TariffsInitializer {

  private CreateCaseRequest testRequest;
  private static final TariffsFactory tariffsFactory = new TariffsFactory();
  private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

  @BeforeEach
  void setup() {
    testRequest =
        CreateCaseRequest.builder()
            .portName("Varna West")
            .shipName("Falcon")
            .shipType("Bulk carrier")
            .shipLengthOverall(BigDecimal.valueOf(190.95))
            .shipGrossTonnage(BigDecimal.valueOf(2500.00))
            .shipHasIncreasedManeuverability(true)
            .warningTypes(
                List.of(
                    "Ship requires special pilot",
                    "Ship transports special cargo",
                    "Ship transports dangerous cargo"))
            .cargoManifest(List.of("Steel rods", "Gold", "Diamonds"))
            .callPurpose("Loading")
            .callCount(3)
            .clientDiscountCoefficient(BigDecimal.valueOf(0.2))
            .agencyCommissionCoefficient(BigDecimal.valueOf(0.3))
            .tugServiceProvider(TugServiceProvider.VTC)
            .arrivesFromBulgarianPort(true)
            .build();

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
  }

  @DisplayName("Should return user input for alongside days expected")
  @Test
  void testReturnsUserInputForAlongsideDaysExpected() {
    testRequest.setEstimatedDateOfArrival("ETA not provided");
    testRequest.setEstimatedDateOfDeparture("2021-06-15");
    testRequest.setAlongsideDaysExpected(3);

    final PdaCase product = PdaCaseComposer.composeFrom(testRequest, tariffsFactory);

    assertThat(product.getAlongsideDaysExpected()).isEqualTo(3);
  }

  @DisplayName("Should return null for alongside days expected")
  @ValueSource(strings = {"2021-06-15", "ETD not provided"})
  @ParameterizedTest(name = "input : {arguments}")
  void testReturnsNullForAlongsideDaysExpected(final String value) {
    testRequest.setEstimatedDateOfArrival("ETA not provided");
    testRequest.setEstimatedDateOfDeparture(value);
    testRequest.setAlongsideDaysExpected(null);

    final PdaCase product = PdaCaseComposer.composeFrom(testRequest, tariffsFactory);

    assertThat(product.getAlongsideDaysExpected()).isEqualTo(0);
  }

  @DisplayName("Should calculate alongside days expected")
  @Test
  void testCalculatesAlongsideDaysExpected() {
    testRequest.setEstimatedDateOfArrival("2021-06-15");
    testRequest.setEstimatedDateOfDeparture("2021-06-20");
    testRequest.setAlongsideDaysExpected(3);

    final LocalDate eta = LocalDate.parse("2021-06-15", dateFormatter);
    final LocalDate etd = LocalDate.parse("2021-06-20", dateFormatter);

    final int expected = Math.toIntExact(ChronoUnit.DAYS.between(eta, etd));

    final PdaCase product = PdaCaseComposer.composeFrom(testRequest, tariffsFactory);

    final int result = product.getAlongsideDaysExpected();

    assertThat(expected).isEqualTo(result);
  }
}
