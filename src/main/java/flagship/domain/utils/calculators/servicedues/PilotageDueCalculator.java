package flagship.domain.utils.calculators.servicedues;

import flagship.domain.cases.entities.Cargo;
import flagship.domain.cases.entities.Case;
import flagship.domain.cases.entities.enums.PilotageArea;
import flagship.domain.utils.tariffs.serviceduestariffs.PilotageDueTariff;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Map;

@RequiredArgsConstructor
public class PilotageDueCalculator {

    public BigDecimal calculateDue(final Case source, final PilotageDueTariff tariff) {

        BigDecimal pilotageDue = getFixedPilotageDue(source, tariff);

        final boolean isAboveGrossTonnageThreshold = evaluateThreshold(source, tariff);

        if (isAboveGrossTonnageThreshold) {
            final BigDecimal totalIncrease = calculateTotalIncrease(source, tariff);
            pilotageDue = pilotageDue.add(totalIncrease);
        }

        BigDecimal increaseCoefficient = evaluateIncreaseCoefficient(source, tariff);

        if (increaseCoefficient.doubleValue() > 0) {
            final BigDecimal increase = pilotageDue.multiply(increaseCoefficient);
            pilotageDue = pilotageDue.add(increase);
        }

        return pilotageDue;
    }


    private BigDecimal getFixedPilotageDue(final Case source, final PilotageDueTariff tariff) {

        final PilotageArea pilotageArea = source.getPort().getPilotageArea();
        final Integer grossTonnage = source.getShip().getGrossTonnage();

        return tariff.getPilotageDuesByArea().get(pilotageArea)
                .entrySet()
                .stream()
                .filter(entry -> grossTonnage >= entry.getValue()[0] && grossTonnage <= entry.getValue()[1])
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(getBiggestFixedPilotageDue(source, tariff));
    }

    private BigDecimal getBiggestFixedPilotageDue(final Case source, final PilotageDueTariff tariff) {

        final PilotageArea pilotageArea = source.getPort().getPilotageArea();

        return tariff.getPilotageDuesByArea().get(pilotageArea)
                .keySet()
                .stream()
                .max(Comparator.naturalOrder())
                .get();
    }

    private boolean evaluateThreshold(final Case source, final PilotageDueTariff tariff) {
        final Integer grossTonnage = source.getShip().getGrossTonnage();
        return grossTonnage >= tariff.getGrossTonnageThreshold().intValue();
    }

    private BigDecimal calculateTotalIncrease(final Case source, final PilotageDueTariff tariff) {
        final BigDecimal dueIncreaseValue = getDueIncreaseValue(source, tariff);
        final BigDecimal multiplier = evaluateMultiplier(source, tariff);
        return dueIncreaseValue.multiply(multiplier);
    }

    private BigDecimal getDueIncreaseValue(final Case source, final PilotageDueTariff tariff) {

        final PilotageArea pilotageArea = source.getPort().getPilotageArea();

        return tariff.getPilotageDuesByArea().get(pilotageArea)
                .values()
                .stream()
                .findFirst()
                .map(array -> BigDecimal.valueOf(array[2]))
                .get();
    }

    private BigDecimal evaluateMultiplier(final Case source, final PilotageDueTariff tariff) {

        double grossTonnage = source.getShip().getGrossTonnage().doubleValue();
        double grossTonnageThreshold = tariff.getGrossTonnageThreshold().doubleValue();

        double a = (grossTonnage - grossTonnageThreshold) / 1000;
        double b = (int) a;
        double c = a - Math.floor(a);
        c = c > 0 ? 1 : 0;

        double multiplier = b + c == 0 ? 1 : b + c;

        return BigDecimal.valueOf(multiplier);
    }

    private BigDecimal evaluateIncreaseCoefficient(final Case source, final PilotageDueTariff tariff) {
        return tariff.getIncreaseCoefficientsByCargoType()
                .entrySet()
                .stream()
                .filter(entry ->
                        source.getCargos()
                                .stream()
                                .map(Cargo::getType)
                                .anyMatch(cargoType -> cargoType == entry.getKey())
                )
                .map(Map.Entry::getValue)
                .max(Comparator.naturalOrder())
                .orElse(BigDecimal.valueOf(0.00));
    }

}


