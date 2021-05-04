package flagship.domain.tariffs;

import flagship.domain.cases.dto.PdaCase;
import flagship.domain.cases.entities.Warning;
import flagship.domain.tariffs.serviceduestariffs.MooringDueTariff;
import flagship.domain.tariffs.serviceduestariffs.PilotageDueTariff;
import flagship.domain.tariffs.serviceduestariffs.TugDueTariff;
import flagship.domain.tariffs.stateduestariffs.WharfDueTariff;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

import static flagship.domain.tariffs.PdaWarningsGenerator.DueType.PILOTAGE_DUE;
import static flagship.domain.tariffs.PdaWarningsGenerator.PdaWarning.HOLIDAY;

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

@Component
@RequiredArgsConstructor
public class PdaWarningsGenerator {

  private final WharfDueTariff wharfDueTariff;
  private final PilotageDueTariff pilotageDueTariff;
  private final TugDueTariff tugDueTariff;
  private final MooringDueTariff mooringDueTariff;
  private String warningReport;

  public String generateWarningReport(final PdaCase source) {
    return warningReport;
  }

  public Set<Warning> generateWarnings(final PdaCase source) {

    final Set<Warning> warnings = new HashSet<>();

    warnings.add(pilotageDueEtaDetected(source));

    return warnings;
  }

  private Warning pilotageDueEtaDetected(PdaCase source) {

    Warning warning =
        Warning.builder()
            .dueType(PILOTAGE_DUE)
            .warningType(HOLIDAY)
            .warningDate(source.getEstimatedDateOfArrival())
            .warningCoefficient(
                pilotageDueTariff.getIncreaseCoefficientsByPdaWarning().get(HOLIDAY))
            .build();

    return null;
  }

  public enum PdaWarning {
    HOLIDAY,
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

// todo: generate warnings for mooring due
