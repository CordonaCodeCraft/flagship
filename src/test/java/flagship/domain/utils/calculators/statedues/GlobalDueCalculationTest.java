package flagship.domain.utils.calculators.statedues;

import com.fasterxml.jackson.databind.ObjectMapper;
import flagship.domain.cases.entities.Case;
import flagship.domain.cases.entities.Port;
import flagship.domain.cases.entities.Ship;
import flagship.domain.utils.tariffs.TonnageDueTariff;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;

import static flagship.domain.cases.entities.enums.CallPurpose.LOADING;
import static flagship.domain.cases.entities.enums.PortArea.FIRST;
import static flagship.domain.cases.entities.enums.ShipType.GENERAL;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("Global due calculator tests")
public class GlobalDueCalculationTest implements DueCalculatorTest {

    private static TonnageDueTariff tariff;
    private final TonnageDueCalculator tonnageDueCalculator = new TonnageDueCalculator();
    private Case testCase;

    @BeforeAll
    public static void beforeClass() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        tariff = mapper.readValue(new File("src/main/resources/tonnageDueTariff.json"), TonnageDueTariff.class);
    }


    @BeforeEach
    void setUp() {
        Port testPort = Port.builder().area(FIRST).build();
        Ship testShip = Ship.builder().grossTonnage(1650).type(GENERAL).build();
        testCase = Case.builder().ship(testShip).callPurpose(LOADING).port(testPort).callCount(1).build();
    }

    @DisplayName("Global tonnage due calculation")
    @Test
    void testTonnageDueCalculation() {

        BigDecimal tonnageDue = tonnageDueCalculator.calculateDue(testCase, tariff);
        BigDecimal discountCoefficient = tonnageDueCalculator.evaluateDiscountCoefficient(testCase, tariff);

        BigDecimal expected = tonnageDue;

        if (discountCoefficient.doubleValue() > 0) {
            BigDecimal discount = tonnageDue.multiply(discountCoefficient);
            expected = tonnageDue.subtract(discount);
        }

        BigDecimal result = tonnageDueCalculator.calculate(testCase, tariff);

        assertThat(result).isEqualByComparingTo(expected);
    }


    @DisplayName("Should return total due if discount is zero")
    @Test
    void testFinalDueIsEqualToTotalDue() {

        BigDecimal totalDue = BigDecimal.valueOf(25.50);
        BigDecimal discountCoefficient = BigDecimal.ZERO;

        BigDecimal result = tonnageDueCalculator.calculateDueAfterDiscount(totalDue, discountCoefficient);

        assertThat(totalDue).isEqualByComparingTo(result);
    }


    @DisplayName("Should return reduced total due decreased by discount")
    @Test
    void testFinalDueIsDecreasedByDiscount() {

        BigDecimal totalDue = BigDecimal.valueOf(25.50);
        BigDecimal discountCoefficient = BigDecimal.valueOf(0.7);
        BigDecimal discount = totalDue.multiply(discountCoefficient);

        BigDecimal expected = totalDue.subtract(discount);
        BigDecimal result = tonnageDueCalculator.calculateDueAfterDiscount(totalDue, discountCoefficient);

        assertThat(expected).isEqualByComparingTo(result);
    }


}
