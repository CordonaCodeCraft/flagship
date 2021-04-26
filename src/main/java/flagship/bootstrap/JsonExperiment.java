package flagship.bootstrap;

import flagship.domain.cases.dto.PdaCase;
import flagship.domain.cases.dto.PdaPort;
import flagship.domain.cases.dto.PdaShip;
import flagship.domain.cases.dto.mappers.CaseMapper;
import flagship.domain.cases.entities.Case;
import org.json.JSONException;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;

import static flagship.domain.cases.entities.enums.CallPurpose.LOADING;
import static flagship.domain.cases.entities.enums.CargoType.HAZARDOUS;
import static flagship.domain.cases.entities.enums.PilotageArea.VARNA_FIRST;
import static flagship.domain.cases.entities.enums.PortArea.FIRST;
import static flagship.domain.cases.entities.enums.ShipType.GENERAL;
import static flagship.domain.cases.entities.enums.TugArea.VTC_FIFTH;

public class JsonExperiment {

  public static void main(String[] args) throws JSONException, IOException {

    PdaShip ship =
        PdaShip.builder()
            .name("Aura")
            .type(GENERAL)
            .lengthOverall(BigDecimal.valueOf(156.25))
            .grossTonnage(BigDecimal.valueOf(2500))
            .requiresSpecialService(true)
            .hasIncreasedManeuverability(true)
            .build();

    PdaPort port =
        PdaPort.builder()
            .name("Varna")
            .area(FIRST)
            .pilotageArea(VARNA_FIRST)
            .tugArea(VTC_FIFTH)
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

    Case entity = CaseMapper.INSTANCE.pdaCaseToCase(pdaCase);

    PdaCase dto = CaseMapper.INSTANCE.caseToPdaCase(entity);

    System.out.println();
  }
}
