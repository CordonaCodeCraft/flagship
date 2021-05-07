package flagship.domain.calculators.agencyduescalculators;

import flagship.domain.calculators.DueCalculator;
import flagship.domain.cases.dto.PdaCase;
import flagship.domain.tariffs.Due;
import flagship.domain.tariffs.GtRange;
import flagship.domain.tariffs.Tariff;
import flagship.domain.tariffs.agencyduestariffs.BasicAgencyDueTariff;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;

import static flagship.domain.cases.entities.enums.CallPurpose.*;

@NoArgsConstructor
public class BasicAgencyDueCalculator implements DueCalculator<PdaCase, Tariff> {

  private PdaCase source;
  private BasicAgencyDueTariff tariff;

  @Override
  public void set(final PdaCase source, final Tariff tariff) {
    this.source = source;
    this.tariff = (BasicAgencyDueTariff) tariff;
  }

  @Override
  public BigDecimal calculate() {

    BigDecimal basicAgencyDue = getBaseDue();

    if (grossTonnageExceedsThreshold()) {
      BigDecimal addition = getAddition();
      BigDecimal multiplier = getMultiplier();
      basicAgencyDue = basicAgencyDue.add(addition.multiply(multiplier));
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
            > tariff.getGrossTonnageThreshold().intValue();
  }

  private BigDecimal getAddition() {
    return tariff.getBasicAgencyDuePerGrossTonnage().values().stream()
            .map(Due::getAddition)
            .filter(Objects::nonNull)
            .findFirst()
            .get();
  }

  private BigDecimal getMultiplier() {
    final double a =
        (source.getShip().getGrossTonnage().doubleValue()
                - tariff.getGrossTonnageThreshold().doubleValue())
            / 1000;
    final double b = a - Math.floor(a) > 0 ? 1 : 0;

    return BigDecimal.valueOf((int) a + b);
  }

  private boolean shipGrossTonnageIsInRange(final Map.Entry<GtRange, Due> entry) {
    return source.getShip().getGrossTonnage().intValue() >= entry.getKey().getMin()
        && source.getShip().getGrossTonnage().intValue() <= entry.getKey().getMax();
  }

  private boolean callPurposeIsEligibleForDiscount() {
    return source.getCallPurpose() != LOADING
            && source.getCallPurpose() != UNLOADING
            && source.getCallPurpose() != LOADING_AND_UNLOADING;
  }

  private BigDecimal getDiscount(final BigDecimal basicAgencyDue) {
    return basicAgencyDue.multiply(tariff.getDiscountCoefficientForCallPurpose());
  }
}
