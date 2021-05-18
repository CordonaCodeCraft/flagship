package flagship.domain;

import flagship.domain.cases.dto.PdaCase;
import flagship.domain.cases.entities.Warning;
import flagship.domain.factories.TariffsFactory;
import flagship.domain.tariffs.servicedues.MooringDueTariff;
import flagship.domain.tariffs.servicedues.PilotageDueTariff;
import flagship.domain.tariffs.servicedues.TugDueTariff;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static flagship.domain.PdaWarningsGenerator.DueType.*;
import static flagship.domain.PdaWarningsGenerator.WarningType.*;
import static flagship.domain.calculators.DueCalculator.CalculatorType.*;

// todo: Confirm, that predefining UUID id is not a problem for Hibernate

// todo: add warning that the PDA is not precise at calculating wharf, pilotage and tug dues and
// moorning/unmoorning
// because ETA and ETD are not provided

// todo: Add warning regarding the wharf due - if between ETA and ETD - the stay of the ship will be
// increased.
// This must increase the total and must generate warning. The warning message will say, that the
// expected wharf due
// is increased.

// todo: Add warning if ship is special and ETA and ETD are not being provided. If stay for more
// than the end of the month
// the exact increase can not be evaluated

// todo: generate warnings for mooring due

// If alongsideDays not provided - the agent will enter value based on his experiences.

// todo: add warning for car due increase for port

@Component
@RequiredArgsConstructor
public class PdaWarningsGenerator {

  private final PdaCase source;
  private final TariffsFactory tariffsFactory;

  public Set<Warning> generateWarnings() {

    LocalDate eta = source.getEstimatedDateOfArrival();
    LocalDate etd = source.getEstimatedDateOfDeparture();

    final Set<Warning> warnings = new LinkedHashSet<>();

    if (source.getWarningTypes().contains(ETA_IS_HOLIDAY)) {
      warnings.addAll(getHolidayWarnings(eta, ETA_IS_HOLIDAY));
    }
    if (source.getWarningTypes().contains(ETD_IS_HOLIDAY)) {
      warnings.addAll(getHolidayWarnings(etd, ETD_IS_HOLIDAY));
    }
    if (source.getWarningTypes().contains(ETA_NOT_PROVIDED)) {
      warnings.addAll(getDateMissingWarnings(ETA_NOT_PROVIDED));
    }
    if (source.getWarningTypes().contains(ETD_NOT_PROVIDED)) {
      warnings.addAll(getDateMissingWarnings(ETD_NOT_PROVIDED));
    }
    if (source.getWarningTypes().contains(ETA_IS_HOLIDAY)
        && source.getWarningTypes().contains(ETD_IS_HOLIDAY)) {
      warnings.addAll(getIntermediateHolidayWarningsForWharfDue(eta, etd));
    }

    return warnings;
  }

  private Set<Warning> getHolidayWarnings(final LocalDate date, final WarningType warningType) {
    return Stream.of(PILOTAGE_DUE, TUG_DUE, MOORING_DUE, WHARF_DUE)
        .map(due -> getNewWarning(date, warningType, due, getFactor(due)))
        .collect(Collectors.toCollection(LinkedHashSet::new));
  }

  private Set<Warning> getDateMissingWarnings(final WarningType warningType) {
    return Stream.of(PILOTAGE_DUE, TUG_DUE, MOORING_DUE, WHARF_DUE)
        .map(due -> getNewWarning(null, warningType, due, BigDecimal.ZERO))
        .collect(Collectors.toCollection(LinkedHashSet::new));
  }

  private Set<Warning> getIntermediateHolidayWarningsForWharfDue(
      final LocalDate eta, final LocalDate etd) {

    PilotageDueTariff pilotageDueTariff =
        (PilotageDueTariff) tariffsFactory.getTariff(PILOTAGE_DUE_CALCULATOR);

    final Set<Warning> warnings = new LinkedHashSet<>();
    final Set<LocalDate> holidays = pilotageDueTariff.getHolidayCalendar();

    LocalDate date = eta.plusDays(1);

    while (!date.equals(etd)) {
      if (holidays.contains(date)) {
        warnings.add(getNewWarning(date, HOLIDAY, WHARF_DUE, BigDecimal.ONE));
      }
      date = date.plusDays(1);
    }

    return warnings;
  }

  private BigDecimal getFactor(final DueType due) {

    TugDueTariff tugDueTariff = (TugDueTariff) tariffsFactory.getTariff(TUG_DUE_CALCULATOR);
    MooringDueTariff mooringDueTariff =
        (MooringDueTariff) tariffsFactory.getTariff(MOORING_DUE_CALCULATOR);
    PilotageDueTariff pilotageDueTariff =
        (PilotageDueTariff) tariffsFactory.getTariff(PILOTAGE_DUE_CALCULATOR);

    BigDecimal factor;

    switch (due) {
      case TUG_DUE:
        factor = tugDueTariff.getIncreaseCoefficientsByWarningType().get(HOLIDAY);
        break;
      case MOORING_DUE:
        factor = mooringDueTariff.getIncreaseCoefficientsByWarningType().get(HOLIDAY);
        break;
      case PILOTAGE_DUE:
        factor = pilotageDueTariff.getIncreaseCoefficientsByWarningType().get(HOLIDAY);
        break;
      case WHARF_DUE:
        factor = BigDecimal.ONE;
        break;
      default:
        factor = BigDecimal.ZERO;
        break;
    }
    return factor;
  }

  private Warning getNewWarning(
      final LocalDate date,
      final WarningType warningType,
      final DueType dueType,
      final BigDecimal factor) {
    return Warning.builder()
        .id(UUID.randomUUID())
        .dueType(dueType)
        .warningType(warningType)
        .warningDate(date)
        .warningFactor(factor)
        .dateCreated(Timestamp.valueOf(LocalDateTime.now()))
        .build();
  }

  public enum WarningType {
    HOLIDAY,
    ETA_IS_HOLIDAY,
    ETD_IS_HOLIDAY,
    ETA_NOT_PROVIDED,
    ETD_NOT_PROVIDED,
    SPECIAL_PILOT,
    HAZARDOUS_PILOTAGE_CARGO,
    SPECIAL_PILOTAGE_CARGO,
    DANGEROUS_TUG_CARGO,
  }

  public enum DueType {
    TUG_DUE("Tug due"),
    WHARF_DUE("Wharf due"),
    MOORING_DUE("Mooring due"),
    PILOTAGE_DUE("Pilotage due");

    public final String type;

    DueType(String name) {
      this.type = name;
    }
  }
}
