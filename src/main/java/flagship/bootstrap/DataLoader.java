package flagship.bootstrap;

import com.fasterxml.jackson.databind.ObjectMapper;
import flagship.domain.calculation.tariffs.Tariff;
import flagship.domain.calculation.tariffs.TariffsFactory;
import flagship.domain.calculation.tariffs.agency.AgencyDuesTariff;
import flagship.domain.calculation.tariffs.calendar.HolidayCalendar;
import flagship.domain.calculation.tariffs.service.MooringDueTariff;
import flagship.domain.calculation.tariffs.service.PilotageDueTariff;
import flagship.domain.calculation.tariffs.service.TugDueTariff;
import flagship.domain.calculation.tariffs.state.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;

import static flagship.domain.calculation.calculators.DueCalculator.CalculatorType;
import static flagship.domain.calculation.calculators.DueCalculator.CalculatorType.*;

@Component
@Slf4j
@RequiredArgsConstructor
@PropertySource("classpath:application.yml")
public class DataLoader implements ApplicationRunner {

  private final ObjectMapper objectMapper;
  private final HolidayCalendar holidayCalendar;
  private final TonnageDueTariff tonnageDueTariff;
  private final WharfDueTariff wharfDueTariff;
  private final CanalDueTariff canalDueTariff;
  private final LightDueTariff lightDueTariff;
  private final MarpolDueTariff marpolDueTariff;
  private final MooringDueTariff mooringDueTariff;
  private final BoomContainmentTariff boomContainmentTariff;
  private final SailingPermissionTariff sailingPermissionTariff;
  private final PilotageDueTariff pilotageDueTariff;
  private final TugDueTariff tugDueTariff;
  private final AgencyDuesTariff agencyDuesTariff;
  private final TariffsFactory tariffsFactory;

  @Value("${flagship.new-installation}")
  private boolean isNewInstallation;

  @Value("${flagship.tariffs-json-path}")
  private String tariffsPath;

  @Override
  public void run(final ApplicationArguments args) throws IOException {

    //    if (isNewInstallation) {
    //      produceHolidayCalendarJsonFile();
    //
    //      produceStateDuesJsonFiles();
    //
    //      produceServiceDuesJsonFiles();
    //
    //      produceAgencyDuesJsonFile();
    //    }

    initializeTariffsFactory();
  }

  //  private void produceHolidayCalendarJsonFile() throws IOException {
  //    createJsonFrom(holidayCalendar, "holidayCalendar.json");
  //  }
  //
  //  private void produceStateDuesJsonFiles() throws IOException {
  //    createJsonFrom(tonnageDueTariff, "tonnageDueTariff.json");
  //
  //    createJsonFrom(wharfDueTariff, "wharfDueTariff.json");
  //
  //    createJsonFrom(canalDueTariff, "canalDueTariff.json");
  //
  //    createJsonFrom(lightDueTariff, "lightDueTariff.json");
  //
  //    createJsonFrom(marpolDueTariff, "marpolDueTariff.json");
  //
  //    createJsonFrom(boomContainmentTariff, "boomContainmentDueTariff.json");
  //
  //    createJsonFrom(sailingPermissionTariff, "sailingPermissionDueTariff.json");
  //  }
  //
  //  private void produceServiceDuesJsonFiles() throws IOException {
  //    createJsonFrom(pilotageDueTariff, "pilotageDueTariff.json");
  //
  //    createJsonFrom(tugDueTariff, "tugDueTariff.json");
  //
  //    createJsonFrom(mooringDueTariff, "mooringDueTariff.json");
  //  }
  //
  //  private void produceAgencyDuesJsonFile() throws IOException {
  //    createJsonFrom(agencyDuesTariff, "agencyDuesTariff.json");
  //  }

  private void initializeTariffsFactory() {
    final Map<CalculatorType, Tariff> tariffs = new EnumMap<>(CalculatorType.class);

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

    log.info(String.format("%s initialized", TariffsFactory.class.getSimpleName()));
  }

  //    private <T> void createJsonFrom(final T source, final String asJson) {
  //
  //      try {
  //        objectMapper
  //            .writerWithDefaultPrettyPrinter()
  //            .writeValue(Paths.get(tariffsPath + asJson).toFile(), source);
  //        log.info(String.format("%s created", asJson));
  //      } catch (final IOException e) {
  //        e.printStackTrace();
  //      }
  //    }
}
