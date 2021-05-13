package flagship.domain.calculators.serviceduescalculators;

import flagship.domain.calculators.PrivateDueCalculator;
import flagship.domain.cases.dto.PdaCase;
import flagship.domain.tariffs.PilotageDueTariff;
import flagship.domain.tariffs.Tariff;
import flagship.domain.tariffs.mix.Due;
import flagship.domain.tariffs.mix.Range;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static flagship.domain.PdaWarningsGenerator.PdaWarning.*;

@NoArgsConstructor
public class PilotageDueCalculator extends PrivateDueCalculator {

  private PilotageDueTariff tariff;

  public void set(final PdaCase source, final Tariff tariff) {
    super.source = source;
    this.tariff = (PilotageDueTariff) tariff;
  }

  @Override
  public BigDecimal calculate() {

    Map<Range, Due> dues = tariff.getPilotageDuesByArea().get(source.getPort().getPilotageArea());

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

    List<BigDecimal> increaseCoefficients = new ArrayList<>();

    final BigDecimal increaseCoefficientForHazardousCargo =
        source.getWarnings().contains(HAZARDOUS_PILOTAGE_CARGO)
            ? tariff.getIncreaseCoefficientsByWarningType().get(HAZARDOUS_PILOTAGE_CARGO)
            : BigDecimal.ZERO;

    final BigDecimal increaseCoefficientForSpecialCargo =
        source.getWarnings().contains(SPECIAL_PILOTAGE_CARGO)
            ? tariff.getIncreaseCoefficientsByWarningType().get(SPECIAL_PILOTAGE_CARGO)
            : BigDecimal.ZERO;

    increaseCoefficients.add(
        increaseCoefficientForHazardousCargo.max(increaseCoefficientForSpecialCargo));

    final BigDecimal increaseCoefficientByPilot =
        source.getWarnings().contains(SPECIAL_PILOT)
            ? tariff.getIncreaseCoefficientsByWarningType().get(SPECIAL_PILOT)
            : BigDecimal.ZERO;

    increaseCoefficients.add(increaseCoefficientByPilot);

    return increaseCoefficients.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
  }
}
