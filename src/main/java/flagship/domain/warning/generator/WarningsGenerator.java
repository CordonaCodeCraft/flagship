package flagship.domain.warning.generator;

import flagship.domain.calculation.tariffs.TariffsFactory;
import flagship.domain.calculation.tariffs.service.MooringDueTariff;
import flagship.domain.calculation.tariffs.service.PilotageDueTariff;
import flagship.domain.calculation.tariffs.service.TugDueTariff;
import flagship.domain.caze.model.PdaCase;
import flagship.domain.warning.entity.Warning;
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

import static flagship.domain.calculation.calculators.DueCalculator.CalculatorType.*;
import static flagship.domain.pda.render.elements.PdaElementsFactory.DueType;
import static flagship.domain.pda.render.elements.PdaElementsFactory.DueType.*;
import static flagship.domain.warning.generator.WarningsGenerator.WarningType.*;

// todo: Confirm, that predefining UUID id is not a problem for Hibernate

// todo: Add warning if ship is special and ETA and ETD are not being provided. If stay for more
// than the end of the month
// the exact increase can not be evaluated

// todo: add warning for car due increase for port

@Component
@RequiredArgsConstructor
public class WarningsGenerator {

  private final PdaCase source;
  private final TariffsFactory tariffsFactory;

  public Set<Warning> generateWarnings() {

    final Optional<LocalDate> eta = Optional.ofNullable(source.getEstimatedDateOfArrival());
    final Optional<LocalDate> etd = Optional.ofNullable(source.getEstimatedDateOfDeparture());

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

    final PilotageDueTariff pilotageDueTariff =
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

    final TugDueTariff tugDueTariff = (TugDueTariff) tariffsFactory.getTariff(TUG_DUE_CALCULATOR);

    final MooringDueTariff mooringDueTariff =
        (MooringDueTariff) tariffsFactory.getTariff(MOORING_DUE_CALCULATOR);

    final PilotageDueTariff pilotageDueTariff =
        (PilotageDueTariff) tariffsFactory.getTariff(PILOTAGE_DUE_CALCULATOR);

    final BigDecimal factor;

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

    final PilotageDueTariff pilotageDueTariff =
        (PilotageDueTariff) tariffsFactory.getTariff(PILOTAGE_DUE_CALCULATOR);

    return date.filter(d -> pilotageDueTariff.getHolidayCalendar().contains(d)).isPresent();
  }

  public enum WarningType {
    HOLIDAY("Date is holiday"),
    ETA_IS_HOLIDAY("ETA is holiday"),
    ETD_IS_HOLIDAY("ETD is holiday"),
    ETA_NOT_PROVIDED("ETA is not provided"),
    ETD_NOT_PROVIDED("ETD is not provided"),
    SPECIAL_PILOT("Ship requires special pilot"),
    HAZARDOUS_PILOTAGE_CARGO("Ship transports hazardous cargo"),
    SPECIAL_PILOTAGE_CARGO("Ship transports special cargo"),
    DANGEROUS_TUG_CARGO("Ship transports dangerous cargo"),
    ;

    public final String type;

    WarningType(final String name) {
      type = name;
    }
  }
}
