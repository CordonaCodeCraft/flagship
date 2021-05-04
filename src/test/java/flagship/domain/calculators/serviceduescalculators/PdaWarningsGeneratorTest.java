package flagship.domain.calculators.serviceduescalculators;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import flagship.domain.tariffs.PdaWarningsGenerator;
import flagship.domain.tariffs.serviceduestariffs.MooringDueTariff;
import flagship.domain.tariffs.serviceduestariffs.PilotageDueTariff;
import flagship.domain.tariffs.serviceduestariffs.TugDueTariff;
import flagship.domain.tariffs.stateduestariffs.WharfDueTariff;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;

import java.io.File;
import java.io.IOException;

@Disabled("Disabled until the warning model is clarified")
@DisplayName("Pilotage due warning generator tests")
class PdaWarningsGeneratorTest {

  private static TugDueTariff tugDueTariff;
  private static WharfDueTariff wharfDueTariff;
  private static MooringDueTariff mooringDueTariff;
  private static PilotageDueTariff pilotageDueTariff;
  private static PdaWarningsGenerator warningsGenerator;

  @BeforeAll
  public static void beforeClass() throws IOException {

    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());

    tugDueTariff =
        mapper.readValue(new File("src/main/resources/tugDueTariff.json"), TugDueTariff.class);
    wharfDueTariff =
        mapper.readValue(new File("src/main/resources/wharfDueTariff.json"), WharfDueTariff.class);
    mooringDueTariff =
        mapper.readValue(
            new File("src/main/resources/mooringDueTariff.json"), MooringDueTariff.class);
    pilotageDueTariff =
        mapper.readValue(
            new File("src/main/resources/pilotageDueTariff.json"), PilotageDueTariff.class);
    warningsGenerator =
        new PdaWarningsGenerator(wharfDueTariff, pilotageDueTariff, tugDueTariff, mooringDueTariff);
  }
}
