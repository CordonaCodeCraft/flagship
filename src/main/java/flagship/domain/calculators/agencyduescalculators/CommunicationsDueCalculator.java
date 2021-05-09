package flagship.domain.calculators.agencyduescalculators;

import flagship.domain.calculators.PrivateDueCalculator;
import flagship.domain.cases.dto.PdaCase;
import flagship.domain.tariffs.Tariff;
import flagship.domain.tariffs.agencyduestariffs.AgencyDuesTariff;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
public class CommunicationsDueCalculator extends PrivateDueCalculator<PdaCase, Tariff> {

  private AgencyDuesTariff tariff;

  @Override
  public void set(final PdaCase source, final Tariff tariff) {
    super.source = source;
    this.tariff = (AgencyDuesTariff) tariff;
  }

  @Override
  public BigDecimal calculate() {

    BigDecimal communicationsDue = tariff.getBaseCommunicationsDue();
    final BigDecimal threshold = tariff.getCommunicationsDueGrossTonnageThreshold();

    if (grossTonnageExceedsThreshold(threshold)) {
      final BigDecimal addition = tariff.getCommunicationsAdditionalDue();
      final BigDecimal multiplier = getMultiplier(threshold);
      communicationsDue = communicationsDue.add(addition.multiply(multiplier));
    }

    return communicationsDue;
  }
}
