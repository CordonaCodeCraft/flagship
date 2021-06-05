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
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static flagship.domain.PdaWarningsGenerator.WarningType.*;
import static flagship.domain.calculators.DueCalculator.CalculatorType.*;
import static flagship.domain.renders.pda.elements.PdaElementsFactory.DueType;
import static flagship.domain.renders.pda.elements.PdaElementsFactory.DueType.*;

// todo: Confirm, that predefining UUID id is not a problem for Hibernate

// todo: Add warning if ship is special and ETA and ETD are not being provided. If stay for more
// than the end of the month
// the exact increase can not be evaluated

// todo: add warning for car due increase for port

@Component
@RequiredArgsConstructor
public class PdaWarningsGenerator {

  private final PdaCase source;
  private final TariffsFactory tariffsFactory;

  public Set<Warning> generateWarnings() {

    Optional<LocalDate> eta = Optional.ofNullable(source.getEstimatedDateOfArrival());
    Optional<LocalDate> etd = Optional.ofNullable(source.getEstimatedDateOfDeparture());

    final Set<Warning> warnings = new LinkedHashSet<>();

    if (eta.isPresent()) {
      if (isHoliday(eta)) {
        warnings.addAll(getHolidayWarnings(eta, ETA_IS_HOLIDAY));
      }
    } else {
      warnings.addAll(getMissingDateWarnings(ETA_NOT_PROVIDED));
    }

    if (etd.isPresent()) {
      if (isHoliday(etd)) {
        warnings.addAll(getHolidayWarnings(etd, ETD_IS_HOLIDAY));
      }
    } else {
      warnings.addAll(getMissingDateWarnings(ETD_NOT_PROVIDED));
    }

    warnings.addAll(getHolidayWarningsForWharfDue(etd));

    return warnings;
  }

  private Set<Warning> getHolidayWarnings(
      final Optional<LocalDate> date, final WarningType warningType) {
    return Stream.of(PILOTAGE_DUE, TUG_DUE, MOORING_DUE)
        .map(due -> getNewWarning(date.get(), warningType, due, getFactor(due)))
        .collect(Collectors.toCollection(LinkedHashSet::new));
  }

  private Set<Warning> getMissingDateWarnings(final WarningType warningType) {
    return Stream.of(PILOTAGE_DUE, TUG_DUE, MOORING_DUE)
        .map(due -> getNewWarning(null, warningType, due, BigDecimal.ZERO))
        .collect(Collectors.toCollection(LinkedHashSet::new));
  }

  private Set<Warning> getHolidayWarningsForWharfDue(final Optional<LocalDate> etd) {

    PilotageDueTariff pilotageDueTariff =
        (PilotageDueTariff) tariffsFactory.getTariff(PILOTAGE_DUE_CALCULATOR);

    final Set<LocalDate> holidays = pilotageDueTariff.getHolidayCalendar();

    final Set<Warning> warnings = new LinkedHashSet<>();

    if (etd.isPresent()) {

      LocalDate date = etd.get();

      if (holidays.contains(date) && !holidays.contains(date.plusDays(1))) {
        warnings.add(getNewWarning(date, HOLIDAY, WHARF_DUE, BigDecimal.ONE));
        return warnings;
      }

      if (holidays.contains(date)) {
        while (holidays.contains(date)) {
          warnings.add(getNewWarning(date, HOLIDAY, WHARF_DUE, BigDecimal.ONE));
          date = date.plusDays(1);
          if (!holidays.contains(date)) {
            break;
          }
        }
      }
    } else {
      warnings.add(
          Warning.builder()
              .dueType(WHARF_DUE)
              .warningType(ETD_NOT_PROVIDED)
              .warningFactor(BigDecimal.ZERO)
              .build());
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

  private boolean isHoliday(final Optional<LocalDate> date) {

    PilotageDueTariff pilotageDueTariff =
        (PilotageDueTariff) tariffsFactory.getTariff(PILOTAGE_DUE_CALCULATOR);

    return date.filter(d -> pilotageDueTariff.getHolidayCalendar().contains(d)).isPresent();
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
}
