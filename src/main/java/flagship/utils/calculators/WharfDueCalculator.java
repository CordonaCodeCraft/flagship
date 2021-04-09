package flagship.utils.calculators;

import flagship.domain.entities.Case;
import flagship.domain.entities.enums.CallPurpose;
import flagship.domain.entities.enums.ShipType;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static flagship.domain.entities.enums.CallPurpose.*;
import static flagship.domain.entities.enums.ShipType.MILITARY;

public class WharfDueCalculator {

    public static BigDecimal calculateWharfDue(Case activeCase) {

        double lengthOverall = Math.ceil(activeCase.getShip().getLengthOverall());
        ShipType shipType = activeCase.getShip().getType();

        double wharfDuePerHour = shipType == MILITARY ? 0.5 : 0.10;
        double wharfDuePerHourTotal = lengthOverall * wharfDuePerHour;
        int alongsideHoursExpected = activeCase.getAlongsideDaysExpected() * 24;

        BigDecimal wharfDueExpected = new BigDecimal(alongsideHoursExpected * wharfDuePerHourTotal);

        List<CallPurpose> callPurposesEligibleForDiscount = Arrays.asList(RESUPPLY, RECRUITMENT, POSTAL, REPAIR);
        List<ShipType> shipTypesNotEligibleForDiscount = Collections.singletonList(MILITARY);

        CallPurpose callPurpose = activeCase.getCallPurpose();

        boolean isEligibleForDiscount = callPurposesEligibleForDiscount.contains(callPurpose) && !shipTypesNotEligibleForDiscount.contains(shipType);

        if (isEligibleForDiscount) {
            BigDecimal discount = BigDecimal.valueOf(wharfDueExpected.doubleValue() * 0.5);
            wharfDueExpected = wharfDueExpected.subtract(discount);
        }

        return wharfDueExpected;
    }

}
