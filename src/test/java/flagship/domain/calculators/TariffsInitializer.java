package flagship.domain.calculators;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import flagship.config.serialization.DueDeserializer;
import flagship.config.serialization.RangeDeserializer;
import flagship.domain.tariffs.Due;
import flagship.domain.tariffs.Range;
import flagship.domain.tariffs.agencyduestariffs.AgencyDuesTariff;
import flagship.domain.tariffs.serviceduestariffs.MooringDueTariff;
import flagship.domain.tariffs.serviceduestariffs.PilotageDueTariff;
import flagship.domain.tariffs.serviceduestariffs.TugDueTariff;
import flagship.domain.tariffs.stateduestariffs.*;
import org.junit.jupiter.api.BeforeAll;

import java.io.File;
import java.io.IOException;

public abstract class TariffsInitializer {

  protected static final ObjectMapper mapper = new ObjectMapper();

  protected static final String TARIFFS_PATH = "src/main/resources/";

  protected static PilotageDueTariff pilotageDueTariff = new PilotageDueTariff();
  protected static AgencyDuesTariff agencyDuesTariff = new AgencyDuesTariff();
  protected static TugDueTariff tugDueTariff = new TugDueTariff();
  protected static WharfDueTariff wharfDueTariff;
  protected static MooringDueTariff mooringDueTariff;
  protected static MarpolDueTariff marpolDueTariff;
  protected static BoomContainmentTariff boomContainmentTariff;
  protected static LightDueTariff lightDueTariff;
  protected static TonnageDueTariff tonnageDueTariff;
  protected static CanalDueTariff canalDueTariff;

  @BeforeAll
  public static void initializeTariffs() throws IOException {
    final SimpleModule simpleModule = new SimpleModule();
    simpleModule.addKeyDeserializer(Range.class, new RangeDeserializer());
    simpleModule.addDeserializer(Due.class, new DueDeserializer());
    mapper.registerModule(simpleModule);
    mapper.registerModule(new JavaTimeModule());

    pilotageDueTariff =
        mapper.readValue(
            new File(TARIFFS_PATH + "pilotageDueTariff.json"), PilotageDueTariff.class);

    agencyDuesTariff =
        mapper.readValue(new File(TARIFFS_PATH + "agencyDuesTariff.json"), AgencyDuesTariff.class);

    tugDueTariff =
        mapper.readValue(new File(TARIFFS_PATH + "tugDueTariff.json"), TugDueTariff.class);

    wharfDueTariff =
        mapper.readValue(new File("src/main/resources/wharfDueTariff.json"), WharfDueTariff.class);

    mooringDueTariff =
        mapper.readValue(
            new File("src/main/resources/mooringDueTariff.json"), MooringDueTariff.class);

    marpolDueTariff =
        mapper.readValue(
            new File("src/main/resources/marpolDueTariff.json"), MarpolDueTariff.class);

    boomContainmentTariff =
        mapper.readValue(
            new File("src/main/resources/boomContainmentDueTariff.json"),
            BoomContainmentTariff.class);

    lightDueTariff =
        mapper.readValue(new File("src/main/resources/lightDueTariff.json"), LightDueTariff.class);

    tonnageDueTariff =
        mapper.readValue(
            new File("src/main/resources/tonnageDueTariff.json"), TonnageDueTariff.class);

    canalDueTariff =
        mapper.readValue(new File("src/main/resources/canalDueTariff.json"), CanalDueTariff.class);
  }
}
