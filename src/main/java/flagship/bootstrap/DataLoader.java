package flagship.bootstrap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import flagship.domain.calculators.HolidayCalendar;
import flagship.domain.calculators.tariffs.serviceduestariffs.PilotageDueTariff;
import flagship.domain.calculators.tariffs.serviceduestariffs.TugDueTariff;
import flagship.domain.calculators.tariffs.stateduestariffs.CanalDueTariff;
import flagship.domain.calculators.tariffs.stateduestariffs.LightDueTariff;
import flagship.domain.calculators.tariffs.stateduestariffs.TonnageDueTariff;
import flagship.domain.calculators.tariffs.stateduestariffs.WharfDueTariff;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

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

  private final PilotageDueTariff pilotageDueTariff;
  private final TugDueTariff tugDueTariff;
  private final HolidayCalendar holidayCalendar;

  @Override
  public void run(ApplicationArguments args) throws Exception {

    StateDueTariffInitializer.initializeTariffs(
        tonnageDueTariff, wharfDueTariff, canalDueTariff, lightDueTariff);
    ServiceDueTariffInitializer.initializeTariff(pilotageDueTariff, tugDueTariff, holidayCalendar);

    produceStateDueJsonFiles();
    produceServiceDueJsonFiles();
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
        .writeValue(Paths.get("src/main/resources/holidayCalendar.json").toFile(), holidayCalendar);
  }
}
