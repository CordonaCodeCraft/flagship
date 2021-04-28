package flagship.domain.calculators.servicedues;

import flagship.domain.calculators.tariffs.serviceduestariffs.PilotageDueTariff;
import flagship.domain.cases.dto.PdaCase;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.*;

import static flagship.domain.calculators.tariffs.enums.PdaWarning.HOLIDAY;
import static flagship.domain.calculators.tariffs.enums.PdaWarning.PILOT;

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

    if (Optional.ofNullable(source.getEstimatedDateOfArrival()).isPresent()) {
      BigDecimal increaseCoefficientByETA =
          tariff.getHolidayCalendar().contains(source.getEstimatedDateOfArrival())
              ? tariff.getIncreaseCoefficientsByWarningType().get(HOLIDAY)
              : BigDecimal.ZERO;
      increaseCoefficients.add(increaseCoefficientByETA);
    }

    if (Optional.ofNullable(source.getEstimatedDateOfDeparture()).isPresent()) {
      BigDecimal increaseCoefficientByETD =
          tariff.getHolidayCalendar().contains(source.getEstimatedDateOfDeparture())
              ? tariff.getIncreaseCoefficientsByWarningType().get(HOLIDAY)
              : BigDecimal.ZERO;
      increaseCoefficients.add(increaseCoefficientByETD);
    }

    return increaseCoefficients.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
  }
}
