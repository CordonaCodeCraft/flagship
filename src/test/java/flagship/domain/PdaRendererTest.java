package flagship.domain;

import flagship.domain.calculation.calculators.TariffsInitializer;
import flagship.domain.calculation.tariffs.Tariff;
import flagship.domain.calculation.tariffs.TariffsFactory;
import flagship.domain.calculation.tariffs.service.MooringDueTariff;
import flagship.domain.calculation.tariffs.service.TugDueTariff;
import flagship.domain.pda.entity.ProformaDisbursementAccount;
import flagship.domain.pda.model.PdaCase;
import flagship.domain.pda.render.PdaRender;
import flagship.domain.port.model.PdaPort;
import flagship.domain.ship.model.PdaShip;
import flagship.domain.warning.generator.WarningsGenerator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static flagship.domain.calculation.calculators.DueCalculator.CalculatorType;
import static flagship.domain.calculation.calculators.DueCalculator.CalculatorType.*;
import static flagship.domain.calculation.tariffs.service.PilotageDueTariff.PilotageArea.BOURGAS_FIRST;
import static flagship.domain.caze.entity.Case.CallPurpose.LOADING;
import static flagship.domain.port.entity.Port.PortArea.FIRST;
import static flagship.domain.ship.entity.Ship.ShipType.BULK_CARRIER;

class PdaRendererTest extends TariffsInitializer {

  private static PdaCase testCase;
  private static final TariffsFactory tariffsFactory = new TariffsFactory();

  @BeforeAll
  static void beforeClass() {

    Map<CalculatorType, Tariff> tariffs = new EnumMap<>(CalculatorType.class);
    tariffs.put(TONNAGE_DUE_CALCULATOR, tonnageDueTariff);
    tariffs.put(WHARF_DUE_CALCULATOR, wharfDueTariff);
    tariffs.put(CANAL_DUE_CALCULATOR, canalDueTariff);
    tariffs.put(LIGHT_DUE_CALCULATOR, lightDueTariff);
    tariffs.put(MARPOL_DUE_CALCULATOR, marpolDueTariff);
    tariffs.put(MOORING_DUE_CALCULATOR, mooringDueTariff);
    tariffs.put(BOOM_CONTAINMENT_DUE_CALCULATOR, boomContainmentTariff);
    tariffs.put(SAILING_PERMISSION_CALCULATOR, sailingPermissionTariff);
    tariffs.put(PILOTAGE_DUE_CALCULATOR, pilotageDueTariff);
    tariffs.put(TUG_DUE_CALCULATOR, tugDueTariff);
    tariffs.put(BASIC_AGENCY_DUE_CALCULATOR, agencyDuesTariff);
    tariffs.put(BANK_EXPENSES_DUE_CALCULATOR, agencyDuesTariff);
    tariffs.put(CARS_DUE_CALCULATOR, agencyDuesTariff);
    tariffs.put(CLEARANCE_DUE_CALCULATOR, agencyDuesTariff);
    tariffs.put(COMMUNICATIONS_DUE_CALCULATOR, agencyDuesTariff);
    tariffs.put(OVERTIME_DUE_CALCULATOR, agencyDuesTariff);
    tariffsFactory.setTariffs(tariffs);

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
            .alongsideDaysExpected(5)
            .callPurpose(LOADING)
            .callCount(3)
            .clientDiscountCoefficient(BigDecimal.valueOf(2.5))
            .cargoManifest(
                List.of("Cocaine", "Guns", "Nuclear weapons", "Whores", "Illegal immigrants"))
            .proformaDisbursementAccount(pda)
            .build();
  }

  @Test
  void name() throws IOException {

    testCase.setEstimatedDateOfArrival(LocalDate.of(2021, 1, 3));
//    testCase.setEstimatedDateOfDeparture(LocalDate.of(2021, 1, 3));

    WarningsGenerator generator = new WarningsGenerator(testCase, tariffsFactory);

    testCase.setWarnings(generator.generateWarnings());

    PdaRender render = new PdaRender(testCase);

    render.renderPdaDocument();
  }
}
