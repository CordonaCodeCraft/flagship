package flagship.domain.utils.calculators.statedues;

import flagship.domain.cases.entities.Case;
import flagship.domain.cases.entities.enums.ShipType;
import flagship.domain.utils.tariffs.LightDueTariff;

import java.math.BigDecimal;
import java.util.Map;

public class LightDueCalculator extends StateDueCalculator<Case, LightDueTariff> {
    @Override
    public BigDecimal calculate(Case source, LightDueTariff tariff) {
        BigDecimal baseDue = calculateBaseDue(source, tariff);
        double discountCoefficient = evaluateDiscountCoefficient(source, tariff);
        return calculateDueTotal(baseDue, discountCoefficient);
    }

    // todo: Validate that gross tonnage is no less than 41, otherwise fixed due per year should be
    // applied.
    @Override
    protected BigDecimal calculateBaseDue(final Case source, final LightDueTariff tariff) {

        final ShipType shipType = source.getShip().getType();
        final int grossTonnage = source.getShip().getGrossTonnage();

        BigDecimal lightDue;

        if (tariff.getLightDuesPerTonByShipType().containsKey(shipType)) {
            lightDue = BigDecimal.valueOf(grossTonnage * tariff.getLightDuesPerTonByShipType().get(shipType));
        } else {
            lightDue = BigDecimal.valueOf(tariff.getLightDuesByGrossTonnage()
                    .entrySet()
                    .stream()
                    .filter(entry -> grossTonnage >= entry.getKey().getValue0() && grossTonnage <= entry.getKey().getValue1())
                    .mapToDouble(Map.Entry::getValue)
                    .findFirst()
                    .orElse(tariff.getLightDueMaximumValue()));
        }

        return lightDue.doubleValue() <= 150.0 ? lightDue : BigDecimal.valueOf(150);

    }

    @Override
    protected double evaluateDiscountCoefficient(final Case source, final LightDueTariff tariff) {

        final int callCount = source.getCallCount();
        final ShipType shipType = source.getShip().getType();

        double discountCoefficient = 0;

        if (callCount >= tariff.getCallCountThreshold()) {
            discountCoefficient = Math.max(discountCoefficient, tariff.getCallCountDiscountCoefficient());
        }

        if (tariff.getDiscountCoefficientsByShipType().containsKey(shipType)) {
            discountCoefficient = Math.max(discountCoefficient, tariff.getDiscountCoefficientsByShipType().get(shipType));
        }

        return discountCoefficient;
    }
}
