package flagship.bootstrap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import flagship.domain.tariffs.HolidayCalendar;
import flagship.domain.tariffs.serviceduestariffs.MooringDueTariff;
import flagship.domain.tariffs.serviceduestariffs.PilotageDueTariff;
import flagship.domain.tariffs.serviceduestariffs.TugDueTariff;
import flagship.domain.tariffs.stateduestariffs.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements ApplicationRunner {

  private final ObjectMapper objectMapper;
  private final TonnageDueTariff tonnageDueTariff;
  private final WharfDueTariff wharfDueTariff;
  private final CanalDueTariff canalDueTariff;
  private final LightDueTariff lightDueTariff;
  private final MarpolDueTariff marpolDueTariff;
  private final BoomContainmentTariff boomContainmentTariff;

  private final PilotageDueTariff pilotageDueTariff;
  private final TugDueTariff tugDueTariff;
  private final MooringDueTariff mooringDueTariff;
  private final HolidayCalendar holidayCalendar;

  @Override
  public void run(ApplicationArguments args) throws Exception {

    StateDueTariffInitializer.initializeTariffs(
        tonnageDueTariff,
        wharfDueTariff,
        canalDueTariff,
        lightDueTariff,
        marpolDueTariff,
        boomContainmentTariff);
    ServiceDueTariffInitializer.initializeTariff(
        pilotageDueTariff, tugDueTariff, mooringDueTariff, holidayCalendar);

    produceStateDueJsonFiles();
    produceServiceDueJsonFiles();

    MarpolDueTariff tariff =
        objectMapper.readValue(
            new File("src/main/resources/marpolDueTariff.json"), MarpolDueTariff.class);

    System.out.println();
  }

  private void produceStateDueJsonFiles() throws IOException {

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
  }

  private void produceServiceDueJsonFiles() throws IOException {

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
}
