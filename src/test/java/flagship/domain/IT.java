package flagship.domain;

import flagship.domain.cases.dto.PdaCase;
import flagship.domain.cases.dto.PdaPort;
import flagship.domain.cases.dto.PdaShip;
import flagship.domain.cases.entities.ProformaDisbursementAccount;
import flagship.domain.tariffs.MooringDueTariff;
import flagship.domain.tariffs.TugDueTariff;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;

import static flagship.domain.cases.entities.enums.ShipType.GENERAL;
import static flagship.domain.tariffs.PilotageDueTariff.PilotageArea.BOURGAS_FIRST;
import static flagship.domain.tariffs.PortArea.FIRST;

@SpringBootTest
public class IT {

    @Autowired PdaComposer pdaComposer;
    @Autowired TariffsFactory tariffsFactory;

     PdaShip testShip =
            PdaShip.builder()
                    .grossTonnage(BigDecimal.valueOf(1650))
                    .hasIncreasedManeuverability(true)
                    .lengthOverall(BigDecimal.valueOf(160.00))
                    .type(GENERAL)
                    .build();

     PdaPort testPort =
            PdaPort.builder()
                    .name("Varna West")
                    .portArea(FIRST)
                    .tugArea(TugDueTariff.TugArea.VTC_FIRST)
                    .mooringServiceProvider(MooringDueTariff.MooringServiceProvider.VTC)
                    .pilotageArea(BOURGAS_FIRST)
                    .tugServiceProvider(TugDueTariff.TugServiceProvider.VTC)
                    .build();

    PdaCase testCase =
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

  @Test
  void name() {
      pdaComposer.setTariffsFactory(tariffsFactory);
      pdaComposer.setSource(testCase);

      ProformaDisbursementAccount proformaDisbursementAccount = pdaComposer.composePda();

    System.out.println();
  }

}
