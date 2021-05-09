package flagship.domain.tariffs;

import flagship.domain.cases.dto.PdaCase;
import flagship.domain.cases.entities.Warning;
import flagship.domain.tariffs.serviceduestariffs.MooringDueTariff;
import flagship.domain.tariffs.serviceduestariffs.PilotageDueTariff;
import flagship.domain.tariffs.serviceduestariffs.TugDueTariff;
import flagship.domain.tariffs.stateduestariffs.WharfDueTariff;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static flagship.domain.tariffs.PdaWarningsGenerator.PdaWarning.*;

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

@Component
@RequiredArgsConstructor
public class PdaWarningsGenerator {

  private final PdaCase source;
  private final WharfDueTariff wharfDueTariff;
  private final PilotageDueTariff pilotageDueTariff;
  private final TugDueTariff tugDueTariff;
  private final MooringDueTariff mooringDueTariff;
  private String warningReport;

  public Set<Warning> generateWarnings() {

    final Set<Warning> warnings = new LinkedHashSet<>();

    final LocalDate eta = source.getEstimatedDateOfArrival();
    final LocalDate etd = source.getEstimatedDateOfDeparture();

    if (isHoliday(eta)) {
      warnings.addAll(generateHolidayDetectedWarnings(eta, ETA_IS_HOLIDAY));
    } else {
      warnings.addAll(generateDateNotProvidedWarning(ETA_NOT_PROVIDED));
    }

    if (isHoliday(etd)) {
      warnings.addAll(generateHolidayDetectedWarnings(etd, ETD_IS_HOLIDAY));
    } else {
      warnings.addAll(generateDateNotProvidedWarning(ETD_NOT_PROVIDED));
    }

    return warnings;
  }

  private boolean isHoliday(final LocalDate date) {
    return Optional.ofNullable(date).isPresent()
        && pilotageDueTariff.getHolidayCalendar().contains(date);
  }

  private Set<Warning> generateHolidayDetectedWarnings(
      final LocalDate date, final PdaWarning warning) {

    final Set<Warning> warnings = new HashSet<>();

    Arrays.stream(DueType.values())
        .forEach(
            due ->
                warnings.add(
                    Warning.builder()
                        .id(UUID.randomUUID())
                        .dueType(due)
                        .warningType(warning)
                        .warningDate(date)
                        .warningFactor(getIncreaseCoefficient(due))
                        .dateCreated(Timestamp.valueOf(LocalDateTime.now()))
                        .build()));

    return warnings;
  }

  private Set<Warning> generateDateNotProvidedWarning(final PdaWarning warning) {

    final Set<Warning> warnings = new HashSet<>();

    Arrays.stream(DueType.values())
        .forEach(
            due ->
                warnings.add(
                    Warning.builder()
                        .id(UUID.randomUUID())
                        .dueType(due)
                        .warningType(warning)
                        .dateCreated(Timestamp.valueOf(LocalDateTime.now()))
                        .build()));

    return warnings;
  }

  private BigDecimal getIncreaseCoefficient(final DueType due) {

    BigDecimal increaseCoefficient;

    switch (due) {
      case TUG_DUE:
        increaseCoefficient = tugDueTariff.getIncreaseCoefficientsByWarningType().get(HOLIDAY);
        break;
      case WHARF_DUE:
        increaseCoefficient = BigDecimal.valueOf(1.00);
        break;
      case MOORING_DUE:
        increaseCoefficient = mooringDueTariff.getIncreaseCoefficientsByWarningType().get(HOLIDAY);
        break;
      case PILOTAGE_DUE:
        increaseCoefficient = pilotageDueTariff.getIncreaseCoefficientsByWarningType().get(HOLIDAY);
        break;
      default:
        increaseCoefficient = BigDecimal.ZERO;
    }
    return increaseCoefficient;
  }

  public enum PdaWarning {
    HOLIDAY,
    ETA_NOT_PROVIDED,
    ETD_NOT_PROVIDED,
    ETA_IS_HOLIDAY,
    ETD_IS_HOLIDAY,
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
