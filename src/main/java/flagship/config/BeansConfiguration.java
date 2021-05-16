package flagship.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import flagship.config.serialization.DueDeserializer;
import flagship.config.serialization.RangeDeserializer;
import flagship.domain.tariffs.*;
import flagship.domain.tariffs.mix.Due;
import flagship.domain.tariffs.mix.Range;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;

@Configuration
@Slf4j
public class BeansConfiguration {

  private final ObjectMapper mapper = configureMapper();

  private static final String TARIFFS_PATH = "src/main/resources/";

  @Bean
  public ObjectMapper objectMapper() {
    return configureMapper();
  }

  @Bean
  public TonnageDueTariff tonnageDueTariff() throws IOException {

    log.info("Tonnage due tariff initialized from database");

    return mapper.readValue(
        new File(TARIFFS_PATH + "tonnageDueTariff.json"), TonnageDueTariff.class);
  }

  @Bean
  public WharfDueTariff wharfDueTariff() throws IOException {

    log.info("Wharf due tariff initialized from database");

    return mapper.readValue(new File(TARIFFS_PATH + "wharfDueTariff.json"), WharfDueTariff.class);
  }

  @Bean
  public CanalDueTariff canalDueTariff() throws IOException {

    log.info("Canal due tariff initialized from database");

    return mapper.readValue(new File(TARIFFS_PATH + "canalDueTariff.json"), CanalDueTariff.class);
  }

  @Bean
  public LightDueTariff lightDueTariff() throws IOException {

    log.info("Light due tariff initialized from database");

    return mapper.readValue(new File(TARIFFS_PATH + "lightDueTariff.json"), LightDueTariff.class);
  }

  @Bean
  public MarpolDueTariff marpolDueTariff() throws IOException {

    log.info("Marpol due tariff initialized from database");

    return mapper.readValue(new File(TARIFFS_PATH + "marpolDueTariff.json"), MarpolDueTariff.class);
  }

  @Bean
  public MooringDueTariff mooringDueTariff() throws IOException {

    log.info("Mooring due tariff initialized from database");

    return mapper.readValue(
        new File(TARIFFS_PATH + "mooringDueTariff.json"), MooringDueTariff.class);
  }

  @Bean
  public BoomContainmentTariff boomContainmentTariff() throws IOException {

    log.info("Boom containment due tariff initialized from database");

    return mapper.readValue(
        new File(TARIFFS_PATH + "boomContainmentDueTariff.json"), BoomContainmentTariff.class);
  }

  @Bean
  public SailingPermissionTariff sailingPermissionTariff() throws IOException {

    log.info("Sailing permission due tariff initialized from database");

    return mapper.readValue(
        new File(TARIFFS_PATH + "sailingPermissionDueTariff.json"), SailingPermissionTariff.class);
  }

  @Bean
  public PilotageDueTariff pilotageDueTariff() throws IOException {

    log.info("Pilotage due tariff initialized from database");

    return mapper.readValue(
        new File(TARIFFS_PATH + "pilotageDueTariff.json"), PilotageDueTariff.class);
  }

  @Bean
  public TugDueTariff tugDueTariff() throws IOException {

    log.info("Tug due tariff initialized from database");

    return mapper.readValue(new File(TARIFFS_PATH + "tugDueTariff.json"), TugDueTariff.class);
  }

  @Bean
  public AgencyDuesTariff agencyDuesTariff() throws IOException {

    log.info("Agency dues tariff initialized from database");

    return mapper.readValue(
        new File(TARIFFS_PATH + "agencyDuesTariff.json"), AgencyDuesTariff.class);
  }

  public ObjectMapper configureMapper() {
    final ObjectMapper objectMapper = new ObjectMapper();
    final SimpleModule simpleModule = new SimpleModule();
    simpleModule.addKeyDeserializer(Range.class, new RangeDeserializer());
    simpleModule.addDeserializer(Due.class, new DueDeserializer());
    objectMapper.registerModule(simpleModule);
    objectMapper.registerModule(new JavaTimeModule());
    return objectMapper;
  }
}
