package flagship.utils.calculators.state_dues_calulators;

import flagship.domain.entities.Case;
import flagship.domain.entities.enums.ShipType;
import flagship.utils.tariffs.LightDueTariff;

import java.math.BigDecimal;
import java.util.Map;

public class LightDueCalculator extends StateDueCalculator<Case, LightDueTariff> {
    @Override
    public BigDecimal calculate(Case source, LightDueTariff tariff) {
        BigDecimal baseDue = calculateBaseDue(source, tariff);
        double discountCoefficient = evaluateDiscountCoefficient(source, tariff);
        return calculateDueTotal(baseDue, discountCoefficient);
    }

    //todo: Validate that gross tonnage is no less than 41, otherwise fixed due per year should be applied.
    @Override
    protected BigDecimal calculateBaseDue(Case source, LightDueTariff tariff) {

        ShipType shipType = source.getShip().getType();
        int grossTonnage = source.getShip().getGrossTonnage();

        if (tariff.getLightDuesByShipType().containsKey(shipType)) {
            double baseDue = grossTonnage * tariff.getLightDuesByShipType().get(shipType);
            baseDue = baseDue <= tariff.getLightDueMaximumValue() ? baseDue : tariff.getLightDueMaximumValue();
            return BigDecimal.valueOf(baseDue);
        } else {
            double lightDuePerTon = tariff
                    .getLightDuesByGrossTonnage()
                    .entrySet()
                    .stream()
                    .filter(entry -> grossTonnage >= entry.getKey().getValue0() && grossTonnage <= entry.getKey().getValue1())
                    .mapToDouble(Map.Entry::getValue)
                    .findFirst()
                    .orElse(tariff.getLightDueMaximumValue());

            return BigDecimal.valueOf(grossTonnage * lightDuePerTon);
        }
    }

    @Override
    protected double evaluateDiscountCoefficient(Case source, LightDueTariff tariff) {

        double discountCoefficient = 0;

        int callCount = source.getCallCount();
        ShipType shipType = source.getShip().getType();

        if (callCount >= tariff.getCallCountThreshold()) {
            discountCoefficient = Math.max(discountCoefficient, tariff.getCallCountDiscountCoefficient());
        }

        if (tariff.getDiscountCoefficientsByShipType().containsKey(shipType)) {
            discountCoefficient = Math.max(discountCoefficient, tariff.getDiscountCoefficientsByShipType().get(shipType));
        }

        return discountCoefficient;
    }
}
