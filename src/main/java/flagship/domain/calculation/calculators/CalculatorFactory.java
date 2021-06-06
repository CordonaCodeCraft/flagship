package flagship.domain.calculation.calculators;

import flagship.domain.calculation.calculators.agency.*;
import flagship.domain.calculation.calculators.service.MooringDueCalculator;
import flagship.domain.calculation.calculators.service.PilotageDueCalculator;
import flagship.domain.calculation.calculators.service.TugDueCalculator;
import flagship.domain.calculation.calculators.state.*;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static flagship.domain.calculation.calculators.DueCalculator.CalculatorType;
import static flagship.domain.calculation.calculators.DueCalculator.CalculatorType.*;

@Component
public class CalculatorFactory {

  public DueCalculator getCalculator(final CalculatorType calculatorType) {

    Map<CalculatorType, DueCalculator> calculators = new HashMap<>();

    calculators.put(TONNAGE_DUE_CALCULATOR, new TonnageDueCalculator());
    calculators.put(WHARF_DUE_CALCULATOR, new WharfDueCalculator());
    calculators.put(CANAL_DUE_CALCULATOR, new CanalDueCalculator());
    calculators.put(LIGHT_DUE_CALCULATOR, new LightDueCalculator());
    calculators.put(MARPOL_DUE_CALCULATOR, new MarpolDueCalculator());
    calculators.put(MOORING_DUE_CALCULATOR, new MooringDueCalculator());
    calculators.put(BOOM_CONTAINMENT_DUE_CALCULATOR, new BoomContainmentCalculator());
    calculators.put(CLEARANCE_DUE_CALCULATOR, new ClearanceDueCalculator());
    calculators.put(SAILING_PERMISSION_CALCULATOR, new SailingPermissionCalculator());
    calculators.put(PILOTAGE_DUE_CALCULATOR, new PilotageDueCalculator());
    calculators.put(TUG_DUE_CALCULATOR, new TugDueCalculator());
    calculators.put(BASIC_AGENCY_DUE_CALCULATOR, new BasicAgencyDueCalculator());
    calculators.put(CARS_DUE_CALCULATOR, new CarsDueCalculator());
    calculators.put(COMMUNICATIONS_DUE_CALCULATOR, new CommunicationsDueCalculator());
    calculators.put(BANK_EXPENSES_DUE_CALCULATOR, new BankExpensesDueCalculator());
    calculators.put(OVERTIME_DUE_CALCULATOR, new OvertimeDueCalculator());

    return calculators.entrySet().stream()
        .filter(e -> e.getKey() == calculatorType)
        .map(Map.Entry::getValue)
        .findFirst()
        .get();
  }
}
