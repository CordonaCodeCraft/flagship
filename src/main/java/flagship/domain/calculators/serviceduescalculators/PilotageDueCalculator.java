package flagship.domain.calculators.serviceduescalculators;

import flagship.domain.cases.dto.PdaCase;
import flagship.domain.tariffs.Tariff;
import flagship.domain.tariffs.serviceduestariffs.PilotageDueTariff;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static flagship.domain.tariffs.PdaWarningsGenerator.PdaWarning.*;

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

    BigDecimal pilotageDue =
        getFixedDue(source, source.getPort().getPilotageArea(), tariff.getPilotageDuesByArea());

    if (grossTonnageIsAboveThreshold(source, tariff.getGrossTonnageThreshold())) {
      pilotageDue = pilotageDue.add(calculateAdditionalDue());
    }

    BigDecimal increaseCoefficient = getIncreaseCoefficient();

    if (increaseCoefficient.doubleValue() > 0) {
      return pilotageDue.multiply(increaseCoefficient);
    }

    return pilotageDue;
  }

  private BigDecimal calculateAdditionalDue() {
    return getAdditionalDueValue(source.getPort().getPilotageArea(), tariff.getPilotageDuesByArea())
        .multiply(getMultiplier());
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

    BigDecimal increaseCoefficientForHazardousCargo =
        source.getWarnings().contains(HAZARDOUS_PILOTAGE_CARGO)
            ? tariff.getIncreaseCoefficientsByWarningType().get(HAZARDOUS_PILOTAGE_CARGO)
            : BigDecimal.ZERO;

    BigDecimal increaseCoefficientForSpecialCargo =
        source.getWarnings().contains(SPECIAL_PILOTAGE_CARGO)
            ? tariff.getIncreaseCoefficientsByWarningType().get(SPECIAL_PILOTAGE_CARGO)
            : BigDecimal.ZERO;

    increaseCoefficients.add(
        increaseCoefficientForHazardousCargo.max(increaseCoefficientForSpecialCargo));

    BigDecimal increaseCoefficientByPilot =
        source.getWarnings().contains(SPECIAL_PILOT)
            ? tariff.getIncreaseCoefficientsByWarningType().get(SPECIAL_PILOT)
            : BigDecimal.ZERO;

    increaseCoefficients.add(increaseCoefficientByPilot);

    return increaseCoefficients.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
  }
}
