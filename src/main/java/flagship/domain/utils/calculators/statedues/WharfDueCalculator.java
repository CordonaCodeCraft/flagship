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
        final BigDecimal discountCoefficient = evaluateDiscountCoefficient(source, tariff);
        return calculateDueTotal(wharfDue, discountCoefficient);
    }

    @Override
    protected BigDecimal calculateBaseDue(final Case source, final WharfDueTariff tariff) {

        final Map<ShipType, BigDecimal> wharfDuesByShipType = tariff.getWharfDuesByShipType();

        final ShipType shipType = source.getShip().getType();
        final double lengthOverall = Math.ceil(source.getShip().getLengthOverall());
        final BigDecimal wharfDuePerHour = wharfDuesByShipType.getOrDefault(shipType, tariff.getDefaultWharfDue());

        final BigDecimal wharfDuePerHourTotal = BigDecimal.valueOf(lengthOverall).multiply(wharfDuePerHour);
        final int alongsideHoursExpected = source.getAlongsideDaysExpected() * 24;

        return wharfDuePerHour.multiply(BigDecimal.valueOf(alongsideHoursExpected));
    }

    @Override
    protected BigDecimal evaluateDiscountCoefficient(final Case source, final WharfDueTariff tariff) {

        final Set<ShipType> shipTypesNotEligibleForDiscount = tariff.getShipTypesNotEligibleForDiscount();
        final Map<CallPurpose, BigDecimal> discountCoefficientsByCallPurpose = tariff.getDiscountCoefficientsByCallPurpose();

        BigDecimal discountCoefficient = BigDecimal.ZERO;

        if (shipTypesNotEligibleForDiscount.contains(source.getShip().getType())) {
            discountCoefficient = BigDecimal.ZERO;
        } else {
            if (discountCoefficientsByCallPurpose.containsKey(source.getCallPurpose())) {
                discountCoefficient = discountCoefficientsByCallPurpose.get(source.getCallPurpose());
            }
        }

        return discountCoefficient;
    }
}
