package flagship.bootstrap;

import flagship.domain.calculators.servicedues.PilotageDueCalculator;
import flagship.domain.calculators.tariffs.Tariff;
import flagship.domain.calculators.tariffs.serviceduestariffs.PilotageDueTariff;
import flagship.domain.calculators.tariffs.serviceduestariffs.TugDueTariff;
import flagship.domain.cases.dto.PdaCase;
import flagship.domain.cases.dto.PdaPort;
import flagship.domain.cases.dto.PdaShip;
import org.json.JSONException;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;

import static flagship.domain.calculators.tariffs.enums.PortArea.FIRST;
import static flagship.domain.calculators.tariffs.serviceduestariffs.PilotageDueTariff.PilotageArea.VARNA_FIRST;
import static flagship.domain.cases.entities.enums.CallPurpose.LOADING;
import static flagship.domain.cases.entities.enums.CargoType.HAZARDOUS;
import static flagship.domain.cases.entities.enums.ShipType.GENERAL;

public class Sandbox {

  public static void main(String[] args) throws JSONException, IOException {

//    dtoConversions();
  }

  private static void dtoConversions() {
    PdaShip ship =
        PdaShip.builder()
            .name("Aura")
            .type(GENERAL)
            .lengthOverall(BigDecimal.valueOf(156.25))
            .grossTonnage(BigDecimal.valueOf(2500))
            .requiresSpecialPilot(true)
            .hasIncreasedManeuverability(true)
            .build();

    PdaPort port =
        PdaPort.builder()
            .name("Varna")
            .area(FIRST)
            .pilotageArea(VARNA_FIRST)
            .tugArea(TugDueTariff.TugArea.VTC_FIFTH)
            .build();

    PdaCase pdaCase =
        PdaCase.builder()
            .ship(ship)
            .port(port)
            .cargoType(HAZARDOUS)
            .callPurpose(LOADING)
            .callCount(5)
            .alongsideDaysExpected(10)
            .estimatedDateOfArrival(LocalDate.of(2021, 5, 10))
            .estimatedDateOfDeparture(LocalDate.of(2021, 6, 15))
            .build();

    //    List<ECalculator<PdaCase, Tariff>> calculators = new ArrayList<>();
    //    calculators.add(new PilotageTestCalculator());
    //    calculators.add(new TugTestCalculator());
    //
    //    for (ECalculator<PdaCase, Tariff> calculator : calculators) {
    //
    //      String simpleName = calculator.getClass().getSimpleName();
    //
    //      Tariff tariff = returnCalculator(simpleName);
    //
    //      calculator.setFor(pdaCase, tariff);
    //
    //    }

    //    Case entity = CaseMapper.INSTANCE.pdaCaseToCase(pdaCase);
    //
    //    PdaCase dto = CaseMapper.INSTANCE.caseToPdaCase(entity);
    //
    //    System.out.println();
  }

  private static Tariff returnCalculator(String simpleName) {

    if (simpleName.contains("Tug")) {
      return new TugDueTariff();
    }

    if (simpleName.contains("Pilotage")) {
      return new PilotageDueTariff();
    }

    return null;
  }
}
