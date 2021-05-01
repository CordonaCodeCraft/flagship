package flagship.domain.calculators.servicedues;

import flagship.domain.calculators.tariffs.Tariff;
import flagship.domain.calculators.tariffs.serviceduestariffs.PilotageDueTariff;
import flagship.domain.cases.dto.PdaCase;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static flagship.domain.calculators.tariffs.enums.PdaWarning.PILOT;

@NoArgsConstructor
public class PilotageDueCalculator extends ServiceDueCalculator<PdaCase, Tariff> {

  private PdaCase source;
  private PilotageDueTariff tariff;

  public void set(final PdaCase source, final Tariff tariff) {
    this.source = source;
    this.tariff = (PilotageDueTariff) tariff;
  }

  @Override
  public BigDecimal calculate() {

    BigDecimal pilotageDue = getFixedPilotageDue();

    if (grossTonnageIsAboveThreshold()) {
      pilotageDue = pilotageDue.add(calculateAdditionalDue());
    }

    BigDecimal increaseCoefficient = getIncreaseCoefficient();

    if (increaseCoefficient.doubleValue() > 0) {
      return pilotageDue.multiply(increaseCoefficient);
    }

    return pilotageDue;
  }

  private BigDecimal getFixedPilotageDue() {
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
        .orElse(getBiggestFixedPilotageDue());
  }

  private BigDecimal getBiggestFixedPilotageDue() {
    return tariff.getPilotageDuesByArea().get(source.getPort().getPilotageArea()).keySet().stream()
        .max(Comparator.naturalOrder())
        .get();
  }

  private boolean grossTonnageIsAboveThreshold() {
    return source.getShip().getGrossTonnage().intValue()
        >= tariff.getGrossTonnageThreshold().intValue();
  }

  private BigDecimal calculateAdditionalDue() {
    return getDueAdditionalValue().multiply(getMultiplier());
  }

  private BigDecimal getDueAdditionalValue() {
    return tariff.getPilotageDuesByArea().get(source.getPort().getPilotageArea()).values().stream()
        .findFirst()
        .map(array -> BigDecimal.valueOf(array[2]))
        .get();
  }

  private BigDecimal getMultiplier() {

    final double grossTonnage = source.getShip().getGrossTonnage().doubleValue();
    final double grossTonnageThreshold = tariff.getGrossTonnageThreshold().doubleValue();

    final double a = (grossTonnage - grossTonnageThreshold) / 1000;
    final double b = (int) a;
    final double c = a - Math.floor(a) > 0 ? 1 : 0;
    final double multiplier = b + c == 0 ? 1 : b + c;

    return BigDecimal.valueOf(multiplier);
  }

  private BigDecimal getIncreaseCoefficient() {

    List<BigDecimal> increaseCoefficients = new ArrayList<>();

    BigDecimal increaseCoefficientByCargoType =
        tariff.getIncreaseCoefficientsByCargoType().entrySet().stream()
            .filter(entry -> entry.getKey() == source.getCargoType())
            .map(Map.Entry::getValue)
            .max(Comparator.naturalOrder())
            .orElse(BigDecimal.ZERO);

    increaseCoefficients.add(increaseCoefficientByCargoType);

    BigDecimal increaseCoefficientByPilot =
        source.getShip().getRequiresSpecialPilot()
            ? tariff.getIncreaseCoefficientsByWarningType().get(PILOT)
            : BigDecimal.ZERO;

    increaseCoefficients.add(increaseCoefficientByPilot);

    return increaseCoefficients.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
  }
}
