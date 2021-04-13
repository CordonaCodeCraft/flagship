package flagship.domain.utils.calculators.statedues;

import flagship.domain.cases.entities.Case;
import flagship.domain.cases.entities.enums.CallPurpose;
import flagship.domain.cases.entities.enums.ShipType;
import flagship.domain.utils.tariffs.WharfDueTariff;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

public class WharfDueCalculator extends StateDueCalculator<Case, WharfDueTariff> {

    @Override
    public BigDecimal calculate(final Case source, final WharfDueTariff tariff) {
        final BigDecimal wharfDue = calculateBaseDue(source, tariff);
        final double discountCoefficient = evaluateDiscountCoefficient(source, tariff);
        return calculateDueTotal(wharfDue, discountCoefficient);
    }

    @Override
    protected BigDecimal calculateBaseDue(final Case source, final WharfDueTariff tariff) {

        final Map<ShipType, Double> wharfDuesByShipType = tariff.getWharfDuesByShipType();

        final ShipType shipType = source.getShip().getType();
        final double lengthOverall = Math.ceil(source.getShip().getLengthOverall());
        final double wharfDuePerHour = wharfDuesByShipType.getOrDefault(shipType, tariff.getDefaultWharfDue());

        final double wharfDuePerHourTotal = lengthOverall * wharfDuePerHour;
        final int alongsideHoursExpected = source.getAlongsideDaysExpected() * 24;

        return BigDecimal.valueOf(wharfDuePerHourTotal * alongsideHoursExpected);
    }

    @Override
    protected double evaluateDiscountCoefficient(final Case source, final WharfDueTariff tariff) {

        final Set<ShipType> shipTypesNotEligibleForDiscount = tariff.getShipTypesNotEligibleForDiscount();
        final Map<CallPurpose, Double> discountCoefficientsByCallPurpose = tariff.getDiscountCoefficientsByCallPurpose();

        double discountCoefficient = 0;

        if (shipTypesNotEligibleForDiscount.contains(source.getShip().getType())) {
            discountCoefficient = 0;
        } else {
            if (discountCoefficientsByCallPurpose.containsKey(source.getCallPurpose())) {
                discountCoefficient = discountCoefficientsByCallPurpose.get(source.getCallPurpose());
            }
        }

        return discountCoefficient;
    }
}
