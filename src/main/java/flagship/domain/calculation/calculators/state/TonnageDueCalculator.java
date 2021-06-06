package flagship.domain.calculation.calculators.state;

import flagship.domain.calculation.calculators.StateDueCalculator;
import flagship.domain.calculation.tariffs.Tariff;
import flagship.domain.calculation.tariffs.state.TonnageDueTariff;
import flagship.domain.pda.model.PdaCase;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static flagship.domain.ship.entity.Ship.ShipType.WORK_SHIP;

@NoArgsConstructor
public class TonnageDueCalculator extends StateDueCalculator {

  private PdaCase source;
  private TonnageDueTariff tariff;

  @Override
  public void set(final PdaCase source, final Tariff tariff) {
    this.source = source;
    this.tariff = (TonnageDueTariff) tariff;
  }

  @Override
  public BigDecimal calculate() {
    return getDueAfterDiscount(getBaseDue(), getDiscountCoefficient());
  }

  @Override
  protected BigDecimal getBaseDue() {

    BigDecimal baseDue = source.getShip().getGrossTonnage().multiply(getTonnageDuePerTon());

    if (source.getShip().getType() == WORK_SHIP) {
      return calculateBaseDueForSpecialShip(baseDue);
    }
    return baseDue;
  }

  private BigDecimal getTonnageDuePerTon() {
    if (dueIsDependantOnCallPurpose()) {
      return tariff.getTonnageDuesByCallPurpose().get(source.getCallPurpose()).getBase();
    } else if (dueIsDependantOnShipType()) {
      return tariff.getTonnageDuesByShipType().get(source.getShip().getType()).getBase();
    } else {
      return tariff.getTonnageDuesByPortArea().get(source.getPort().getPortArea()).getBase();
    }
  }

  private boolean dueIsDependantOnCallPurpose() {
    return tariff.getTonnageDuesByCallPurpose().containsKey(source.getCallPurpose());
  }

  private boolean dueIsDependantOnShipType() {
    return tariff.getTonnageDuesByShipType().containsKey(source.getShip().getType());
  }

  private BigDecimal calculateBaseDueForSpecialShip(final BigDecimal baseDue) {

    if (expectedAlongsideDaysAreProvided()) {

      if (expectedAlongsideDaysExceedCurrentMonth()) {
        final long multiplier =
            ChronoUnit.MONTHS.between(
                    YearMonth.from(source.getEstimatedDateOfArrival()),
                    YearMonth.from(source.getEstimatedDateOfDeparture()))
                + 1;
        return baseDue.multiply(BigDecimal.valueOf(multiplier));
      }
    }
    return baseDue;
  }

  private boolean expectedAlongsideDaysAreProvided() {
    return Optional.ofNullable(source.getEstimatedDateOfArrival()).isPresent()
        && Optional.ofNullable(source.getEstimatedDateOfDeparture()).isPresent();
  }

  private boolean expectedAlongsideDaysExceedCurrentMonth() {
    return source.getEstimatedDateOfArrival().getMonthValue()
        != source.getEstimatedDateOfDeparture().getMonthValue();
  }

  @Override
  protected BigDecimal getDiscountCoefficient() {

    BigDecimal discountCoefficient =
        source.getArrivesFromBulgarianPort()
            ? tariff.getDiscountCoefficientForPortOfArrival()
            : BigDecimal.ZERO;

    if (isEligibleForDiscount()) {

      if (isEligibleForCallCountDiscount()) {
        discountCoefficient = discountCoefficient.max(tariff.getCallCountDiscountCoefficient());
      }
      if (isEligibleForCallPurposeDiscount()) {
        discountCoefficient =
            discountCoefficient.max(
                tariff.getDiscountCoefficientsByCallPurpose().get(source.getCallPurpose()));
      }
      if (isEligibleForShipTypeDiscount()) {
        discountCoefficient =
            discountCoefficient.max(
                tariff.getDiscountCoefficientsByShipType().get(source.getShip().getType()));
      }
    }

    return discountCoefficient;
  }

  private boolean isEligibleForDiscount() {
    return !tariff.getShipTypesNotEligibleForDiscount().contains(source.getShip().getType())
        && !tariff.getCallPurposesNotEligibleForDiscount().contains(source.getCallPurpose());
  }

  private boolean isEligibleForCallCountDiscount() {
    return source.getCallCount() >= tariff.getCallCountThreshold();
  }

  private boolean isEligibleForCallPurposeDiscount() {
    return tariff.getDiscountCoefficientsByCallPurpose().containsKey(source.getCallPurpose());
  }

  private boolean isEligibleForShipTypeDiscount() {
    return tariff.getDiscountCoefficientsByShipType().containsKey(source.getShip().getType());
  }
}
