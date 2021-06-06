package flagship.domain.calculation.calculators.agency;

import flagship.domain.calculation.calculators.PrivateDueCalculator;
import flagship.domain.calculation.tariffs.Tariff;
import flagship.domain.calculation.tariffs.agency.AgencyDuesTariff;
import flagship.domain.pda.model.PdaCase;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
public class CommunicationsDueCalculator extends PrivateDueCalculator {

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
