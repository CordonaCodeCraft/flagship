package flagship.domain;

import flagship.domain.cases.dto.PdaCase;
import flagship.domain.cases.dto.PdaPort;
import flagship.domain.cases.dto.PdaShip;
import flagship.domain.tariffs.servicedues.MooringDueTariff;
import flagship.domain.tariffs.servicedues.TugDueTariff;
import org.junit.jupiter.api.BeforeAll;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;

import static flagship.domain.cases.entities.enums.ShipType.BULK_CARRIER;
import static flagship.domain.tariffs.PortArea.FIRST;
import static flagship.domain.tariffs.servicedues.PilotageDueTariff.PilotageArea.BOURGAS_FIRST;

class PdaRendererTest {

  private static PdaCase testCase;

  @BeforeAll
  static void beforeClass() {

    final PdaShip testShip =
        PdaShip.builder()
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

    testCase =
        PdaCase.builder()
            .ship(testShip)
            .port(testPort)
            .arrivesFromBulgarianPort(true)
            .estimatedDateOfArrival(LocalDate.now())
            .estimatedDateOfDeparture(LocalDate.now().plusDays(5))
            .alongsideDaysExpected(5)
            .callCount(3)
            .clientDiscountCoefficient(BigDecimal.ZERO)
            .warningTypes(new HashSet<>())
            .build();
  }
}
