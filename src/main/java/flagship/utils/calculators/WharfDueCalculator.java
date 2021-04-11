package flagship.utils.calculators;

import flagship.domain.entities.Case;
import flagship.domain.entities.enums.CallPurpose;
import flagship.domain.entities.enums.ShipType;
import flagship.utils.tariffs.WharfDueTariff;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

public class WharfDueCalculator {

    public static BigDecimal calculateWharfDue(Case activeCase, WharfDueTariff tariff) {

        BigDecimal wharfDue = calculateDue(activeCase, tariff);

        Map<CallPurpose, Double> discountCoefficientsByCallPurpose = tariff.getDiscountCoefficientsByCallPurpose();
        Set<ShipType> shipTypesNotEligibleForDiscount = tariff.getShipTypesNotEligibleForDiscount();

        CallPurpose callPurpose = activeCase.getCallPurpose();

        boolean isEligibleForDiscount = discountCoefficientsByCallPurpose.containsKey(callPurpose) && !shipTypesNotEligibleForDiscount.contains(activeCase.getShip().getType());

        if (isEligibleForDiscount) {
            BigDecimal discount = BigDecimal.valueOf(wharfDue.doubleValue() * discountCoefficientsByCallPurpose.get(callPurpose));
            wharfDue = wharfDue.subtract(discount);
        }

        return wharfDue;
    }

    private static BigDecimal calculateDue(Case activeCase, WharfDueTariff tariff) {
        double lengthOverall = Math.ceil(activeCase.getShip().getLengthOverall());
        ShipType shipType = activeCase.getShip().getType();
        Map<ShipType, Double> wharfDuesByShipType = tariff.getWharfDuesByShipType();
        double wharfDuePerHour = wharfDuesByShipType.getOrDefault(shipType, tariff.getDefaultWharfDue());
        double wharfDuePerHourTotal = lengthOverall * wharfDuePerHour;
        int alongsideHoursExpected = activeCase.getAlongsideDaysExpected() * 24;
        return BigDecimal.valueOf(wharfDuePerHourTotal * alongsideHoursExpected);
    }

}
