package flagship.bootstrap;

import com.fasterxml.jackson.databind.ObjectMapper;
import flagship.domain.tariffs.TariffsFactory;
import flagship.domain.tariffs.Tariff;
import flagship.domain.tariffs.agencydues.AgencyDuesTariff;
import flagship.domain.tariffs.mix.HolidayCalendar;
import flagship.domain.tariffs.servicedues.MooringDueTariff;
import flagship.domain.tariffs.servicedues.PilotageDueTariff;
import flagship.domain.tariffs.servicedues.TugDueTariff;
import flagship.domain.tariffs.statedues.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.EnumMap;
import java.util.Map;

import static flagship.domain.calculators.DueCalculator.CalculatorType;
import static flagship.domain.calculators.DueCalculator.CalculatorType.*;

@Component
@Slf4j
@RequiredArgsConstructor
@PropertySource("classpath:application.yml")
public class DataLoader implements ApplicationRunner {

  private static final String TARIFFS_PATH = "src/main/resources/";
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

  @Override
  public void run(ApplicationArguments args) throws IOException {

    if (isNewInstallation) {
      produceHolidayCalendarJsonFile();
      produceStateDuesJsonFiles();
      produceServiceDuesJsonFiles();
      produceAgencyDuesJsonFile();
    } else {

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

      log.info("Tariffs factory initialized");
    }
  }

  private void produceHolidayCalendarJsonFile() throws IOException {
    objectMapper
        .writerWithDefaultPrettyPrinter()
        .writeValue(Paths.get(TARIFFS_PATH + "holidayCalendar.json").toFile(), holidayCalendar);

    log.info("Holiday calendar json file created");
  }

  private void produceStateDuesJsonFiles() throws IOException {

    objectMapper
        .writerWithDefaultPrettyPrinter()
        .writeValue(Paths.get(TARIFFS_PATH + "tonnageDueTariff.json").toFile(), tonnageDueTariff);

    log.info("Tonnage due tariff json file created");

    objectMapper
        .writerWithDefaultPrettyPrinter()
        .writeValue(Paths.get(TARIFFS_PATH + "wharfDueTariff.json").toFile(), wharfDueTariff);

    log.info("Wharf due tariff json file created");

    objectMapper
        .writerWithDefaultPrettyPrinter()
        .writeValue(Paths.get(TARIFFS_PATH + "canalDueTariff.json").toFile(), canalDueTariff);

    log.info("Canal due tariff json file created");

    objectMapper
        .writerWithDefaultPrettyPrinter()
        .writeValue(Paths.get(TARIFFS_PATH + "lightDueTariff.json").toFile(), lightDueTariff);

    log.info("Light due tariff json file created");

    objectMapper
        .writerWithDefaultPrettyPrinter()
        .writeValue(Paths.get(TARIFFS_PATH + "marpolDueTariff.json").toFile(), marpolDueTariff);

    log.info("Marpol due tariff json file created");

    objectMapper
        .writerWithDefaultPrettyPrinter()
        .writeValue(
            Paths.get(TARIFFS_PATH + "boomContainmentDueTariff.json").toFile(),
            boomContainmentTariff);

    log.info("Boom containment due tariff json file created");

    objectMapper
        .writerWithDefaultPrettyPrinter()
        .writeValue(
            Paths.get(TARIFFS_PATH + "sailingPermissionDueTariff.json").toFile(),
            sailingPermissionTariff);

    log.info("Sailing permission due tariff json file created");
  }

  private void produceServiceDuesJsonFiles() throws IOException {

    objectMapper
        .writerWithDefaultPrettyPrinter()
        .writeValue(Paths.get(TARIFFS_PATH + "pilotageDueTariff.json").toFile(), pilotageDueTariff);

    log.info("Pilotage due tariff json file created");

    objectMapper
        .writerWithDefaultPrettyPrinter()
        .writeValue(Paths.get(TARIFFS_PATH + "tugDueTariff.json").toFile(), tugDueTariff);

    log.info("Tug due tariff json file created");

    objectMapper
        .writerWithDefaultPrettyPrinter()
        .writeValue(Paths.get(TARIFFS_PATH + "mooringDueTariff.json").toFile(), mooringDueTariff);

    log.info("Mooring due tariff json file created");
  }

  private void produceAgencyDuesJsonFile() throws IOException {
    objectMapper
        .writerWithDefaultPrettyPrinter()
        .writeValue(Paths.get(TARIFFS_PATH + "agencyDuesTariff.json").toFile(), agencyDuesTariff);

    log.info("Agency dues tariff json file created");
  }
}
