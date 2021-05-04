package flagship.domain.calculators.serviceduescalculators;

import flagship.domain.tariffs.Tariff;
import flagship.domain.tariffs.serviceduestariffs.MooringDueTariff;
import flagship.domain.cases.dto.PdaCase;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
public class MooringDueCalculator extends ServiceDueCalculator<PdaCase, Tariff> {

  private PdaCase source;
  private MooringDueTariff tariff;

  @Override
  public void set(final PdaCase source, final Tariff tariff) {
    this.source = source;
    this.tariff = (MooringDueTariff) tariff;
  }

  @Override
  public BigDecimal calculate() {

    BigDecimal mooringDue = BigDecimal.ZERO;

    switch (source.getPort().getMooringServiceProvider()) {
      case VTC:
        mooringDue = new VtcMooringDueCalculator().calculateVtcMooringDue();
        break;
      case PORTFLEET:
        mooringDue = new PortfleetMooringDueCalculator().calculatePortfleetMooringDue();
        break;
      case LESPORT:
        mooringDue = new LesportMooringDueCalculator().calculateLesportMooringDue();
        break;
      case ODESSOS:
        mooringDue = new OdessosMooringDueCalculator().calculateOdessosMooringDue();
        break;
      case BALCHIK:
        mooringDue = new BalchikMooringDueCalculator().calculateBalchikMooringDue();
    }

    return mooringDue;
  }

  private class VtcMooringDueCalculator {

    private BigDecimal calculateVtcMooringDue() {
      return commonMooringDueCalculation(tariff.getVtcGrossTonnageThreshold());
    }
  }

  private class PortfleetMooringDueCalculator {

    private BigDecimal calculatePortfleetMooringDue() {
      return commonMooringDueCalculation(tariff.getPortfleetGrossTonnageThreshold());
    }
  }

  private class LesportMooringDueCalculator {

    private BigDecimal calculateLesportMooringDue() {
      return commonMooringDueCalculation(tariff.getLesportGrossTonnageThreshold());
    }
  }

  private class OdessosMooringDueCalculator {

    public BigDecimal calculateOdessosMooringDue() {

      BigDecimal fixedMooringDue =
          getFixedDue(
              source,
              source.getPort().getMooringServiceProvider(),
              tariff.getMooringDuesByProvider());

      if (grossTonnageIsAboveThreshold()) {
        fixedMooringDue =
            getAdditionalDueValue(
                source.getPort().getMooringServiceProvider(), tariff.getMooringDuesByProvider());
      }

      return fixedMooringDue;
    }
  }

  private class BalchikMooringDueCalculator {

    public BigDecimal calculateBalchikMooringDue() {

      BigDecimal fixedMooringDue =
          getFixedDue(
              source,
              source.getPort().getMooringServiceProvider(),
              tariff.getMooringDuesByProvider());

      if (grossTonnageIsAboveThreshold()) {
        fixedMooringDue =
            getAdditionalDueValue(
                source.getPort().getMooringServiceProvider(), tariff.getMooringDuesByProvider());
      }

      return fixedMooringDue.multiply(BigDecimal.valueOf(2.00));
    }
  }

  private BigDecimal commonMooringDueCalculation(final BigDecimal grossTonnageThreshold) {

    BigDecimal fixedMooringDue =
        getFixedDue(
            source,
            source.getPort().getMooringServiceProvider(),
            tariff.getMooringDuesByProvider());

    if (grossTonnageIsAboveThreshold()) {
      fixedMooringDue = fixedMooringDue.add(calculateAdditionalDue(grossTonnageThreshold));
    }
    return fixedMooringDue.multiply(BigDecimal.valueOf(2.00));
  }

  private boolean grossTonnageIsAboveThreshold() {

    switch (source.getPort().getMooringServiceProvider()) {
      case VTC:
        return grossTonnageIsAboveThreshold(source, tariff.getVtcGrossTonnageThreshold());
      case PORTFLEET:
        return grossTonnageIsAboveThreshold(source, tariff.getPortfleetGrossTonnageThreshold());
      case LESPORT:
        return grossTonnageIsAboveThreshold(source, tariff.getLesportGrossTonnageThreshold());
      case ODESSOS:
        return grossTonnageIsAboveThreshold(source, tariff.getOdessosGrossTonnageThreshold());
      case BALCHIK:
        return grossTonnageIsAboveThreshold(source, tariff.getBalchikGrossTonnageThreshold());
    }

    return false;
  }

  private BigDecimal calculateAdditionalDue(final BigDecimal grossTonnageThreshold) {
    final BigDecimal additionalValue =
        getAdditionalDueValue(
            source.getPort().getMooringServiceProvider(), tariff.getMooringDuesByProvider());
    final BigDecimal multiplier = getMultiplier(grossTonnageThreshold);
    return additionalValue.multiply(multiplier);
  }

  private BigDecimal getMultiplier(final BigDecimal threshold) {
    final double a =
        (source.getShip().getGrossTonnage().doubleValue() - threshold.doubleValue()) / 1000;
    final double b = a - Math.floor(a) > 0 ? 1 : 0;

    return BigDecimal.valueOf((int) a + b);
  }
}
