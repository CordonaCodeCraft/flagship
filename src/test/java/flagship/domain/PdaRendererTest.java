package flagship.domain;

import flagship.domain.cases.dto.PdaCase;
import flagship.domain.cases.dto.PdaPort;
import flagship.domain.cases.dto.PdaShip;
import flagship.domain.cases.entities.ProformaDisbursementAccount;
import flagship.domain.renders.pda.PdaRender;
import flagship.domain.tariffs.servicedues.MooringDueTariff;
import flagship.domain.tariffs.servicedues.TugDueTariff;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import static flagship.domain.cases.entities.enums.CallPurpose.LOADING;
import static flagship.domain.cases.entities.enums.ShipType.BULK_CARRIER;
import static flagship.domain.tariffs.PortArea.FIRST;
import static flagship.domain.tariffs.servicedues.PilotageDueTariff.PilotageArea.BOURGAS_FIRST;

class PdaRendererTest {

  private static PdaCase testCase;

  @BeforeAll
  static void beforeClass() {

    final PdaShip testShip =
        PdaShip.builder()
            .name("Millennium Falcon")
            .grossTonnage(BigDecimal.valueOf(1650))
            .hasIncreasedManeuverability(true)
            .lengthOverall(BigDecimal.valueOf(160.00))
            .type(BULK_CARRIER)
            .build();

    final PdaPort testPort =
        PdaPort.builder()
            .name("Varna West")
            .portArea(FIRST)
            .tugArea(TugDueTariff.TugArea.VTC_FIRST)
            .mooringServiceProvider(MooringDueTariff.MooringServiceProvider.VTC)
            .pilotageArea(BOURGAS_FIRST)
            .tugServiceProvider(TugDueTariff.TugServiceProvider.VTC)
            .build();

    final ProformaDisbursementAccount pda = new ProformaDisbursementAccount();
    pda.setTonnageDue(BigDecimal.valueOf(2500.00));
    pda.setWharfDue(BigDecimal.valueOf(500.00));
    pda.setCanalDue(BigDecimal.valueOf(800.00));
    pda.setLightDue(BigDecimal.valueOf(80.00));
    pda.setMarpolDue(BigDecimal.valueOf(29.89));
    pda.setMooringDue(BigDecimal.valueOf(189.76));
    pda.setBoomContainmentDue(BigDecimal.valueOf(235.21));
    pda.setSailingPermissionDue(BigDecimal.valueOf(50.00));
    pda.setPilotageDue(BigDecimal.valueOf(1450.22));
    pda.setTugDue(BigDecimal.valueOf(1231.21));
    pda.setBasicAgencyDue(BigDecimal.valueOf(12450.21));
    pda.setCarsDue(BigDecimal.valueOf(123.12));
    pda.setClearanceDue(BigDecimal.valueOf(823.12));
    pda.setCommunicationsDue(BigDecimal.valueOf(121.12));
    pda.setBankExpensesDue(BigDecimal.valueOf(125.21));
    pda.setAgencyOvertimeDue(BigDecimal.valueOf(67.23));
    pda.setClientDiscount(BigDecimal.valueOf(800.00));
    pda.setPayableTotal(BigDecimal.valueOf(980789.23));
    pda.setTotalAfterDiscount(BigDecimal.valueOf(789023.87));
    pda.setProfitExpected(BigDecimal.valueOf(7890.23));

    testCase =
        PdaCase.builder()
            .ship(testShip)
            .port(testPort)
            .arrivesFromBulgarianPort(true)
            .estimatedDateOfArrival(LocalDate.now())
            .estimatedDateOfDeparture(LocalDate.now().plusDays(5))
            .alongsideDaysExpected(5)
            .callPurpose(LOADING)
            .callCount(3)
            .clientDiscountCoefficient(BigDecimal.ZERO)
            .warningTypes(new HashSet<>())
            .cargoManifest(
                List.of("Cocaine", "Guns", "Nuclear weapons", "Whores", "Illegal immigrants"))
            .proformaDisbursementAccount(pda)
            .build();
  }

  @Test
  void name() throws IOException {

    PdaRender render = new PdaRender(testCase);

    render.renderPdaDocument();
  }
}
