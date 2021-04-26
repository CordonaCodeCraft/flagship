package flagship.domain.calculators.servicedues;

import flagship.domain.calculators.HolidayCalendar;
import flagship.domain.calculators.tariffs.serviceduestariffs.PilotageDueTariff;
import flagship.domain.cases.dto.PdaCase;
import flagship.domain.cases.entities.Case;
import flagship.domain.cases.entities.Warning;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static flagship.domain.cases.entities.enums.PdaWarning.HOLIDAY;
import static flagship.domain.cases.entities.enums.PdaWarning.PILOT;

// todo: Confirm, that predefining UUID id is not a problem for Hibernate

// todo: add warning that the PDA is not precise at calculating wharf, pilotage and tug dues
// because ETA and ETD are not provided

// todo: Add warning regarding the wharf due - if between ETA and ETD - the stay of the ship will be
// increased.
// This must increase the total and must generate warning. The warning message will say, that the
// expected wharf due
// is increased.

public class PilotageDueWarningsGenerator {

  private static final String DUE_TYPE = "Pilotage due";

  public Set<Warning> generateWarnings(
          final PdaCase source, final HolidayCalendar holidays, final PilotageDueTariff tariff) {

    final Set<Warning> warnings = new HashSet<>();

    final LocalDate estimatedDateOfArrival = source.getEstimatedDateOfArrival();
    final LocalDate estimatedDateOfDeparture = source.getEstimatedDateOfDeparture();

    if (holidays.getHolidayCalendar().contains(estimatedDateOfArrival)) {
      final Warning arrivalWarning =
          Warning.builder()
              .id(UUID.randomUUID())
              .dueType(DUE_TYPE)
              .warningDate(estimatedDateOfArrival)
              .warningCoefficient(tariff.getIncreaseCoefficientsByWarningType().get(HOLIDAY))
              .build();
      warnings.add(arrivalWarning);
    }

    if (holidays.getHolidayCalendar().contains(estimatedDateOfDeparture)) {
      final Warning departureWarning =
          Warning.builder()
              .id(UUID.randomUUID())
              .dueType(DUE_TYPE)
              .warningDate(estimatedDateOfDeparture)
              .warningCoefficient(tariff.getIncreaseCoefficientsByWarningType().get(HOLIDAY))
              .build();
      warnings.add(departureWarning);
    }

    if (source.getShip().getRequiresSpecialService()) {
      final Warning pilotWarning =
          Warning.builder()
              .id(UUID.randomUUID())
              .dueType(DUE_TYPE)
              .warningCoefficient(tariff.getIncreaseCoefficientsByWarningType().get(PILOT))
              .build();
      warnings.add(pilotWarning);
    }

    return warnings;
  }
}
