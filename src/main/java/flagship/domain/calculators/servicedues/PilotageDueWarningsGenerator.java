package flagship.domain.calculators.servicedues;

import flagship.domain.calculators.HolidayCalendar;
import flagship.domain.calculators.tariffs.serviceduestariffs.PilotageDueTariff;
import flagship.domain.cases.dto.PdaCase;
import flagship.domain.cases.entities.Warning;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static flagship.domain.calculators.tariffs.enums.PdaWarning.HOLIDAY;

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

//todo: generate warnings for mooring due

public class PilotageDueWarningsGenerator {

  private static final String PILOTAGE_DUE = "Pilotage due";
  private static final String WHARF_DUE = "Wharf due";

  public Set<Warning> generateWarnings(
      final PdaCase source, final HolidayCalendar holidays, final PilotageDueTariff tariff) {

    final Set<Warning> warnings = new HashSet<>();

    final Optional<LocalDate> estimatedDateOfDeparture =
        Optional.ofNullable(source.getEstimatedDateOfDeparture());

    if (Optional.ofNullable(source.getEstimatedDateOfArrival()).isPresent()) {
      if (holidays.getHolidayCalendar().contains(source.getEstimatedDateOfArrival())) {
        final Warning arrivalWarning =
            Warning.builder()
                .id(UUID.randomUUID())
                .message(String.format("Expect due increase of 50 percent"))
                .warningDate(source.getEstimatedDateOfArrival())
                .warningCoefficient(tariff.getIncreaseCoefficientsByWarningType().get(HOLIDAY))
                .build();
        warnings.add(arrivalWarning);
      }
    } else {
      //      Warning.builder()
      //              .id(UUID.randomUUID())
      //              .dueType(PILOTAGE_DUE)
      //              .warningDate(estimatedDateOfArrival.get())
      //
      // .warningCoefficient(tariff.getIncreaseCoefficientsByWarningType().get(HOLIDAY))
      //              .build();
      //      warnings.add(arrivalWarning);

    }

    if (holidays.getHolidayCalendar().contains(estimatedDateOfDeparture)) {
      //      final Warning departureWarning =
      //          Warning.builder()
      //              .id(UUID.randomUUID())
      //              .dueType(PILOTAGE_DUE)
      //              .warningDate(estimatedDateOfDeparture)
      //
      // .warningCoefficient(tariff.getIncreaseCoefficientsByWarningType().get(HOLIDAY))
      //              .build();
      //      warnings.add(departureWarning);
    }

    if (source.getShip().getRequiresSpecialPilot()) {
      //      final Warning pilotWarning =
      //          Warning.builder()
      //              .id(UUID.randomUUID())
      //              .dueType(PILOTAGE_DUE)
      //              .warningCoefficient(tariff.getIncreaseCoefficientsByWarningType().get(PILOT))
      //              .build();
      //      warnings.add(pilotWarning);
    }

    return warnings;
  }
}
