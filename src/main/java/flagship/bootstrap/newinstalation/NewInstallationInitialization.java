package flagship.bootstrap.newinstalation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import flagship.domain.TariffsFactory;
import flagship.domain.tariffs.*;
import flagship.domain.tariffs.AgencyDuesTariff;
import flagship.domain.tariffs.mix.HolidayCalendar;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Paths;

@PropertySource("classpath:application.yml")
@Component
@Order(1)
@RequiredArgsConstructor
@Slf4j
public class NewInstallationInitialization implements ApplicationRunner {

  @Value("${flagship.new-installation}")
  private boolean isNewInstallation;

  private static final String TARIFFS_PATH = "src/main/resources/";

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
  public void run(final ApplicationArguments args) throws Exception {

    if (isNewInstallation) {
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

      produceStateDuesJsonFiles();
      produceServiceDuesJsonFiles();
      produceAgencyDuesJsonFile();
    }
  }

  private void produceStateDuesJsonFiles() throws IOException {

    objectMapper
        .writerWithDefaultPrettyPrinter()
        .writeValue(Paths.get(TARIFFS_PATH + "tonnageDueTariff.json").toFile(), tonnageDueTariff);

    objectMapper
        .writerWithDefaultPrettyPrinter()
        .writeValue(Paths.get(TARIFFS_PATH + "wharfDueTariff.json").toFile(), wharfDueTariff);

    objectMapper
        .writerWithDefaultPrettyPrinter()
        .writeValue(Paths.get(TARIFFS_PATH + "lightDueTariff.json").toFile(), lightDueTariff);

    objectMapper
        .writerWithDefaultPrettyPrinter()
        .writeValue(Paths.get(TARIFFS_PATH + "canalDueTariff.json").toFile(), canalDueTariff);

    objectMapper
        .writerWithDefaultPrettyPrinter()
        .writeValue(Paths.get(TARIFFS_PATH + "marpolDueTariff.json").toFile(), marpolDueTariff);

    objectMapper
        .writerWithDefaultPrettyPrinter()
        .writeValue(
            Paths.get(TARIFFS_PATH + "boomContainmentDueTariff.json").toFile(),
            boomContainmentTariff);

    objectMapper
        .writerWithDefaultPrettyPrinter()
        .writeValue(
            Paths.get(TARIFFS_PATH + "sailingPermissionDueTariff.json").toFile(),
            sailingPermissionTariff);
  }

  private void produceServiceDuesJsonFiles() throws IOException {

    objectMapper
        .registerModule(new JavaTimeModule())
        .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        .configure(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS, false)
        .writerWithDefaultPrettyPrinter()
        .writeValue(
            Paths.get(TARIFFS_PATH + "pilotageDueTariff.json").toFile(), pilotageDueTariff);

    objectMapper
        .writerWithDefaultPrettyPrinter()
        .writeValue(Paths.get(TARIFFS_PATH + "tugDueTariff.json").toFile(), tugDueTariff);

    objectMapper
        .writerWithDefaultPrettyPrinter()
        .writeValue(
            Paths.get(TARIFFS_PATH + "mooringDueTariff.json").toFile(), mooringDueTariff);

    objectMapper
        .writerWithDefaultPrettyPrinter()
        .writeValue(Paths.get(TARIFFS_PATH + "holidayCalendar.json").toFile(), holidayCalendar);
  }

  private void produceAgencyDuesJsonFile() throws IOException {
    objectMapper
        .writerWithDefaultPrettyPrinter()
        .writeValue(
            Paths.get(TARIFFS_PATH + "agencyDuesTariff.json").toFile(), agencyDuesTariff);
  }
}
