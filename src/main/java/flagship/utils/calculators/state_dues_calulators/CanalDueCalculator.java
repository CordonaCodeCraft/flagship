package flagship.utils.calculators.state_dues_calulators;

import flagship.domain.entities.Case;
import flagship.domain.entities.enums.PortArea;
import flagship.domain.entities.enums.ShipType;
import flagship.utils.tariffs.CanalDueTariff;

import java.math.BigDecimal;

import static flagship.domain.entities.enums.ShipType.CONTAINER;

public class CanalDueCalculator extends StateDueCalculator<Case, CanalDueTariff> {

    @Override
    public BigDecimal calculate(Case source, CanalDueTariff tariff) {
        BigDecimal baseDue = calculateBaseDue(source, tariff);
        double discountCoefficient = evaluateDiscountCoefficient(source, tariff);
        return calculateDueTotal(baseDue, discountCoefficient);
    }

    @Override
    protected BigDecimal calculateBaseDue(Case source, CanalDueTariff tariff) {
        PortArea portArea = source.getPort().getArea();
        int grossTonnage = source.getShip().getGrossTonnage();
        double euroPerTon = tariff.getCanalDuesByPortArea().get(portArea);
        return BigDecimal.valueOf(grossTonnage * euroPerTon);
    }

    @Override
    protected double evaluateDiscountCoefficient(Case source, CanalDueTariff tariff) {

        PortArea portArea = source.getPort().getArea();
        ShipType shipType = source.getShip().getType();

        boolean satisfiesCallCountThreshold = source.getCallCount() >= tariff.getCallCountThreshold();

        double discountCoefficient = 0;

        if (tariff.getShipTypesNotEligibleForDiscount().contains(shipType)) {
            discountCoefficient = 0;
        } else {
            if (shipType == CONTAINER) {
                discountCoefficient = Math.max(discountCoefficient, tariff.getDiscountCoefficientsByPortAreaForContainers().get(portArea));
                if (satisfiesCallCountThreshold) {
                    discountCoefficient = Math.max(discountCoefficient, tariff.getDiscountCoefficientsByPortAreaPerCallCountForContainers().get(portArea));
                }
            } else {
                discountCoefficient = Math.max(discountCoefficient, tariff.getDiscountCoefficientByShipType().get(shipType));
                if (satisfiesCallCountThreshold) {
                    discountCoefficient = Math.max(discountCoefficient, tariff.getDefaultCallCountDiscountCoefficient());
                }
            }
        }

        return discountCoefficient;
    }
}
