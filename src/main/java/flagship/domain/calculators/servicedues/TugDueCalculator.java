package flagship.domain.calculators.servicedues;

import flagship.domain.calculators.DueCalculator;
import flagship.domain.calculators.tariffs.serviceduestariffs.TugDueTariff;
import flagship.domain.cases.dto.PdaCase;

import java.math.BigDecimal;
import java.util.*;

import static flagship.domain.calculators.tariffs.enums.PdaWarning.HOLIDAY;

public class TugDueCalculator implements DueCalculator<PdaCase, TugDueTariff> {

  @Override
  public BigDecimal calculateFor(PdaCase source, TugDueTariff tariff) {

    BigDecimal tugDue = getFixedTugDuePerGrossTonnage(source, tariff);

    if (grossTonnageIsAboveThreshold(source, tariff)) {
      BigDecimal totalIncrease =
          getIncreaseValue(source, tariff).multiply(getMultiplier(source, tariff));

      tugDue = tugDue.add(totalIncrease);
    }

    tugDue = tugDue.multiply(getTugCount(source, tariff));

    BigDecimal increaseCoefficient = getIncreaseCoefficient(source, tariff);

    if (increaseCoefficient.doubleValue() > 0) {
      return tugDue.multiply(increaseCoefficient);
    }

    return tugDue;
  }

  private BigDecimal getFixedTugDuePerGrossTonnage(PdaCase source, TugDueTariff tariff) {
    return tariff.getTugDuesByArea().get(source.getPort().getTugArea()).entrySet().stream()
        .filter(entry -> shipGrossTonnageIsInRange(source, entry))
        .map(Map.Entry::getKey)
        .findFirst()
        .orElse(getBiggestFixedTugDue(source, tariff));
  }

  private boolean grossTonnageIsAboveThreshold(PdaCase source, TugDueTariff tariff) {
    return source.getShip().getGrossTonnage().doubleValue()
        > tariff.getGrossTonnageThreshold().doubleValue();
  }

  private BigDecimal getIncreaseValue(PdaCase source, TugDueTariff tariff) {
    return BigDecimal.valueOf(
        tariff
            .getTugDuesByArea()
            .get(source.getPort().getTugArea())
            .get(getBiggestFixedTugDue(source, tariff))[2]);
  }

  private BigDecimal getMultiplier(PdaCase source, TugDueTariff tariff) {
    double a =
        (source.getShip().getGrossTonnage().doubleValue()
                - tariff.getGrossTonnageThreshold().doubleValue())
            / 1000;
    double b = a - Math.floor(a) > 0 ? 1 : 0;

    return BigDecimal.valueOf((int) a + b);
  }

  private boolean shipGrossTonnageIsInRange(
      PdaCase source, Map.Entry<BigDecimal, Integer[]> entry) {
    return source.getShip().getGrossTonnage().intValue() >= entry.getValue()[0]
        && source.getShip().getGrossTonnage().intValue() <= entry.getValue()[1];
  }

  private BigDecimal getBiggestFixedTugDue(PdaCase source, TugDueTariff tariff) {
    return tariff.getTugDuesByArea().get(source.getPort().getTugArea()).keySet().stream()
        .max(Comparator.naturalOrder())
        .get();
  }

  private BigDecimal getTugCount(PdaCase source, TugDueTariff tariff) {

    BigDecimal tugCount =
        tariff.getTugCountByGrossTonnage().entrySet().stream()
            .filter(entry -> shipGrossTonnageIsInRange(source, entry))
            .map(Map.Entry::getKey)
            .findFirst()
            .orElse(tariff.getMaximumTugCount());

    if (isNotEligibleForTugCountReduce(source, tariff)) {
      return tugCount;
    } else {
      return tugCount.intValue() == 1
          ? BigDecimal.valueOf(1)
          : tugCount.subtract(BigDecimal.valueOf(1));
    }
  }

  private boolean isNotEligibleForTugCountReduce(PdaCase source, TugDueTariff tariff) {
    return !source.getShip().getHasIncreasedManeuverability()
        || (source.getShip().getTransportsDangerousCargo()
            && source.getShip().getGrossTonnage().intValue()
                < tariff.getGrossTonnageThresholdForTugCountReduce().intValue());
  }

  private BigDecimal getIncreaseCoefficient(PdaCase source, TugDueTariff tariff) {

    List<BigDecimal> increaseCoefficients = new ArrayList<>();

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
