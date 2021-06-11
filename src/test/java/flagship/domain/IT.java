package flagship.domain;

import flagship.domain.calculation.tariffs.TariffsFactory;
import flagship.domain.caze.model.PdaCase;
import flagship.domain.caze.model.request.resolvers.MooringServiceProviderResolver;
import flagship.domain.caze.model.request.resolvers.TugAreaResolver;
import flagship.domain.pda.composer.PdaComposer;
import flagship.domain.pda.entity.ProformaDisbursementAccount;
import flagship.domain.port.model.PdaPort;
import flagship.domain.ship.model.PdaShip;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;

import static flagship.domain.caze.model.request.resolvers.PilotageAreaResolver.PilotageArea.BOURGAS_FIRST;
import static flagship.domain.caze.model.request.resolvers.PortAreaResolver.PortArea.FIRST;
import static flagship.domain.ship.entity.Ship.ShipType.BULK_CARRIER;

@Disabled
@SpringBootTest
public class IT {

  @Autowired PdaComposer pdaComposer;
  @Autowired TariffsFactory tariffsFactory;

  PdaShip testShip =
      PdaShip.builder()
          .grossTonnage(BigDecimal.valueOf(1650))
          .hasIncreasedManeuverability(true)
          .lengthOverall(BigDecimal.valueOf(160.00))
          .type(BULK_CARRIER)
          .build();

  PdaPort testPort =
      PdaPort.builder()
          .name("Varna West")
          .portArea(FIRST)
          .tugArea(TugAreaResolver.TugArea.VTC_FIRST)
          .mooringServiceProvider(MooringServiceProviderResolver.MooringServiceProvider.VTC)
          .pilotageArea(BOURGAS_FIRST)
          .tugServiceProvider(TugAreaResolver.TugServiceProvider.VTC)
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

    final ProformaDisbursementAccount proformaDisbursementAccount = pdaComposer.composePda();

    System.out.println();
  }
}
