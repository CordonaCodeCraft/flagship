package flagship.domain.calculation.calculators;

import flagship.domain.calculation.tariffs.Tariff;
import flagship.domain.caze.model.PdaCase;
import lombok.Getter;

import java.math.BigDecimal;

public interface DueCalculator {

  void set(PdaCase source, Tariff tariff);

  BigDecimal calculate();

  @Getter
  enum CalculatorType {
    TONNAGE_DUE_CALCULATOR("tonnageDue"),
    WHARF_DUE_CALCULATOR("wharfDue"),
    CANAL_DUE_CALCULATOR("canalDue"),
    LIGHT_DUE_CALCULATOR("lightDue"),
    MARPOL_DUE_CALCULATOR("marpolDue"),
    MOORING_DUE_CALCULATOR("mooringDue"),
    BOOM_CONTAINMENT_DUE_CALCULATOR("boomContainmentDue"),
    SAILING_PERMISSION_CALCULATOR("sailingPermissionDue"),
    PILOTAGE_DUE_CALCULATOR("pilotageDue"),
    TUG_DUE_CALCULATOR("tugDue"),
    BASIC_AGENCY_DUE_CALCULATOR("basicAgencyDue"),
    BANK_EXPENSES_DUE_CALCULATOR("bankExpensesDue"),
    CARS_DUE_CALCULATOR("carsDue"),
    CLEARANCE_DUE_CALCULATOR("clearanceDue"),
    COMMUNICATIONS_DUE_CALCULATOR("communicationsDue"),
    OVERTIME_DUE_CALCULATOR("agencyOvertimeDue");

    private final String type;

    CalculatorType(final String type) {
      this.type = type;
    }
  }
}
