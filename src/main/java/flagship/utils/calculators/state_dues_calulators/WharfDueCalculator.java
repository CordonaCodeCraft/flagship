package flagship.utils.calculators.state_dues_calulators;

import flagship.domain.entities.Case;
import flagship.domain.entities.enums.CallPurpose;
import flagship.domain.entities.enums.ShipType;
import flagship.utils.tariffs.WharfDueTariff;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

public class WharfDueCalculator extends StateDueCalculator<Case, WharfDueTariff> {

    @Override
    public BigDecimal calculate(Case source, WharfDueTariff tariff) {
        BigDecimal wharfDue = calculateBaseDue(source, tariff);
        double discountCoefficient = evaluateDiscountCoefficient(source, tariff);
        return calculateDueTotal(wharfDue,discountCoefficient);
    }

    @Override
    protected BigDecimal calculateBaseDue(Case source, WharfDueTariff tariff) {

        Map<ShipType, Double> wharfDuesByShipType = tariff.getWharfDuesByShipType();

        ShipType shipType = source.getShip().getType();
        double lengthOverall = Math.ceil(source.getShip().getLengthOverall());
        double wharfDuePerHour = wharfDuesByShipType.getOrDefault(shipType, tariff.getDefaultWharfDue());

        double wharfDuePerHourTotal = lengthOverall * wharfDuePerHour;
        int alongsideHoursExpected = source.getAlongsideDaysExpected() * 24;

        return BigDecimal.valueOf(wharfDuePerHourTotal * alongsideHoursExpected);
    }

    @Override
    protected double evaluateDiscountCoefficient(Case source, WharfDueTariff tariff) {

        double discountCoefficient = 0;

        Set<ShipType> shipTypesNotEligibleForDiscount = tariff.getShipTypesNotEligibleForDiscount();
        Map<CallPurpose, Double> discountCoefficientsByCallPurpose = tariff.getDiscountCoefficientsByCallPurpose();

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
