package flagship.domain.calculators.statedues;

import flagship.domain.cases.entities.Case;
import flagship.domain.cases.entities.enums.ShipType;
import flagship.domain.calculators.tariffs.stateduestariffs.LightDueTariff;

import java.math.BigDecimal;
import java.util.Map;

public class LightDueCalculator extends StateDueCalculator<Case, LightDueTariff> {

    // todo: Validate that gross tonnage is no less than 41, otherwise fixed due per year should be applied.
    @Override
    protected BigDecimal calculateDue(final Case source, final LightDueTariff tariff) {

        final ShipType shipType = source.getShip().getType();
        final int grossTonnage = source.getShip().getGrossTonnage();

        BigDecimal lightDue;

        if (tariff.getLightDuesPerTonByShipType().containsKey(shipType)) {
            lightDue = BigDecimal.valueOf(grossTonnage).multiply(tariff.getLightDuesPerTonByShipType().get(shipType));
        } else {
            lightDue = tariff.getLightDuesByGrossTonnage()
                    .entrySet()
                    .stream()
                    .filter(entry -> grossTonnage >= entry.getValue()[0] && grossTonnage <= entry.getValue()[1])
                    .findFirst()
                    .map(Map.Entry::getKey)
                    .orElse(tariff.getLightDueMaximumValue());
        }
        // todo: replace the magic number 150 with light due maximum value
        return lightDue.doubleValue() <= 150.0 ? lightDue : BigDecimal.valueOf(150);
    }

    @Override
    protected BigDecimal evaluateDiscountCoefficient(final Case source, final LightDueTariff tariff) {

        final int callCount = source.getCallCount();
        final ShipType shipType = source.getShip().getType();

        BigDecimal discountCoefficient = BigDecimal.ZERO;

        boolean isEligibleForDiscount = !tariff.getShipTypesNotEligibleForDiscount().contains(shipType);

        if (isEligibleForDiscount) {
            if (callCount >= tariff.getCallCountThreshold()) {
                discountCoefficient = discountCoefficient.max(tariff.getCallCountDiscountCoefficient());
            }

            if (tariff.getDiscountCoefficientsByShipType().containsKey(shipType)) {
                discountCoefficient = discountCoefficient.max(tariff.getDiscountCoefficientsByShipType().get(shipType));
            }
        }

        return discountCoefficient;
    }
}
