package flagship.domain.utils.calculators.servicedues;

import flagship.domain.cases.entities.Case;
import flagship.domain.cases.entities.Warning;
import flagship.domain.utils.tariffs.serviceduestariffs.HolidayCalendar;
import flagship.domain.utils.tariffs.serviceduestariffs.PilotageDueTariff;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static flagship.domain.utils.tariffs.serviceduestariffs.PdaWarning.HOLIDAY;
import static flagship.domain.utils.tariffs.serviceduestariffs.PdaWarning.PILOT;

//todo: Confirm, that predefining UUID id is not a problem for Hibernate
public class PilotageDueWarningsGenerator {

    private static final String DUE_TYPE = "Pilotage due";

    public Set<Warning> generateWarnings(Case source, HolidayCalendar holidays, PilotageDueTariff tariff) {

        Set<Warning> warnings = new HashSet<>();

        LocalDate estimatedDateOfArrival = source.getEstimatedDateOfArrival();
        LocalDate estimatedDateOfDeparture = source.getEstimatedDateOfDeparture();

        if (holidays.getHolidayCalendar().contains(estimatedDateOfArrival)) {
            Warning arrivalWarning = Warning
                    .builder()
                    .id(UUID.randomUUID())
                    .dueType(DUE_TYPE)
                    .warningDate(estimatedDateOfArrival)
                    .warningCoefficient(tariff.getIncreaseCoefficientsByWarningType().get(HOLIDAY))
                    .build();
            warnings.add(arrivalWarning);
        }

        if (holidays.getHolidayCalendar().contains(estimatedDateOfDeparture)) {
            Warning departureWarning = Warning
                    .builder()
                    .id(UUID.randomUUID())
                    .dueType(DUE_TYPE)
                    .warningDate(estimatedDateOfDeparture)
                    .warningCoefficient(tariff.getIncreaseCoefficientsByWarningType().get(HOLIDAY))
                    .build();
            warnings.add(departureWarning);
        }

        if (source.getShip().getRequiresAdditionalService()) {
            Warning pilotWarning = Warning
                    .builder()
                    .id(UUID.randomUUID())
                    .dueType(DUE_TYPE)
                    .warningCoefficient(tariff.getIncreaseCoefficientsByWarningType().get(PILOT))
                    .build();
            warnings.add(pilotWarning);
        }

        return warnings;
    }
}
