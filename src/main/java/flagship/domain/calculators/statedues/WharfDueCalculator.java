package flagship.domain.calculators.statedues;

import flagship.domain.cases.entities.Case;
import flagship.domain.cases.entities.enums.CallPurpose;
import flagship.domain.cases.entities.enums.ShipType;
import flagship.domain.calculators.tariffs.stateduestariffs.WharfDueTariff;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

public class WharfDueCalculator extends StateDueCalculator<Case, WharfDueTariff> {

    @Override
    protected BigDecimal calculateDue(final Case source, final WharfDueTariff tariff) {

        final Map<ShipType, BigDecimal> wharfDuesByShipType = tariff.getWharfDuesByShipType();

        final ShipType shipType = source.getShip().getType();
        final BigDecimal lengthOverall = BigDecimal.valueOf(Math.ceil(source.getShip().getLengthOverall()));
        final BigDecimal wharfDuePerMeter = wharfDuesByShipType.getOrDefault(shipType, tariff.getDefaultWharfDue());

        final BigDecimal wharfDuePerLengthOverall = lengthOverall.multiply(wharfDuePerMeter);
        final BigDecimal alongsideHoursExpected = BigDecimal.valueOf(source.getAlongsideDaysExpected() * 24);

        return alongsideHoursExpected.multiply(wharfDuePerLengthOverall);
    }

    @Override
    protected BigDecimal evaluateDiscountCoefficient(final Case source, final WharfDueTariff tariff) {

        final Set<ShipType> shipTypesNotEligibleForDiscount = tariff.getShipTypesNotEligibleForDiscount();
        final Map<CallPurpose, BigDecimal> discountCoefficientsByCallPurpose = tariff.getDiscountCoefficientsByCallPurpose();

        BigDecimal discountCoefficient = BigDecimal.ZERO;

        boolean shipTypeIsEligibleForDiscount = !shipTypesNotEligibleForDiscount.contains(source.getShip().getType());
        boolean callPurposeIsEligibleForDiscount = discountCoefficientsByCallPurpose.containsKey(source.getCallPurpose());

        if (shipTypeIsEligibleForDiscount && callPurposeIsEligibleForDiscount) {
            discountCoefficient = discountCoefficientsByCallPurpose.get(source.getCallPurpose());
        }
        return discountCoefficient;
    }
}
