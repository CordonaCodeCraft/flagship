package flagship.domain.calculators.agencyduescalculators;

import flagship.domain.calculators.DueCalculator;
import flagship.domain.cases.dto.PdaCase;
import flagship.domain.tariffs.Due;
import flagship.domain.tariffs.Range;
import flagship.domain.tariffs.Tariff;
import flagship.domain.tariffs.agencyduestariffs.AgencyDuesTariff;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

import static flagship.domain.cases.entities.enums.CallPurpose.*;

@NoArgsConstructor
public class BasicAgencyDueCalculator implements DueCalculator<PdaCase, Tariff> {

  private PdaCase source;
  private AgencyDuesTariff tariff;

  @Override
  public void set(final PdaCase source, final Tariff tariff) {
    this.source = source;
    this.tariff = (AgencyDuesTariff) tariff;
  }

  @Override
  public BigDecimal calculate() {

    BigDecimal basicAgencyDue = getBaseDue();

    if (grossTonnageExceedsThreshold()) {
      basicAgencyDue = basicAgencyDue.add(getAddition().multiply(getMultiplier()));
    }

    if (callPurposeIsEligibleForDiscount()) {
      basicAgencyDue = basicAgencyDue.subtract(getDiscount(basicAgencyDue));
    }

    return basicAgencyDue;
  }

  private BigDecimal getBaseDue() {
    return tariff.getBasicAgencyDuePerGrossTonnage().entrySet().stream()
        .filter(this::shipGrossTonnageIsInRange)
        .map(e -> e.getValue().getBase())
        .findFirst()
        .get();
  }

  private boolean grossTonnageExceedsThreshold() {
    return source.getShip().getGrossTonnage().intValue()
        > tariff.getBasicAgencyDueGrossTonnageThreshold().intValue();
  }

  private BigDecimal getAddition() {
    return tariff.getBasicAgencyDuePerGrossTonnage().entrySet().stream()
        .filter(this::shipGrossTonnageIsInRange)
        .map(e -> e.getValue().getAddition())
        .findFirst()
        .get();
  }

  private BigDecimal getMultiplier() {
    final double a =
        (source.getShip().getGrossTonnage().doubleValue()
                - tariff.getBasicAgencyDueGrossTonnageThreshold().doubleValue())
            / 1000;
    final double b = a - Math.floor(a) > 0 ? 1 : 0;
    return BigDecimal.valueOf((int) a + b);
  }

  private boolean shipGrossTonnageIsInRange(final Map.Entry<Range, Due> entry) {
    return source.getShip().getGrossTonnage().intValue() >= entry.getKey().getMin()
        && source.getShip().getGrossTonnage().intValue() <= entry.getKey().getMax();
  }

  private boolean callPurposeIsEligibleForDiscount() {
    return source.getCallPurpose() != LOADING
        && source.getCallPurpose() != UNLOADING
        && source.getCallPurpose() != LOADING_AND_UNLOADING;
  }

  private BigDecimal getDiscount(final BigDecimal baseDue) {
    return baseDue.multiply(tariff.getBasicAgencyDueDiscountCoefficientByCallPurpose());
  }
}
