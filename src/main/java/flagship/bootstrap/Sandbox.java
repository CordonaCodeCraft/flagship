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
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;

import static flagship.domain.cases.entities.enums.CallPurpose.LOADING;
import static flagship.domain.cases.entities.enums.CargoType.HAZARDOUS;
import static flagship.domain.calculators.tariffs.enums.PilotageArea.VARNA_FIRST;
import static flagship.domain.calculators.tariffs.enums.PortArea.FIRST;
import static flagship.domain.cases.entities.enums.ShipType.GENERAL;
import static flagship.domain.calculators.tariffs.enums.TugArea.VTC_FIFTH;

public class Sandbox {

  public static void main(String[] args) throws JSONException, IOException {

    LocalDate first = LocalDate.of(2021, 1, 29);
    LocalDate second = LocalDate.of(2021, 10, 11);

    int diff = (int) ChronoUnit.MONTHS.between(YearMonth.from(first),YearMonth.from(second));

    int result = diff == 0 ? 1 : diff + 1;

    System.out.println(result);

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
