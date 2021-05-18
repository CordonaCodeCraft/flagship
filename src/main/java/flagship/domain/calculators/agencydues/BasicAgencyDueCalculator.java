package flagship.domain.calculators.agencydues;

import flagship.domain.calculators.PrivateDueCalculator;
import flagship.domain.cases.dto.PdaCase;
import flagship.domain.tariffs.Tariff;
import flagship.domain.tariffs.agencydues.AgencyDuesTariff;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

import static flagship.domain.cases.entities.enums.CallPurpose.*;

@NoArgsConstructor
public class BasicAgencyDueCalculator extends PrivateDueCalculator {

  private AgencyDuesTariff tariff;

  @Override
  public void set(final PdaCase source, final Tariff tariff) {
    super.source = source;
    this.tariff = (AgencyDuesTariff) tariff;
  }

  @Override
  public BigDecimal calculate() {

    BigDecimal basicAgencyDue = getBaseDue(tariff.getBasicAgencyDuePerGrossTonnage());

    if (grossTonnageExceedsThreshold(tariff.getBasicAgencyDueGrossTonnageThreshold())) {
      final BigDecimal addition = getAddition(tariff.getBasicAgencyDuePerGrossTonnage());
      final BigDecimal multiplier = getMultiplier(tariff.getBasicAgencyDueGrossTonnageThreshold());
      basicAgencyDue = basicAgencyDue.add(addition.multiply(multiplier));
    }

    if (callPurposeIsEligibleForDiscount()) {
      basicAgencyDue = basicAgencyDue.subtract(getDiscount(basicAgencyDue));
    }

    return basicAgencyDue;
  }

  private boolean callPurposeIsEligibleForDiscount() {
    return source.getCallPurpose() != LOADING
        && source.getCallPurpose() != UNLOADING
        && source.getCallPurpose() != LOADING_AND_UNLOADING;
  }

  private BigDecimal getDiscount(final BigDecimal basicAgencyDue) {
    return basicAgencyDue.multiply(tariff.getBasicAgencyDueDiscountCoefficientByCallPurpose());
  }
}
