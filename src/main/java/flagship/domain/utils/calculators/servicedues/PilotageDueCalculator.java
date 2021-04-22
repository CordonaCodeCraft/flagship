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

    public BigDecimal calculateDue(Case source, PilotageDueTariff tariff) {

        BigDecimal pilotageDue = getFixedPilotageDue(source, tariff);

        boolean isAboveGrossTonnageThreshold = evaluateThreshold(source, tariff);

        if (isAboveGrossTonnageThreshold) {
            BigDecimal totalIncrease = calculateTotalIncrease(source, tariff);
            pilotageDue = pilotageDue.add(totalIncrease);
        }

        BigDecimal increaseCoefficient = evaluateIncreaseCoefficient(source, tariff);

        if (increaseCoefficient.doubleValue() > 0) {
            BigDecimal increase = pilotageDue.multiply(increaseCoefficient);
            pilotageDue = pilotageDue.add(increase);
        }

        return pilotageDue;
    }


    private BigDecimal getFixedPilotageDue(Case source, PilotageDueTariff tariff) {

        PilotageArea pilotageArea = source.getPort().getPilotageArea();
        Integer grossTonnage = source.getShip().getGrossTonnage();

        return tariff.getPilotageDuesByArea().get(pilotageArea)
                .entrySet()
                .stream()
                .filter(entry -> grossTonnage >= entry.getValue()[0] && grossTonnage <= entry.getValue()[1])
                .map(Map.Entry::getKey)
                .findFirst().orElse(getBiggestFixedPilotageDue(source, tariff));
    }

    private BigDecimal getBiggestFixedPilotageDue(Case source, PilotageDueTariff tariff) {
        return tariff.getPilotageDuesByArea().get(source.getPort().getPilotageArea())
                .keySet()
                .stream()
                .max(Comparator.naturalOrder())
                .get();
    }

    private BigDecimal getDueIncreaseValue(Case source, PilotageDueTariff tariff) {
        return tariff.getPilotageDuesByArea().get(source.getPort().getPilotageArea())
                .values()
                .stream()
                .findFirst()
                .map(array -> BigDecimal.valueOf(array[2]))
                .get();
    }

    private BigDecimal evaluateMultiplier(Case source, PilotageDueTariff tariff) {
        int grossTonnage = source.getShip().getGrossTonnage();
        int stopValue = tariff.getGrossTonnageThreshold().intValue() + 1;
        int factor = 0;

        if (grossTonnage == stopValue) {
            return BigDecimal.valueOf(1);
        } else {
            while (grossTonnage > stopValue) {
                grossTonnage -= 1000;
                factor++;
            }
        }

        return BigDecimal.valueOf(factor);
    }

    private BigDecimal calculateTotalIncrease(Case source, PilotageDueTariff tariff) {
        BigDecimal dueIncreaseValue = getDueIncreaseValue(source, tariff);
        BigDecimal multiplier = evaluateMultiplier(source, tariff);
        return dueIncreaseValue.multiply(multiplier);
    }

    private boolean evaluateThreshold(Case source, PilotageDueTariff tariff) {
        Integer grossTonnage = source.getShip().getGrossTonnage();
        return grossTonnage > tariff.getGrossTonnageThreshold().intValue();
    }

    private BigDecimal evaluateIncreaseCoefficient(Case source, PilotageDueTariff tariff) {
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


