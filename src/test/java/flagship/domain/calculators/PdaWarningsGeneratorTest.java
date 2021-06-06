package flagship.domain.calculators;

import flagship.domain.PdaWarningsGenerator;
import flagship.domain.PdaWarningsGenerator.WarningType;
import flagship.domain.cases.dto.PdaCase;
import flagship.domain.cases.entities.Warning;
import flagship.domain.tariffs.TariffsFactory;
import flagship.domain.tariffs.Tariff;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static flagship.domain.PdaWarningsGenerator.WarningType.*;
import static flagship.domain.calculators.DueCalculator.CalculatorType;
import static flagship.domain.calculators.DueCalculator.CalculatorType.*;
import static flagship.domain.renders.pda.elements.PdaElementsFactory.DueType.WHARF_DUE;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("Pilotage due warning generator tests")
class PdaWarningsGeneratorTest extends TariffsInitializer {

  private static final TariffsFactory tariffsFactory = new TariffsFactory();
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

    testCase = PdaCase.builder().warningTypes(new HashSet<>()).warnings(new HashSet<>()).build();
  }

  @DisplayName("Should generate 3 eta detected warnings")
  @Test
  void testGeneratesEtaDetectedWarnings() {

    testCase.setEstimatedDateOfArrival(LocalDate.of(2021, 12, 28));

    warningsGenerator = new PdaWarningsGenerator(testCase, tariffsFactory);

    final long warningsCount = getCountForWarning(ETA_IS_HOLIDAY);

    assertThat(warningsCount).isEqualTo(3);
  }

  @DisplayName("Should generate 3 etd detected warnings")
  @Test
  void testGeneratesEtdDetectedWarnings() {

    testCase.setEstimatedDateOfDeparture(LocalDate.of(2021, 12, 28));

    warningsGenerator = new PdaWarningsGenerator(testCase, tariffsFactory);

    long warningsCount = getCountForWarning(ETD_IS_HOLIDAY);

    assertThat(warningsCount).isEqualTo(3);
  }

  @DisplayName("Should generate 6 holiday detected warnings")
  @Test
  void testGeneratesEtaAndEtdDetectedWarnings() {

    testCase.setEstimatedDateOfArrival(LocalDate.of(2021, 12, 28));
    testCase.setEstimatedDateOfDeparture(LocalDate.of(2021, 12, 28));

    warningsGenerator = new PdaWarningsGenerator(testCase, tariffsFactory);

    final long warningsCount =
        warningsGenerator.generateWarnings().stream()
            .filter(
                warning ->
                    warning.getWarningType() == ETA_IS_HOLIDAY
                        || warning.getWarningType() == ETD_IS_HOLIDAY)
            .count();

    assertThat(warningsCount).isEqualTo(6);
  }

  @DisplayName("Should generate 3 eta not provided warnings")
  @Test
  void testGeneratesEtaNotProvidedWarnings() {

    testCase.setEstimatedDateOfDeparture(LocalDate.of(2021, 12, 28));

    warningsGenerator = new PdaWarningsGenerator(testCase, tariffsFactory);

    final long warningsCount = getCountForWarning(ETA_NOT_PROVIDED);

    assertThat(warningsCount).isEqualTo(3);
  }

  @DisplayName("Should generate 4 etd not provided warnings")
  @Test
  void testGeneratesEtdNotProvidedWarnings() {

    testCase.setEstimatedDateOfArrival(LocalDate.of(2021, 12, 28));

    warningsGenerator = new PdaWarningsGenerator(testCase, tariffsFactory);

    final long warningsCount = getCountForWarning(ETD_NOT_PROVIDED);

    assertThat(warningsCount).isEqualTo(4);
  }

  @DisplayName("Should generate 7 eta and etd not provided warnings")
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

    assertThat(warningsCount).isEqualTo(7);
  }

  @DisplayName("Should generate 1 warning message")
  @Test
  void testGeneratesOneHolidayWarningForWharfDue() {

    testCase.setEstimatedDateOfArrival(LocalDate.of(2021, 5, 1));
    testCase.setEstimatedDateOfDeparture(LocalDate.of(2021, 5, 4));

    warningsGenerator = new PdaWarningsGenerator(testCase, tariffsFactory);

    Set<Warning> warnings =
        warningsGenerator.generateWarnings().stream()
            .filter(warning -> warning.getDueType() == WHARF_DUE)
            .collect(Collectors.toSet());

    assertThat(warnings.size()).isEqualTo(1);
  }

  @DisplayName("Should generate 3 warning message")
  @Test
  void testGenerateMultipleHolidayWarningForWharfDue() {

    testCase.setEstimatedDateOfDeparture(LocalDate.of(2021, 5, 2));

    warningsGenerator = new PdaWarningsGenerator(testCase, tariffsFactory);

    Set<Warning> warnings =
        warningsGenerator.generateWarnings().stream()
            .filter(warning -> warning.getDueType() == WHARF_DUE)
            .collect(Collectors.toSet());

    assertThat(warnings.size()).isEqualTo(3);
  }

  @DisplayName("Should generate 1 warning message for missing ETD")
  @Test
  void testGeneratesOneWarningForMissingEtdWharfDue() {

    warningsGenerator = new PdaWarningsGenerator(testCase, tariffsFactory);

    Set<Warning> warnings =
        warningsGenerator.generateWarnings().stream()
            .filter(warning -> warning.getDueType() == WHARF_DUE)
            .collect(Collectors.toSet());

    assertThat(warnings.size()).isEqualTo(1);
  }

  private long getCountForWarning(final WarningType warning) {
    return warningsGenerator.generateWarnings().stream()
        .filter(w -> w.getWarningType() == warning)
        .count();
  }
}
