package flagship.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import flagship.bootstrap.initializers.*;
import flagship.domain.calculation.tariffs.Tariff;
import flagship.domain.calculation.tariffs.agency.AgencyDuesTariff;
import flagship.domain.calculation.tariffs.calendar.HolidayCalendar;
import flagship.domain.calculation.tariffs.service.MooringDueTariff;
import flagship.domain.calculation.tariffs.service.PilotageDueTariff;
import flagship.domain.calculation.tariffs.service.TugDueTariff;
import flagship.domain.calculation.tariffs.state.*;
import flagship.domain.tuples.due.Due;
import flagship.domain.tuples.due.serialization.DueDeserializer;
import flagship.domain.tuples.range.Range;
import flagship.domain.tuples.range.serialization.RangeDeserializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.io.File;
import java.io.IOException;

@Configuration
@Slf4j
@PropertySource("classpath:application.yml")
public class BeansConfiguration {

  private final ObjectMapper objectMapper = configureMapper();

  @Value("${flagship.new-installation}")
  private boolean isNewInstallation;

  @Value("${flagship.tariffs-json-path}")
  private String tariffsPath;

  @Bean
  public ObjectMapper objectMapper() {
    return configureMapper();
  }

  @Bean
  HolidayCalendar holidayCalendar() throws IOException {

    final HolidayCalendar holidayCalendar;

    if (isNewInstallation) {
      final HolidayCalendarInitializer holidayCalendarInitializer =
          new HolidayCalendarInitializer();
      holidayCalendar = holidayCalendarInitializer.initializeCalendar();
    } else {
      holidayCalendar =
          objectMapper.readValue(
              new File(tariffsPath + "holidayCalendar.json"), HolidayCalendar.class);
      log.info("Holiday calendar initialized from database");
    }
    return holidayCalendar;
  }

  @Bean
  public TonnageDueTariff tonnageDueTariff() throws IOException {
    return isNewInstallation
        ? TonnageDueTariffInitializer.getTariff()
        : deserialize(TonnageDueTariff.class, "tonnageDueTariff.json");
  }

  @Bean
  public WharfDueTariff wharfDueTariff() throws IOException {
    return isNewInstallation
        ? WharfDueTariffInitializer.getTariff()
        : deserialize(WharfDueTariff.class, "wharfDueTariff.json");
  }

  @Bean
  public CanalDueTariff canalDueTariff() throws IOException {
    return isNewInstallation
        ? CanalDueTariffInitializer.getTariff()
        : deserialize(CanalDueTariff.class, "canalDueTariff.json");
  }

  @Bean
  public LightDueTariff lightDueTariff() throws IOException {
    return isNewInstallation
        ? LightDueTariffInitializer.getTariff()
        : deserialize(LightDueTariff.class, "lightDueTariff.json");
  }

  @Bean
  public MarpolDueTariff marpolDueTariff() throws IOException {
    return isNewInstallation
        ? MarpolDueTariffInitializer.getTariff()
        : deserialize(MarpolDueTariff.class, "marpolDueTariff.json");
  }

  @Bean
  public BoomContainmentTariff boomContainmentTariff() throws IOException {
    return isNewInstallation
        ? BoomContainmentTariffInitializer.getTariff()
        : deserialize(BoomContainmentTariff.class, "boomContainmentDueTariff.json");
  }

  @Bean
  public SailingPermissionTariff sailingPermissionTariff() throws IOException {
    return isNewInstallation
        ? SailingPermissionDueTariffInitializer.getTariff()
        : deserialize(SailingPermissionTariff.class, "sailingPermissionDueTariff.json");
  }

  @Bean
  public PilotageDueTariff pilotageDueTariff() throws IOException {
    return isNewInstallation
        ? PilotageDueTariffInitializer.getTariff(holidayCalendar())
        : deserialize(PilotageDueTariff.class, "pilotageDueTariff.json");
  }

  @Bean
  public TugDueTariff tugDueTariff() throws IOException {
    return isNewInstallation
        ? TugDueTariffInitializer.getTariff(holidayCalendar())
        : deserialize(TugDueTariff.class, "tugDueTariff.json");
  }

  @Bean
  public MooringDueTariff mooringDueTariff() throws IOException {
    return isNewInstallation
        ? MooringDueTariffInitializer.getTariff(holidayCalendar())
        : deserialize(MooringDueTariff.class, "mooringDueTariff.json");
  }

  @Bean
  public AgencyDuesTariff agencyDuesTariff() throws IOException {
    return isNewInstallation
        ? AgencyDuesTariffInitializer.getTariff()
        : deserialize(AgencyDuesTariff.class, "agencyDuesTariff.json");
  }

  public ObjectMapper configureMapper() {

    final ObjectMapper objectMapper = new ObjectMapper();
    final SimpleModule simpleModule = new SimpleModule();

    simpleModule.addKeyDeserializer(Range.class, new RangeDeserializer());
    simpleModule.addDeserializer(Due.class, new DueDeserializer());
    objectMapper.registerModule(simpleModule);
    objectMapper.registerModule(new JavaTimeModule());
    objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    objectMapper.configure(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS, false);
    objectMapper.writerWithDefaultPrettyPrinter();

    return objectMapper;
  }

  private <T extends Tariff> T deserialize(final Class<T> target, final String from)
      throws IOException {

    final T product = target.cast(objectMapper.readValue(new File(tariffsPath + from), target));

    log.info(String.format("%s initialized from JSON", target.getSimpleName()));

    return product;
  }
}
