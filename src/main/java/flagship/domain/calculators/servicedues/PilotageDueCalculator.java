package flagship.domain.calculators.servicedues;

import flagship.domain.calculators.tariffs.serviceduestariffs.PilotageDueTariff;
import flagship.domain.cases.dto.PdaCase;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Map;

// todo: implement logic for increase coefficient by ETA and ETD
@RequiredArgsConstructor
public class PilotageDueCalculator {

  public BigDecimal calculateFor(final PdaCase source, final PilotageDueTariff tariff) {

    BigDecimal pilotageDue = getFixedPilotageDue(source, tariff);

    if (grossTonnageIsAboveThreshold(source, tariff)) {
      pilotageDue = pilotageDue.add(calculateTotalIncrease(source, tariff));
    }

    BigDecimal increaseCoefficient = getIncreaseCoefficient(source, tariff);

    if (increaseCoefficient.doubleValue() > 0) {
      pilotageDue = pilotageDue.add(pilotageDue.multiply(increaseCoefficient));
    }

    return pilotageDue;
  }

  private BigDecimal getFixedPilotageDue(final PdaCase source, final PilotageDueTariff tariff) {
    return tariff
        .getPilotageDuesByArea()
        .get(source.getPort().getPilotageArea())
        .entrySet()
        .stream()
        .filter(
            entry ->
                source.getShip().getGrossTonnage().intValue() >= entry.getValue()[0]
                    && source.getShip().getGrossTonnage().intValue() <= entry.getValue()[1])
        .map(Map.Entry::getKey)
        .findFirst()
        .orElse(getBiggestFixedPilotageDue(source, tariff));
  }

  private BigDecimal getBiggestFixedPilotageDue(
      final PdaCase source, final PilotageDueTariff tariff) {
    return tariff.getPilotageDuesByArea().get(source.getPort().getPilotageArea()).keySet().stream()
        .max(Comparator.naturalOrder())
        .get();
  }

  private boolean grossTonnageIsAboveThreshold(
      final PdaCase source, final PilotageDueTariff tariff) {
    return source.getShip().getGrossTonnage().intValue()
        >= tariff.getGrossTonnageThreshold().intValue();
  }

  private BigDecimal calculateTotalIncrease(final PdaCase source, final PilotageDueTariff tariff) {
    return getDueIncreaseValue(source, tariff).multiply(evaluateMultiplier(source, tariff));
  }

  private BigDecimal getDueIncreaseValue(final PdaCase source, final PilotageDueTariff tariff) {
    return tariff.getPilotageDuesByArea().get(source.getPort().getPilotageArea()).values().stream()
        .findFirst()
        .map(array -> BigDecimal.valueOf(array[2]))
        .get();
  }

  private BigDecimal evaluateMultiplier(final PdaCase source, final PilotageDueTariff tariff) {

    final double grossTonnage = source.getShip().getGrossTonnage().doubleValue();
    final double grossTonnageThreshold = tariff.getGrossTonnageThreshold().doubleValue();

    final double a = (grossTonnage - grossTonnageThreshold) / 1000;
    final double b = (int) a;
    final double c = a - Math.floor(a) > 0 ? 1 : 0;
    final double multiplier = b + c == 0 ? 1 : b + c;

    return BigDecimal.valueOf(multiplier);
  }

  private BigDecimal getIncreaseCoefficient(final PdaCase source, final PilotageDueTariff tariff) {
    return tariff.getIncreaseCoefficientsByCargoType().entrySet().stream()
        .filter(entry -> entry.getKey() == source.getCargoType())
        .map(Map.Entry::getValue)
        .max(Comparator.naturalOrder())
        .orElse(BigDecimal.valueOf(0.00));
  }
}
