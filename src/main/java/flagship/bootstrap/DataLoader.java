package flagship.bootstrap;

import flagship.domain.TariffsFactory;
import flagship.domain.tariffs.*;
import flagship.domain.tariffs.AgencyDuesTariff;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;

import static flagship.domain.calculators.DueCalculator.CalculatorType;
import static flagship.domain.calculators.DueCalculator.CalculatorType.*;


@Component
@Slf4j
@RequiredArgsConstructor
@Order(2)
public class DataLoader implements ApplicationRunner {

  private final TonnageDueTariff tonnageDueTariff;
  private final WharfDueTariff wharfDueTariff;
  private final CanalDueTariff canalDueTariff;
  private final LightDueTariff lightDueTariff;
  private final MarpolDueTariff marpolDueTariff;
  private final BoomContainmentTariff boomContainmentTariff;
  private final SailingPermissionTariff sailingPermissionTariff;
  private final PilotageDueTariff pilotageDueTariff;
  private final TugDueTariff tugDueTariff;
  private final MooringDueTariff mooringDueTariff;
  private final AgencyDuesTariff agencyDuesTariff;
  private final TariffsFactory tariffsFactory;

  @Override
  public void run(ApplicationArguments args) throws Exception {

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


  }


}
