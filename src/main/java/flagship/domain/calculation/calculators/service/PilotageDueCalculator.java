package flagship.domain.calculation.calculators.service;

import flagship.domain.calculation.calculators.PrivateDueCalculator;
import flagship.domain.calculation.tariffs.Tariff;
import flagship.domain.calculation.tariffs.service.PilotageDueTariff;
import flagship.domain.caze.model.PdaCase;
import flagship.domain.tuples.due.Due;
import flagship.domain.tuples.range.Range;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static flagship.domain.warning.generator.WarningsGenerator.WarningType.*;

@NoArgsConstructor
public class PilotageDueCalculator extends PrivateDueCalculator {

  private PilotageDueTariff tariff;

  @Override
  public void set(final PdaCase source, final Tariff tariff) {
    super.source = source;
    this.tariff = (PilotageDueTariff) tariff;
  }

  @Override
  public BigDecimal calculate() {

    final Map<Range, Due> dues =
        tariff.getPilotageDuesByArea().get(source.getPort().getPilotageArea());

    BigDecimal pilotageDue = getBaseDue(dues);

    if (grossTonnageExceedsThreshold(tariff.getGrossTonnageThreshold())) {
      final BigDecimal addition = getAddition(dues);
      final BigDecimal multiplier = getMultiplier();
      pilotageDue = pilotageDue.add(addition.multiply(multiplier));
    }

    final BigDecimal increaseCoefficient = getIncreaseCoefficient();

    if (increaseCoefficient.doubleValue() > 0) {
      pilotageDue = pilotageDue.add(pilotageDue.multiply(increaseCoefficient));
    }

    return pilotageDue;
  }

  private BigDecimal getMultiplier() {
    final double a =
        (source.getShip().getGrossTonnage().doubleValue()
                - tariff.getGrossTonnageThreshold().doubleValue())
            / 1000;
    final double b = (int) a;
    final double c = a - Math.floor(a) > 0 ? 1 : 0;
    final double multiplier = b + c == 0 ? 1 : b + c;
    return BigDecimal.valueOf(multiplier);
  }

  private BigDecimal getIncreaseCoefficient() {

    final List<BigDecimal> increaseCoefficients = new ArrayList<>();

    final BigDecimal increaseCoefficientForHazardousCargo =
        source.getWarningTypes().contains(HAZARDOUS_PILOTAGE_CARGO)
            ? tariff.getIncreaseCoefficientsByWarningType().get(HAZARDOUS_PILOTAGE_CARGO)
            : BigDecimal.ZERO;

    final BigDecimal increaseCoefficientForSpecialCargo =
        source.getWarningTypes().contains(SPECIAL_PILOTAGE_CARGO)
            ? tariff.getIncreaseCoefficientsByWarningType().get(SPECIAL_PILOTAGE_CARGO)
            : BigDecimal.ZERO;

    increaseCoefficients.add(
        increaseCoefficientForHazardousCargo.max(increaseCoefficientForSpecialCargo));

    final BigDecimal increaseCoefficientByPilot =
        source.getWarningTypes().contains(SPECIAL_PILOT)
            ? tariff.getIncreaseCoefficientsByWarningType().get(SPECIAL_PILOT)
            : BigDecimal.ZERO;

    increaseCoefficients.add(increaseCoefficientByPilot);

    return increaseCoefficients.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
  }
}
