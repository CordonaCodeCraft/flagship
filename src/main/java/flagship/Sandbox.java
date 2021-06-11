package flagship;

import com.fasterxml.jackson.databind.ObjectMapper;
import flagship.domain.caze.entity.Case;
import flagship.domain.caze.mapper.CaseMapper;
import flagship.domain.caze.model.PdaCase;
import flagship.domain.caze.model.request.CreateCaseRequest;
import flagship.domain.port.model.PdaPort;
import flagship.domain.ship.model.PdaShip;
import org.json.JSONException;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static flagship.domain.caze.entity.Case.CallPurpose.LOADING;
import static flagship.domain.caze.model.request.resolvers.PilotageAreaResolver.PilotageArea.VARNA_FIRST;
import static flagship.domain.caze.model.request.resolvers.PortAreaResolver.PortArea.FIRST;
import static flagship.domain.caze.model.request.resolvers.TugAreaResolver.TugArea;
import static flagship.domain.caze.model.request.resolvers.TugAreaResolver.TugServiceProvider;
import static flagship.domain.ship.entity.Ship.ShipType.BULK_CARRIER;

public class Sandbox {

  public static final String LOGO = "D:/Gdrive/Inbox/neftoil.png";
  public static final String DEST = "D:/Gdrive/Inbox/test.pdf";

  public static void main(final String[] args) throws JSONException, IOException {

    final CreateCaseRequest request =
        CreateCaseRequest.builder()
            .portName("Varna West")
            .shipName("Falcon")
            .shipType("Bulk carrier")
            .shipLengthOverall(BigDecimal.valueOf(190.95))
            .shipGrossTonnage(BigDecimal.valueOf(2500.00))
            .shipHasIncreasedManeuverability(true)
            .warningTypes(
                List.of(
                    "Ship requires special pilot",
                    "Ship transports special cargo",
                    "Ship transports dangerous cargo"))
            .cargoManifest(List.of("Steel rods", "Gold", "Diamonds"))
            .callPurpose("Loading")
            .callCount(3)
            .estimatedDateOfArrival("2021-06-12")
            .estimatedDateOfDeparture("2021-06-15")
            .alongsideDaysExpected(3)
            .clientDiscountCoefficient(BigDecimal.valueOf(0.2))
            .agencyCommissionCoefficient(BigDecimal.valueOf(0.3))
            .tugServiceProvider(TugServiceProvider.VTC)
            .arrivesFromBulgarianPort(true)
            .build();

    final ObjectMapper mapper = new ObjectMapper();
    final String s = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(request);

    System.out.println(s);

    //        dtoConversions();
  }

  private static void dtoConversions() {
    final PdaShip ship =
        PdaShip.builder()
            .name("Aura")
            .type(BULK_CARRIER)
            .lengthOverall(BigDecimal.valueOf(156.25))
            .grossTonnage(BigDecimal.valueOf(2500))
            .hasIncreasedManeuverability(true)
            .build();

    final PdaPort port =
        PdaPort.builder()
            .name("Varna")
            .portArea(FIRST)
            .pilotageArea(VARNA_FIRST)
            .tugArea(TugArea.VTC_FIFTH)
            .build();

    final PdaCase pdaCase =
        PdaCase.builder()
            .ship(ship)
            .port(port)
            .callPurpose(LOADING)
            .callCount(5)
            .alongsideDaysExpected(10)
            .estimatedDateOfArrival(LocalDate.of(2021, 5, 10))
            .estimatedDateOfDeparture(LocalDate.of(2021, 6, 15))
            .build();

    final Case newCase = CaseMapper.INSTANCE.pdaCaseToCase(pdaCase);

    System.out.println();
  }
}
