package flagship.domain.calculators.agencyduescalculators;

import flagship.domain.calculators.DueCalculator;
import flagship.domain.cases.dto.PdaCase;
import flagship.domain.tariffs.Tariff;
import flagship.domain.tariffs.agencyduestariffs.AgencyDuesTariff;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
public class CommunicationsDueCalculator implements DueCalculator<PdaCase, Tariff> {

  private PdaCase source;
  private AgencyDuesTariff tariff;

  @Override
  public void set(final PdaCase source, final Tariff tariff) {
    this.source = source;
    this.tariff = (AgencyDuesTariff) tariff;
  }

  @Override
  public BigDecimal calculate() {

    BigDecimal communicationsDue = tariff.getBaseCommunicationsDue();

    if (grossTonnageExceedsThreshold()) {
      communicationsDue =
          communicationsDue.add(tariff.getCommunicationsAdditionalDue().multiply(getMultiplier()));
    }

    return communicationsDue;
  }

  private boolean grossTonnageExceedsThreshold() {
    return source.getShip().getGrossTonnage().intValue()
        > tariff.getCommunicationsDueGrossTonnageThreshold().intValue();
  }

  private BigDecimal getMultiplier() {
    final double a =
        (source.getShip().getGrossTonnage().doubleValue()
                - tariff.getCommunicationsDueGrossTonnageThreshold().doubleValue())
            / 1000;
    final double b = a - Math.floor(a) > 0 ? 1 : 0;
    return BigDecimal.valueOf((int) a + b);
  }
}
