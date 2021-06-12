package flagship.domain.calculation.calculators.service;

import flagship.domain.calculation.calculators.PrivateDueCalculator;
import flagship.domain.calculation.tariffs.Tariff;
import flagship.domain.calculation.tariffs.service.MooringDueTariff;
import flagship.domain.caze.model.PdaCase;
import flagship.domain.caze.model.createrequest.resolvers.MooringServiceProviderResolver.MooringServiceProvider;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

import static flagship.domain.caze.model.createrequest.resolvers.MooringServiceProviderResolver.MooringServiceProvider.*;

@NoArgsConstructor
public class MooringDueCalculator extends PrivateDueCalculator {

  private MooringDueTariff tariff;

  @Override
  public void set(final PdaCase source, final Tariff tariff) {
    super.source = source;
    this.tariff = (MooringDueTariff) tariff;
  }

  @Override
  public BigDecimal calculate() {

    final BigDecimal mooringDue;

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
        break;
      case PCHMV:
        mooringDue = new PchvmMooringDueCalculator().calculatePchvmMooringDue();
        break;
      default:
        mooringDue = BigDecimal.ZERO;
    }

    return mooringDue;
  }

  private BigDecimal commonMooringDueCalculation(
      final MooringServiceProvider provider, final BigDecimal grossTonnageThreshold) {

    BigDecimal baseDue = getBaseDue(tariff.getMooringDuesByProvider().get(provider));

    if (grossTonnageExceedsThreshold()) {
      final BigDecimal addition = getAddition(tariff.getMooringDuesByProvider().get(provider));
      final BigDecimal multiplier = getMultiplier(grossTonnageThreshold);
      baseDue = baseDue.add(addition.multiply(multiplier));
    }
    return baseDue.multiply(BigDecimal.valueOf(2.00));
  }

  private boolean grossTonnageExceedsThreshold() {
    switch (source.getPort().getMooringServiceProvider()) {
      case VTC:
        return grossTonnageExceedsThreshold(tariff.getVtcGrossTonnageThreshold());
      case PORTFLEET:
        return grossTonnageExceedsThreshold(tariff.getPortfleetGrossTonnageThreshold());
      case LESPORT:
        return grossTonnageExceedsThreshold(tariff.getLesportGrossTonnageThreshold());
      case PCHMV:
        return grossTonnageExceedsThreshold(tariff.getPchvmGrossTonnageThreshold());
      default:
        return false;
    }
  }

  private class VtcMooringDueCalculator {

    private BigDecimal calculateVtcMooringDue() {
      return commonMooringDueCalculation(VTC, tariff.getVtcGrossTonnageThreshold());
    }
  }

  private class PortfleetMooringDueCalculator {

    private BigDecimal calculatePortfleetMooringDue() {
      return commonMooringDueCalculation(PORTFLEET, tariff.getPortfleetGrossTonnageThreshold());
    }
  }

  private class LesportMooringDueCalculator {

    private BigDecimal calculateLesportMooringDue() {
      return commonMooringDueCalculation(LESPORT, tariff.getLesportGrossTonnageThreshold());
    }
  }

  private class PchvmMooringDueCalculator {
    public BigDecimal calculatePchvmMooringDue() {
      return commonMooringDueCalculation(PCHMV, tariff.getPchvmGrossTonnageThreshold());
    }
  }

  private class OdessosMooringDueCalculator {

    public BigDecimal calculateOdessosMooringDue() {
      return getBaseDue(tariff.getMooringDuesByProvider().get(ODESSOS));
    }
  }

  private class BalchikMooringDueCalculator {

    public BigDecimal calculateBalchikMooringDue() {
      return getBaseDue(tariff.getMooringDuesByProvider().get(BALCHIK))
          .multiply(BigDecimal.valueOf(2.00));
    }
  }
}
