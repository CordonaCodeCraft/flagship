package flagship.domain.calculators;

import flagship.domain.PdaWarningsGenerator;
import flagship.domain.PdaWarningsGenerator.WarningType;
import flagship.domain.TariffsFactory;
import flagship.domain.cases.dto.PdaCase;
import flagship.domain.tariffs.Tariff;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Map;

import static flagship.domain.PdaWarningsGenerator.WarningType.*;
import static flagship.domain.calculators.DueCalculator.CalculatorType;
import static flagship.domain.calculators.DueCalculator.CalculatorType.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Disabled("Disabled until the warning model is clarified")
@DisplayName("Pilotage due warning generator tests")
class PdaWarningsGeneratorTest extends TariffsInitializer {

  private static TariffsFactory tariffsFactory;
  private static PdaWarningsGenerator warningsGenerator;
  PdaCase testCase;

  @BeforeEach
  void setUp() {
    Map<CalculatorType, Tariff> tariffs = new EnumMap<>(CalculatorType.class);
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

    testCase =
        PdaCase.builder()
            .estimatedDateOfArrival(LocalDate.now())
            .estimatedDateOfDeparture(LocalDate.now().plusDays(5))
            .warnings(new HashSet<>())
            .build();
  }

  @DisplayName("Should generate 4 eta detected warnings")
  @Test
  void testGeneratesEtaDetectedWarnings() {

    testCase.getWarningTypes().addAll(EnumSet.of(ETA_IS_HOLIDAY, ETD_NOT_PROVIDED));

    warningsGenerator = new PdaWarningsGenerator(testCase, tariffsFactory);

    final long warningsCount = getCountForWarning(ETA_IS_HOLIDAY);

    assertThat(warningsCount).isEqualTo(4);
  }

  @DisplayName("Should generate 4 etd detected warnings")
  @Test
  void testGeneratesEtdDetectedWarnings() {

    testCase.getWarningTypes().addAll(EnumSet.of(ETD_IS_HOLIDAY, ETA_NOT_PROVIDED));

    warningsGenerator = new PdaWarningsGenerator(testCase, tariffsFactory);

    long warningsCount = getCountForWarning(ETD_IS_HOLIDAY);

    assertThat(warningsCount).isEqualTo(4);
  }

  @DisplayName("Should generate 8 holiday detected warnings")
  @Test
  void testGeneratesEtaAndEtdDetectedWarnings() {

    testCase.getWarningTypes().addAll(EnumSet.of(ETA_IS_HOLIDAY, ETD_IS_HOLIDAY));

    warningsGenerator = new PdaWarningsGenerator(testCase, tariffsFactory);

    final long warningsCount =
        warningsGenerator.generateWarnings().stream()
            .filter(
                warning ->
                    warning.getWarningType() == ETA_IS_HOLIDAY
                        || warning.getWarningType() == ETD_IS_HOLIDAY)
            .count();

    assertThat(warningsCount).isEqualTo(8);
  }

  @DisplayName("Should generate 4 eta not provided warnings")
  @Test
  void testGeneratesEtaNotProvidedWarnings() {

    testCase.getWarningTypes().addAll(EnumSet.of(ETA_NOT_PROVIDED, ETD_IS_HOLIDAY));

    warningsGenerator = new PdaWarningsGenerator(testCase, tariffsFactory);

    final long warningsCount = getCountForWarning(ETA_NOT_PROVIDED);

    assertThat(warningsCount).isEqualTo(4);
  }

  @DisplayName("Should generate 4 etd not provided warnings")
  @Test
  void testGeneratesEtdNotProvidedWarnings() {

    testCase.getWarningTypes().addAll(EnumSet.of(ETD_NOT_PROVIDED, ETA_IS_HOLIDAY));

    warningsGenerator = new PdaWarningsGenerator(testCase, tariffsFactory);

    final long warningsCount = getCountForWarning(ETD_NOT_PROVIDED);

    assertThat(warningsCount).isEqualTo(4);
  }

  @DisplayName("Should generate 8 eta and etd not provided warnings")
  @Test
  void testGeneratesEtaAndEtdNotProvidedWarnings() {

    testCase.getWarningTypes().addAll(EnumSet.of(ETA_NOT_PROVIDED, ETD_NOT_PROVIDED));

    warningsGenerator = new PdaWarningsGenerator(testCase, tariffsFactory);

    final long warningsCount =
        warningsGenerator.generateWarnings().stream()
            .filter(
                warning ->
                    warning.getWarningType() == ETA_NOT_PROVIDED
                        || warning.getWarningType() == ETD_NOT_PROVIDED)
            .count();

    assertThat(warningsCount).isEqualTo(8);
  }

  @Test
  void testGenerateIntermediateHolidayWarnings() {
    testCase.getWarningTypes().addAll(EnumSet.of(ETA_IS_HOLIDAY, ETD_IS_HOLIDAY));
  }

  private long getCountForWarning(final WarningType warning) {
    return warningsGenerator.generateWarnings().stream()
        .filter(w -> w.getWarningType() == warning)
        .count();
  }
}
