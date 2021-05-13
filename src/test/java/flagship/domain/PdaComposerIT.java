package flagship.domain;

import flagship.domain.calculators.TariffsInitializer;
import flagship.domain.cases.dto.PdaCase;
import flagship.domain.cases.dto.PdaPort;
import flagship.domain.cases.dto.PdaShip;
import flagship.domain.cases.entities.ProformaDisbursementAccount;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.EnumSet;

import static flagship.domain.PdaWarningsGenerator.PdaWarning.*;
import static flagship.domain.cases.entities.enums.ShipType.GENERAL;
import static flagship.domain.tariffs.MooringDueTariff.MooringServiceProvider;
import static flagship.domain.tariffs.PilotageDueTariff.PilotageArea.BOURGAS_FIRST;
import static flagship.domain.tariffs.PortArea.FIRST;
import static flagship.domain.tariffs.TugDueTariff.TugArea;
import static flagship.domain.tariffs.TugDueTariff.TugServiceProvider;

@SpringBootTest
class PdaComposerIT extends TariffsInitializer {

  @Autowired private TariffsFactory tariffsFactory;

  @Test
  void getStateAccountCalculators() {

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
            .tugArea(TugArea.VTC_FIRST)
            .mooringServiceProvider(MooringServiceProvider.VTC)
            .pilotageArea(BOURGAS_FIRST)
            .tugServiceProvider(TugServiceProvider.VTC)
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
            .warnings(EnumSet.of(SPECIAL_PILOTAGE_CARGO, HAZARDOUS_PILOTAGE_CARGO, ETA_IS_HOLIDAY, ETD_NOT_PROVIDED))
            .build();

    PdaComposer pdaComposer = new PdaComposer(testCase,tariffsFactory);

    ProformaDisbursementAccount proformaDisbursementAccount = pdaComposer.composePda();

    System.out.println();
  }

  @Test
  void test() {

    System.out.println();
  }
}
