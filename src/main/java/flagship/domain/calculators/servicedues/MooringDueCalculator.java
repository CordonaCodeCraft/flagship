package flagship.domain.calculators.servicedues;

import flagship.domain.calculators.tariffs.Tariff;
import flagship.domain.calculators.tariffs.serviceduestariffs.MooringDueTariff;
import flagship.domain.calculators.tariffs.serviceduestariffs.MooringDueTariff.MooringServiceProvider;
import flagship.domain.cases.dto.PdaCase;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Map;

@NoArgsConstructor
public class MooringDueCalculator extends ServiceDueCalculator<PdaCase, Tariff> {

  private static PdaCase source;
  private static MooringDueTariff tariff;

  @Override
  public void set(final PdaCase source, final Tariff tariff) {
    MooringDueCalculator.source = source;
    MooringDueCalculator.tariff = (MooringDueTariff) tariff;
  }

  @Override
  public BigDecimal calculate() {

    BigDecimal mooringDue = BigDecimal.ZERO;

    switch (source.getPort().getMooringServiceProvider()) {
      case VTC:
        mooringDue = VtcMooringDueCalculator.calculateVtcMooringDue();
        break;
      case PORTFLEET:
        mooringDue = PortfleetMooringDueCalculator.calculatePortfleetMooringDue();
        break;
      case LESPORT:
        mooringDue = LesportMooringDueCalculator.calculateLesportMooringDue();
        break;
      case ODESSOS:
        mooringDue = OdessosMooringDueCalculator.calculateOdessosMooringDue();
        break;
      case BALCHIK:
        mooringDue = BalchikMooringDueCalculator.calculateBalchikMooringDue();
    }

    return mooringDue;
  }

  private static class VtcMooringDueCalculator {

    private static BigDecimal calculateVtcMooringDue() {
      return commonMooringDueCalculation(tariff.getVtcGrossTonnageThreshold());
    }
  }

  private static class PortfleetMooringDueCalculator {

    private static BigDecimal calculatePortfleetMooringDue() {
      return commonMooringDueCalculation(tariff.getPortfleetGrossTonnageThreshold());
    }
  }

  private static class LesportMooringDueCalculator {

    private static BigDecimal calculateLesportMooringDue() {
      return commonMooringDueCalculation(tariff.getLesportGrossTonnageThreshold());
    }
  }

  private static class OdessosMooringDueCalculator {

    public static BigDecimal calculateOdessosMooringDue() {

      BigDecimal fixedMooringDue = getFixedMooringDue(source.getPort().getMooringServiceProvider());

      if (grossTonnageIsAboveThreshold()) {
        fixedMooringDue = getAdditionalValue();
      }

      return fixedMooringDue;
    }
  }

  private static class BalchikMooringDueCalculator {

    public static BigDecimal calculateBalchikMooringDue() {

      BigDecimal fixedMooringDue = getFixedMooringDue(source.getPort().getMooringServiceProvider());

      if (grossTonnageIsAboveThreshold()) {
        fixedMooringDue = getAdditionalValue();
      }

      return fixedMooringDue.multiply(BigDecimal.valueOf(2));
    }
  }

  private static BigDecimal commonMooringDueCalculation(final BigDecimal grossTonnageThreshold) {

    BigDecimal fixedMooringDue = getFixedMooringDue(source.getPort().getMooringServiceProvider());

    if (grossTonnageIsAboveThreshold()) {
      fixedMooringDue = fixedMooringDue.add(calculateAdditionalDue(grossTonnageThreshold));
    }
    return fixedMooringDue.multiply(BigDecimal.valueOf(2));
  }

  private static BigDecimal getFixedMooringDue(final MooringServiceProvider provider) {
    return tariff.getMooringDuesByProvider().get(provider).entrySet().stream()
        .filter(MooringDueCalculator::shipGrossTonnageIsInRange)
        .map(Map.Entry::getKey)
        .findFirst()
        .orElse(getBiggestFixedMooringDue());
  }

  private static boolean shipGrossTonnageIsInRange(final Map.Entry<BigDecimal, Integer[]> entry) {
    return source.getShip().getGrossTonnage().intValue() >= entry.getValue()[0]
        && source.getShip().getGrossTonnage().intValue() <= entry.getValue()[1];
  }

  private static BigDecimal getBiggestFixedMooringDue() {
    return tariff
        .getMooringDuesByProvider()
        .get(source.getPort().getMooringServiceProvider())
        .keySet()
        .stream()
        .max(Comparator.naturalOrder())
        .get();
  }

  private static boolean grossTonnageIsAboveThreshold() {

    switch (source.getPort().getMooringServiceProvider()) {
      case VTC:
        return source.getShip().getGrossTonnage().doubleValue()
            > tariff.getVtcGrossTonnageThreshold().doubleValue();
      case PORTFLEET:
        return source.getShip().getGrossTonnage().doubleValue()
            > tariff.getPortfleetGrossTonnageThreshold().doubleValue();
      case LESPORT:
        return source.getShip().getGrossTonnage().doubleValue()
            > tariff.getLesportGrossTonnageThreshold().doubleValue();
      case ODESSOS:
        return source.getShip().getGrossTonnage().doubleValue()
            > tariff.getOdessosGrossTonnageThreshold().doubleValue();
      case BALCHIK:
        return source.getShip().getGrossTonnage().doubleValue()
            > tariff.getBalchikGrossTonnageThreshold().doubleValue();
    }

    return false;
  }

  private static BigDecimal calculateAdditionalDue(final BigDecimal grossTonnageThreshold) {
    final BigDecimal additionalValue = getAdditionalValue();
    final BigDecimal multiplier = getMultiplier(grossTonnageThreshold);
    return additionalValue.multiply(multiplier);
  }

  private static BigDecimal getAdditionalValue() {
    return BigDecimal.valueOf(
        tariff
            .getMooringDuesByProvider()
            .get(source.getPort().getMooringServiceProvider())
            .get(getBiggestFixedMooringDue())[2]);
  }

  private static BigDecimal getMultiplier(final BigDecimal threshold) {
    final double a = (source.getShip().getGrossTonnage().doubleValue() - threshold.doubleValue()) / 1000;
    final double b = a - Math.floor(a) > 0 ? 1 : 0;

    return BigDecimal.valueOf((int) a + b);
  }
}
