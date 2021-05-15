package flagship;

import flagship.domain.cases.dto.PdaCase;
import flagship.domain.cases.dto.PdaPort;
import flagship.domain.cases.dto.PdaShip;
import flagship.domain.cases.dto.mappers.CaseMapper;
import flagship.domain.cases.entities.Case;
import flagship.domain.cases.entities.Ship;
import flagship.domain.tariffs.PilotageDueTariff;
import flagship.domain.tariffs.Tariff;
import flagship.domain.tariffs.TugDueTariff;
import org.json.JSONException;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;

import static flagship.domain.cases.entities.enums.CallPurpose.LOADING;
import static flagship.domain.cases.entities.enums.ShipType.GENERAL;
import static flagship.domain.tariffs.PilotageDueTariff.PilotageArea.VARNA_FIRST;
import static flagship.domain.tariffs.PortArea.FIRST;

public class Sandbox {

  public static void main(String[] args) throws JSONException, IOException {

//        dtoConversions();
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

    Case newCase = CaseMapper.INSTANCE.pdaCaseToCase(pdaCase);

    System.out.println();
  }

}
