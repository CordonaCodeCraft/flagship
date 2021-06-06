package flagship.domain.calculation.calculators;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import flagship.domain.calculation.tariffs.agency.AgencyDuesTariff;
import flagship.domain.calculation.tariffs.service.MooringDueTariff;
import flagship.domain.calculation.tariffs.service.PilotageDueTariff;
import flagship.domain.calculation.tariffs.service.TugDueTariff;
import flagship.domain.calculation.tariffs.state.*;
import flagship.domain.base.due.serialization.DueDeserializer;
import flagship.domain.base.due.tuple.Due;
import flagship.domain.base.range.serialization.RangeDeserializer;
import flagship.domain.base.range.tuple.Range;
import org.junit.jupiter.api.BeforeAll;

import java.io.File;
import java.io.IOException;

public abstract class TariffsInitializer {

  protected static final ObjectMapper mapper = new ObjectMapper();

  protected static final String TARIFFS_PATH = "src/main/resources/tariffs/";

  protected static TonnageDueTariff tonnageDueTariff;
  protected static WharfDueTariff wharfDueTariff;
  protected static CanalDueTariff canalDueTariff;
  protected static LightDueTariff lightDueTariff;
  protected static MarpolDueTariff marpolDueTariff;
  protected static MooringDueTariff mooringDueTariff;
  protected static BoomContainmentTariff boomContainmentTariff;
  protected static SailingPermissionTariff sailingPermissionTariff;
  protected static PilotageDueTariff pilotageDueTariff = new PilotageDueTariff();
  protected static TugDueTariff tugDueTariff = new TugDueTariff();
  protected static AgencyDuesTariff agencyDuesTariff = new AgencyDuesTariff();

  @BeforeAll
  public static void initializeTariffs() throws IOException {
    final SimpleModule simpleModule = new SimpleModule();
    simpleModule.addKeyDeserializer(Range.class, new RangeDeserializer());
    simpleModule.addDeserializer(Due.class, new DueDeserializer());
    mapper.registerModule(simpleModule);
    mapper.registerModule(new JavaTimeModule());

    tonnageDueTariff =
        mapper.readValue(new File(TARIFFS_PATH + "tonnageDueTariff.json"), TonnageDueTariff.class);

    wharfDueTariff =
        mapper.readValue(new File(TARIFFS_PATH + "wharfDueTariff.json"), WharfDueTariff.class);

    canalDueTariff =
        mapper.readValue(new File(TARIFFS_PATH + "canalDueTariff.json"), CanalDueTariff.class);

    lightDueTariff =
        mapper.readValue(new File(TARIFFS_PATH + "lightDueTariff.json"), LightDueTariff.class);

    marpolDueTariff =
        mapper.readValue(new File(TARIFFS_PATH + "marpolDueTariff.json"), MarpolDueTariff.class);

    mooringDueTariff =
        mapper.readValue(new File(TARIFFS_PATH + "mooringDueTariff.json"), MooringDueTariff.class);

    boomContainmentTariff =
        mapper.readValue(
            new File(TARIFFS_PATH + "boomContainmentDueTariff.json"), BoomContainmentTariff.class);

    sailingPermissionTariff =
        mapper.readValue(
            new File(TARIFFS_PATH + "sailingPermissionDueTariff.json"),
            SailingPermissionTariff.class);

    pilotageDueTariff =
        mapper.readValue(
            new File(TARIFFS_PATH + "pilotageDueTariff.json"), PilotageDueTariff.class);

    tugDueTariff =
        mapper.readValue(new File(TARIFFS_PATH + "tugDueTariff.json"), TugDueTariff.class);

    agencyDuesTariff =
        mapper.readValue(new File(TARIFFS_PATH + "agencyDuesTariff.json"), AgencyDuesTariff.class);
  }
}
