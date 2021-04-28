package flagship.domain.calculators.servicedues;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import flagship.domain.calculators.HolidayCalendar;
import flagship.domain.calculators.tariffs.serviceduestariffs.PilotageDueTariff;
import flagship.domain.cases.dto.PdaCase;
import flagship.domain.cases.dto.PdaShip;
import flagship.domain.cases.entities.Warning;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import static flagship.domain.calculators.tariffs.enums.PdaWarning.HOLIDAY;
import static flagship.domain.calculators.tariffs.enums.PdaWarning.PILOT;
import static java.time.Month.DECEMBER;
import static java.time.Month.JANUARY;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Disabled("Disabled until the warning model is clarified")
@DisplayName("Pilotage due warning generator tests")
class PilotageDueWarningsGeneratorTest {

  private static HolidayCalendar calendar;
  private static PilotageDueTariff tariff;
  private final PilotageDueWarningsGenerator warningsGenerator = new PilotageDueWarningsGenerator();
  private PdaCase testCase;

  @BeforeAll
  public static void beforeClass() throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    calendar =
        mapper.readValue(
            new File("src/main/resources/holidayCalendar.json"), HolidayCalendar.class);
    tariff =
        mapper.readValue(
            new File("src/main/resources/pilotageDueTariff.json"), PilotageDueTariff.class);
  }

  @BeforeEach
  void setUp() {
    testCase = new PdaCase();
    PdaShip testShip = PdaShip.builder().requiresSpecialPilot(false).build();
    testCase.setShip(testShip);
    testCase.setEstimatedDateOfArrival(LocalDate.of(LocalDate.now().getYear(), JANUARY, 4));
    testCase.setEstimatedDateOfDeparture(LocalDate.of(LocalDate.now().getYear(), JANUARY, 5));
  }

  @DisplayName("Should return zero warnings")
  @Test
  void testReturnsZeroWarnings() {
    Set<Warning> warnings = warningsGenerator.generateWarnings(testCase, calendar, tariff);
    assertThat(warnings.isEmpty()).isTrue();
  }

  @DisplayName("Should return arrival warning")
  @Test
  void testReturnsArrivalWarning() {

    LocalDate date = calendar.getHolidayCalendar().stream().findAny().get();
    testCase.setEstimatedDateOfArrival(date);

    Set<Warning> warnings = warningsGenerator.generateWarnings(testCase, calendar, tariff);

    Warning warning = warnings.stream().findFirst().get();

    BigDecimal expected = tariff.getIncreaseCoefficientsByWarningType().get(HOLIDAY);
    BigDecimal result = warning.getWarningCoefficient();

    SoftAssertions assertions = new SoftAssertions();
    assertions.assertThat(warnings.size()).isEqualTo(1);
    assertions.assertThat(result).isEqualByComparingTo(expected);
    assertions.assertAll();
  }

  @DisplayName("Should return departure warning")
  @Test
  void testReturnsDepartureWarning() {

    LocalDate date = calendar.getHolidayCalendar().stream().findAny().get();
    testCase.setEstimatedDateOfDeparture(date);

    Set<Warning> warnings = warningsGenerator.generateWarnings(testCase, calendar, tariff);

    Warning warning = warnings.stream().findFirst().get();

    BigDecimal expected = tariff.getIncreaseCoefficientsByWarningType().get(HOLIDAY);
    BigDecimal result = warning.getWarningCoefficient();

    SoftAssertions assertions = new SoftAssertions();
    assertions.assertThat(warnings.size()).isEqualTo(1);
    assertions.assertThat(result).isEqualByComparingTo(expected);
    assertions.assertAll();
  }

  @DisplayName("Should return arrival and departure warning")
  @Test
  void testReturnsArrivalAndDepartureWarning() {

    LocalDate arrivalDate = LocalDate.of(LocalDate.now().getYear(), DECEMBER, 24);
    LocalDate departureDate = LocalDate.of(LocalDate.now().getYear(), DECEMBER, 25);

    testCase.setEstimatedDateOfArrival(arrivalDate);
    testCase.setEstimatedDateOfDeparture(departureDate);

    Set<Warning> warnings = warningsGenerator.generateWarnings(testCase, calendar, tariff);

    BigDecimal expected =
        tariff.getIncreaseCoefficientsByWarningType().get(HOLIDAY).multiply(BigDecimal.valueOf(2));

    BigDecimal result =
        warnings.stream()
            .map(Warning::getWarningCoefficient)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    SoftAssertions assertions = new SoftAssertions();
    assertions.assertThat(warnings.size()).isEqualTo(2);
    assertions.assertThat(result).isEqualByComparingTo(expected);
    assertions.assertAll();
  }

  @DisplayName("Should return pilot warning")
  @Test
  void testReturnsPilotWarning() {

    testCase.getShip().setRequiresSpecialPilot(true);

    Set<Warning> warnings = warningsGenerator.generateWarnings(testCase, calendar, tariff);

    Warning warning = warnings.stream().findFirst().get();

    BigDecimal expected = tariff.getIncreaseCoefficientsByWarningType().get(PILOT);
    BigDecimal result = warning.getWarningCoefficient();

    SoftAssertions assertions = new SoftAssertions();
    assertions.assertThat(warnings.size()).isEqualTo(1);
    assertions.assertThat(result).isEqualByComparingTo(expected);
    assertions.assertAll();
  }

  @DisplayName("Should return arrival, departure and pilot warning")
  @Test
  void testReturnsAllWarnings() {

    LocalDate arrivalDate = LocalDate.of(LocalDate.now().getYear(), DECEMBER, 24);
    LocalDate departureDate = LocalDate.of(LocalDate.now().getYear(), DECEMBER, 25);

    testCase.setEstimatedDateOfArrival(arrivalDate);
    testCase.setEstimatedDateOfDeparture(departureDate);
    testCase.getShip().setRequiresSpecialPilot(true);

    Set<Warning> warnings = warningsGenerator.generateWarnings(testCase, calendar, tariff);

    SoftAssertions assertions = new SoftAssertions();
    assertions.assertThat(warnings.size()).isEqualTo(3);
    assertions.assertAll();
  }
}
