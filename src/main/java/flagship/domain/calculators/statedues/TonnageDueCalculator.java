package flagship.domain.calculators.statedues;

import flagship.domain.calculators.tariffs.stateduestariffs.TonnageDueTariff;
import flagship.domain.cases.dto.PdaCase;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;

import static flagship.domain.cases.entities.enums.ShipType.SPECIAL;

@NoArgsConstructor
public class TonnageDueCalculator extends StateDueCalculator<PdaCase, TonnageDueTariff> {

  @Override
  protected BigDecimal calculateBaseDue(final PdaCase source, final TonnageDueTariff tariff) {

    BigDecimal baseDue =
        source.getShip().getGrossTonnage().multiply(getTonnageDuePerTon(source, tariff));

    if (source.getShip().getType() == SPECIAL) {
      return calculateBaseDueForSpecial(source, baseDue);
    }
    return baseDue;
  }

  private BigDecimal calculateBaseDueForSpecial(final PdaCase source, final BigDecimal baseDue) {
    if (source.getEstimatedDateOfArrival().getMonthValue()
        != source.getEstimatedDateOfDeparture().getMonthValue()) {
      final long multiplier =
          ChronoUnit.MONTHS.between(
                  YearMonth.from(source.getEstimatedDateOfArrival()),
                  YearMonth.from(source.getEstimatedDateOfDeparture()))
              + 1;
      return baseDue.multiply(BigDecimal.valueOf(multiplier));
    } else {
      return baseDue;
    }
  }

  @Override
  protected BigDecimal evaluateDiscountCoefficient(
      final PdaCase source, final TonnageDueTariff tariff) {

    BigDecimal discountCoefficient =
        source.getArrivesFromBulgarianPort()
            ? tariff.getDiscountCoefficientForPortOfArrival()
            : BigDecimal.ZERO;

    if (isEligibleForDiscount(source, tariff)) {
      if (source.getCallCount() >= tariff.getCallCountThreshold()) {
        discountCoefficient = discountCoefficient.max(tariff.getCallCountDiscountCoefficient());
      }
      if (tariff.getDiscountCoefficientsByCallPurpose().containsKey(source.getCallPurpose())) {
        discountCoefficient =
            discountCoefficient.max(
                tariff.getDiscountCoefficientsByCallPurpose().get(source.getCallPurpose()));
      }
      if (tariff.getDiscountCoefficientsByShipType().containsKey(source.getShip().getType())) {
        discountCoefficient =
            discountCoefficient.max(
                tariff.getDiscountCoefficientsByShipType().get(source.getShip().getType()));
      }
    }

    return discountCoefficient;
  }

  private BigDecimal getTonnageDuePerTon(PdaCase source, TonnageDueTariff tariff) {
    if (dueIsDependantOnCallPurpose(source, tariff)) {
      return tariff.getTonnageDuesByCallPurpose().get(source.getCallPurpose());
    } else if (dueIsDependantOnShipType(source, tariff)) {
      return tariff.getTonnageDuesByShipType().get(source.getShip().getType());
    } else {
      return tariff.getTonnageDuesByPortArea().get(source.getPort().getArea());
    }
  }

  private boolean dueIsDependantOnShipType(final PdaCase source, final TonnageDueTariff tariff) {
    return tariff.getTonnageDuesByShipType().containsKey(source.getShip().getType());
  }

  private boolean dueIsDependantOnCallPurpose(final PdaCase source, final TonnageDueTariff tariff) {
    return tariff.getTonnageDuesByCallPurpose().containsKey(source.getCallPurpose());
  }

  private boolean isEligibleForDiscount(PdaCase source, TonnageDueTariff tariff) {
    return !tariff.getShipTypesNotEligibleForDiscount().contains(source.getShip().getType())
        && !tariff.getCallPurposesNotEligibleForDiscount().contains(source.getCallPurpose());
  }
}
