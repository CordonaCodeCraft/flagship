package flagship.bootstrap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import flagship.domain.TariffsFactory;
import flagship.domain.tariffs.*;
import flagship.domain.tariffs.mix.HolidayCalendar;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
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
public class DataLoader implements ApplicationRunner {

  private final ObjectMapper objectMapper;
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
  private final HolidayCalendar holidayCalendar;
  private final AgencyDuesTariff agencyDuesTariff;
  private final TariffsFactory tariffsFactory;


  @Override
  public void run(ApplicationArguments args) throws Exception {

    StateDuesTariffsInitializer.initializeTariffs(
            tonnageDueTariff,
            wharfDueTariff,
            canalDueTariff,
            lightDueTariff,
            marpolDueTariff,
            boomContainmentTariff,
            sailingPermissionTariff);
    ServiceDuesTariffsInitializer.initializeTariffs(
            pilotageDueTariff, tugDueTariff, mooringDueTariff, holidayCalendar);

    AgencyDueTariffsInitializer.initializeTariffs(agencyDuesTariff);

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

    System.out.println();

    produceStateDuesJsonFiles();
    produceServiceDuesJsonFiles();
    produceAgencyDuesJsonFile();
  }

  private void produceStateDuesJsonFiles() throws IOException {

    objectMapper
            .writerWithDefaultPrettyPrinter()
            .writeValue(
                    Paths.get("src/main/resources/tonnageDueTariff.json").toFile(), tonnageDueTariff);

    objectMapper
            .writerWithDefaultPrettyPrinter()
            .writeValue(Paths.get("src/main/resources/wharfDueTariff.json").toFile(), wharfDueTariff);

    objectMapper
            .writerWithDefaultPrettyPrinter()
            .writeValue(Paths.get("src/main/resources/lightDueTariff.json").toFile(), lightDueTariff);

    objectMapper
            .writerWithDefaultPrettyPrinter()
            .writeValue(Paths.get("src/main/resources/canalDueTariff.json").toFile(), canalDueTariff);

    objectMapper
            .writerWithDefaultPrettyPrinter()
            .writeValue(Paths.get("src/main/resources/marpolDueTariff.json").toFile(), marpolDueTariff);

    objectMapper
            .writerWithDefaultPrettyPrinter()
            .writeValue(
                    Paths.get("src/main/resources/boomContainmentDueTariff.json").toFile(),
                    boomContainmentTariff);

    objectMapper
            .writerWithDefaultPrettyPrinter()
            .writeValue(
                    Paths.get("src/main/resources/sailingPermissionDueTariff.json").toFile(),
                    sailingPermissionTariff);
  }

  private void produceServiceDuesJsonFiles() throws IOException {

    objectMapper
            .registerModule(new JavaTimeModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .configure(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS, false)
            .writerWithDefaultPrettyPrinter()
            .writeValue(
                    Paths.get("src/main/resources/pilotageDueTariff.json").toFile(), pilotageDueTariff);

    objectMapper
            .writerWithDefaultPrettyPrinter()
            .writeValue(Paths.get("src/main/resources/tugDueTariff.json").toFile(), tugDueTariff);

    objectMapper
            .writerWithDefaultPrettyPrinter()
            .writeValue(
                    Paths.get("src/main/resources/mooringDueTariff.json").toFile(), mooringDueTariff);

    objectMapper
            .writerWithDefaultPrettyPrinter()
            .writeValue(Paths.get("src/main/resources/holidayCalendar.json").toFile(), holidayCalendar);
  }

  private void produceAgencyDuesJsonFile() throws IOException {
    objectMapper
            .writerWithDefaultPrettyPrinter()
            .writeValue(
                    Paths.get("src/main/resources/agencyDuesTariff.json").toFile(), agencyDuesTariff);
  }
}
