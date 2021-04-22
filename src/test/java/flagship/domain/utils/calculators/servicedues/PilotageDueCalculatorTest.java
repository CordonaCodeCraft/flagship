package flagship.domain.utils.calculators.servicedues;

import com.fasterxml.jackson.databind.ObjectMapper;
import flagship.domain.cases.entities.Cargo;
import flagship.domain.cases.entities.Case;
import flagship.domain.cases.entities.Port;
import flagship.domain.cases.entities.Ship;
import flagship.domain.cases.entities.enums.PilotageArea;
import flagship.domain.utils.calculators.DueCalculatorTest;
import flagship.domain.utils.tariffs.serviceduestariffs.PilotageDueTariff;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

import static flagship.domain.cases.entities.enums.CargoType.*;
import static flagship.domain.cases.entities.enums.PilotageArea.VARNA_FIRST;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("Pilotage due calculator tests")
public class PilotageDueCalculatorTest implements DueCalculatorTest {

    private static PilotageDueTariff tariff;
    private Case testCase;
    PilotageDueCalculator calculator = new PilotageDueCalculator();

    @BeforeAll
    public static void beforeClass() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        tariff = mapper.readValue(new File("src/main/resources/pilotageDueTariff.json"), PilotageDueTariff.class);
    }

    @BeforeEach
    void setUp() {
        Port testPort = Port.builder().pilotageArea(VARNA_FIRST).build();
        Ship testShip = Ship.builder().grossTonnage(1650).build();
        Cargo regular = Cargo.builder().id(UUID.randomUUID()).type(REGULAR).build();
        Set<Cargo> cargos = new HashSet<>(Collections.singletonList(regular));
        testCase = Case.builder().port(testPort).ship(testShip).cargos(cargos).build();
    }

    @DisplayName("Should calculate correct fixed pilotage due by pilotage area")
    @ParameterizedTest(name = "pilotage area: {arguments}")
    @EnumSource(PilotageArea.class)
    void testPilotageDueCalculationWithinThreshold(PilotageArea pilotageArea) {

        int grossTonnage = getRandomGrossTonnage(0, tariff.getGrossTonnageThreshold().intValue());
        testCase.getShip().setGrossTonnage(grossTonnage);
        testCase.getPort().setPilotageArea(pilotageArea);

        BigDecimal expected = getFixedPilotageDuePerGrossTonnage(testCase);
        BigDecimal result = calculator.calculateDue(testCase, tariff);

        assertThat(result).isEqualByComparingTo(expected);
    }

    @DisplayName("Should calculate correct increased pilotage due")
    @ParameterizedTest(name = "pilotage area: {arguments}")
    @EnumSource(PilotageArea.class)
    void testCalculateIncreasedPilotageDue(PilotageArea pilotageArea) {

        int grossTonnage = getRandomGrossTonnage(tariff.getGrossTonnageThreshold().intValue() + 1, 200000);
        testCase.getShip().setGrossTonnage(grossTonnage);
        testCase.getPort().setPilotageArea(pilotageArea);

        BigDecimal fixedPilotageDue = getFixedPilotageDuePerGrossTonnage(testCase);
        BigDecimal increaseValue = getIncreaseValue(testCase);
        BigDecimal multiplier = evaluateMultiplier(grossTonnage);
        BigDecimal totalIncrease = increaseValue.multiply(multiplier);

        BigDecimal expected = fixedPilotageDue.add(totalIncrease);
        BigDecimal result = calculator.calculateDue(testCase, tariff);

        assertThat(result).isEqualByComparingTo(expected);
    }


    @DisplayName("Should increase total pilotage due by 20 percent if cargo is hazardous")
    @Test
    void shouldIncreaseTotalPilotageDueBy20Percent() {

        Cargo regular = Cargo.builder().id(UUID.randomUUID()).type(REGULAR).build();
        Cargo hazardous = Cargo.builder().id(UUID.randomUUID()).type(HAZARDOUS).build();
        Set<Cargo> cargos = new HashSet<>(Arrays.asList(regular, hazardous));
        testCase.setCargos(cargos);

        BigDecimal pilotageDue = getFixedPilotageDuePerGrossTonnage(testCase);
        BigDecimal increase = pilotageDue.multiply(tariff.getIncreaseCoefficientsByCargoType().get(HAZARDOUS));
        BigDecimal expected = pilotageDue.add(increase);
        BigDecimal result = calculator.calculateDue(testCase, tariff);

        assertThat(result).isEqualByComparingTo(expected);
    }

    @DisplayName("Should increase total pilotage due by 100 percent if cargo is special")
    @Test
    void shouldIncreaseTotalPilotageDueBy100Percent() {

        Cargo regular = Cargo.builder().id(UUID.randomUUID()).type(REGULAR).build();
        Cargo hazardous = Cargo.builder().id(UUID.randomUUID()).type(SPECIAL).build();
        Set<Cargo> cargos = new HashSet<>(Arrays.asList(regular, hazardous));
        testCase.setCargos(cargos);

        BigDecimal pilotageDue = getFixedPilotageDuePerGrossTonnage(testCase);
        BigDecimal increase = pilotageDue.multiply(tariff.getIncreaseCoefficientsByCargoType().get(SPECIAL));
        BigDecimal expected = pilotageDue.add(increase);
        BigDecimal result = calculator.calculateDue(testCase, tariff);

        assertThat(result).isEqualByComparingTo(expected);
    }

    private BigDecimal getFixedPilotageDuePerGrossTonnage(Case testCase) {
        return tariff.getPilotageDuesByArea().get(testCase.getPort().getPilotageArea())
                .entrySet()
                .stream()
                .filter(entry ->
                        testCase.getShip().getGrossTonnage() >= entry.getValue()[0]
                                && testCase.getShip().getGrossTonnage() <= entry.getValue()[1])
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(getBiggestFixedPilotageDue(testCase));
    }

    private BigDecimal getBiggestFixedPilotageDue(Case testCase) {
        return tariff.getPilotageDuesByArea().get(testCase.getPort().getPilotageArea())
                .keySet()
                .stream()
                .max(Comparator.naturalOrder())
                .get();
    }

    private BigDecimal getIncreaseValue(Case testCase) {
        return tariff.getPilotageDuesByArea().get(testCase.getPort().getPilotageArea())
                .values()
                .stream()
                .findFirst()
                .map(array -> BigDecimal.valueOf(array[2]))
                .get();
    }

    private BigDecimal evaluateMultiplier(int grossTonnage) {
        int gt = grossTonnage;
        int stopValue = tariff.getGrossTonnageThreshold().intValue() + 1;
        int factor = 0;

        if (grossTonnage == stopValue) {
            return BigDecimal.valueOf(1);
        } else {
            while (gt > stopValue) {
                gt -= 1000;
                factor++;
            }
        }

        return BigDecimal.valueOf(factor);
    }

    private int getRandomGrossTonnage(int min, int max) {
        Random random = new Random();
        return random.ints(min, max).findFirst().getAsInt();
    }


}
