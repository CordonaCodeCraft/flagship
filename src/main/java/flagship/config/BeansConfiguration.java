package flagship.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import flagship.bootstrap.newinstalation.*;
import flagship.config.serialization.DueDeserializer;
import flagship.config.serialization.RangeDeserializer;
import flagship.domain.tariffs.agencydues.AgencyDuesTariff;
import flagship.domain.tariffs.mix.Due;
import flagship.domain.tariffs.mix.HolidayCalendar;
import flagship.domain.tariffs.mix.Range;
import flagship.domain.tariffs.servicedues.MooringDueTariff;
import flagship.domain.tariffs.servicedues.PilotageDueTariff;
import flagship.domain.tariffs.servicedues.TugDueTariff;
import flagship.domain.tariffs.statedues.*;
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

  private static final String TARIFFS_PATH = "src/main/resources/";
  private final ObjectMapper objectMapper = configureMapper();

  @Value("${flagship.new-installation}")
  private boolean isNewInstallation;

  @Bean
  public ObjectMapper objectMapper() {
    return configureMapper();
  }

  @Bean
  HolidayCalendar holidayCalendar() throws IOException {

    final HolidayCalendar holidayCalendar;

    if (isNewInstallation) {
      HolidayCalendarInitializer holidayCalendarInitializer = new HolidayCalendarInitializer();
      holidayCalendar = holidayCalendarInitializer.initializeCalendar();
    } else {
      holidayCalendar =
          objectMapper.readValue(
              new File(TARIFFS_PATH + "holidayCalendar.json"), HolidayCalendar.class);
      log.info("Holiday calendar initialized from database");
    }
    return holidayCalendar;
  }

  @Bean
  public TonnageDueTariff tonnageDueTariff() throws IOException {

    final TonnageDueTariff tonnageDueTariff;

    if (isNewInstallation) {
      tonnageDueTariff = TonnageDueTariffInitializer.getTariff();
    } else {
      tonnageDueTariff =
          objectMapper.readValue(
              new File(TARIFFS_PATH + "tonnageDueTariff.json"), TonnageDueTariff.class);
      log.info("Tonnage due tariff initialized from database");
    }
    return tonnageDueTariff;
  }

  @Bean
  public WharfDueTariff wharfDueTariff() throws IOException {

    final WharfDueTariff wharfDueTariff;

    if (isNewInstallation) {
      wharfDueTariff = WharfDueTariffInitializer.getTariff();
    } else {
      wharfDueTariff =
          objectMapper.readValue(
              new File(TARIFFS_PATH + "wharfDueTariff.json"), WharfDueTariff.class);
      log.info("Wharf due tariff initialized from database");
    }
    return wharfDueTariff;
  }

  @Bean
  public CanalDueTariff canalDueTariff() throws IOException {

    final CanalDueTariff canalDueTariff;

    if (isNewInstallation) {
      canalDueTariff = CanalDueTariffInitializer.getTariff();
    } else {
      canalDueTariff =
          objectMapper.readValue(
              new File(TARIFFS_PATH + "canalDueTariff.json"), CanalDueTariff.class);
      log.info("Canal due tariff initialized from database");
    }
    return canalDueTariff;
  }

  @Bean
  public LightDueTariff lightDueTariff() throws IOException {

    final LightDueTariff lightDueTariff;

    if (isNewInstallation) {
      lightDueTariff = LightDueTariffInitializer.getTariff();
    } else {
      lightDueTariff =
          objectMapper.readValue(
              new File(TARIFFS_PATH + "lightDueTariff.json"), LightDueTariff.class);
      log.info("Light due tariff initialized from database");
    }
    return lightDueTariff;
  }

  @Bean
  public MarpolDueTariff marpolDueTariff() throws IOException {

    final MarpolDueTariff marpolDueTariff;

    if (isNewInstallation) {
      marpolDueTariff = MarpolDueTariffInitializer.getTariff();
    } else {
      marpolDueTariff =
          objectMapper.readValue(
              new File(TARIFFS_PATH + "marpolDueTariff.json"), MarpolDueTariff.class);
      log.info("Marpol due tariff initialized from database");
    }
    return marpolDueTariff;
  }

  @Bean
  public BoomContainmentTariff boomContainmentTariff() throws IOException {

    final BoomContainmentTariff boomContainmentTariff;

    if (isNewInstallation) {
      boomContainmentTariff = BoomContainmentTariffInitializer.getTariff();
    } else {
      boomContainmentTariff =
          objectMapper.readValue(
              new File(TARIFFS_PATH + "boomContainmentDueTariff.json"),
              BoomContainmentTariff.class);
      log.info("Boom containment due tariff initialized from database");
    }
    return boomContainmentTariff;
  }

  @Bean
  public SailingPermissionTariff sailingPermissionTariff() throws IOException {

    final SailingPermissionTariff sailingPermissionTariff;

    if (isNewInstallation) {
      sailingPermissionTariff = SailingPermissionDueTariffInitializer.getTariff();
    } else {
      sailingPermissionTariff =
          objectMapper.readValue(
              new File(TARIFFS_PATH + "sailingPermissionDueTariff.json"),
              SailingPermissionTariff.class);
      log.info("Sailing permission due tariff initialized from database");
    }
    return sailingPermissionTariff;
  }

  @Bean
  public PilotageDueTariff pilotageDueTariff() throws IOException {

    final PilotageDueTariff pilotageDueTariff;

    if (isNewInstallation) {
      pilotageDueTariff = PilotageDueTariffInitializer.getTariff(holidayCalendar());
    } else {
      pilotageDueTariff =
          objectMapper.readValue(
              new File(TARIFFS_PATH + "pilotageDueTariff.json"), PilotageDueTariff.class);
      log.info("Pilotage due tariff initialized from database");
    }
    return pilotageDueTariff;
  }

  @Bean
  public TugDueTariff tugDueTariff() throws IOException {

    final TugDueTariff tugDueTariff;

    if (isNewInstallation) {
      tugDueTariff = TugDueTariffInitializer.getTariff(holidayCalendar());
    } else {
      tugDueTariff =
          objectMapper.readValue(new File(TARIFFS_PATH + "tugDueTariff.json"), TugDueTariff.class);
      log.info("Tug due tariff initialized from database");
    }
    return tugDueTariff;
  }

  @Bean
  public MooringDueTariff mooringDueTariff() throws IOException {

    MooringDueTariff mooringDueTariff;

    if (isNewInstallation) {
      mooringDueTariff = MooringDueTariffInitializer.getTariff(holidayCalendar());
    } else {
      mooringDueTariff =
          objectMapper.readValue(
              new File(TARIFFS_PATH + "mooringDueTariff.json"), MooringDueTariff.class);
      log.info("Mooring due tariff initialized from database");
    }
    return mooringDueTariff;
  }

  @Bean
  public AgencyDuesTariff agencyDuesTariff() throws IOException {

    final AgencyDuesTariff agencyDuesTariff;

    if (isNewInstallation) {
      agencyDuesTariff = AgencyDuesTariffInitializer.getTariff();
    } else {
      agencyDuesTariff =
          objectMapper.readValue(
              new File(TARIFFS_PATH + "agencyDuesTariff.json"), AgencyDuesTariff.class);
      log.info("Agency dues tariff initialized from database");
    }
    return agencyDuesTariff;
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
}
