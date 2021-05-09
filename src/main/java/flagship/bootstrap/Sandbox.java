package flagship.bootstrap;

import flagship.domain.cases.dto.PdaCase;
import flagship.domain.cases.dto.PdaPort;
import flagship.domain.cases.dto.PdaShip;
import flagship.domain.tariffs.Tariff;
import flagship.domain.tariffs.serviceduestariffs.PilotageDueTariff;
import flagship.domain.tariffs.serviceduestariffs.TugDueTariff;
import org.json.JSONException;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;

import static flagship.domain.cases.entities.enums.CallPurpose.LOADING;
import static flagship.domain.cases.entities.enums.ShipType.GENERAL;
import static flagship.domain.tariffs.serviceduestariffs.PilotageDueTariff.PilotageArea.VARNA_FIRST;
import static flagship.domain.tariffs.stateduestariffs.PortArea.FIRST;

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
            .hasIncreasedManeuverability(true)
            .build();

    PdaPort port =
        PdaPort.builder()
            .name("Varna")
            .portArea(FIRST)
            .pilotageArea(VARNA_FIRST)
            .tugArea(TugDueTariff.TugArea.VTC_FIFTH)
            .build();

    PdaCase pdaCase =
        PdaCase.builder()
            .ship(ship)
            .port(port)
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
