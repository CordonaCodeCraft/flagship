package flagship.domain.utils.calculators.statedues;

import flagship.domain.cases.entities.Case;
import flagship.domain.cases.entities.enums.PortArea;
import flagship.domain.cases.entities.enums.ShipType;
import flagship.domain.utils.tariffs.CanalDueTariff;

import java.math.BigDecimal;

import static flagship.domain.cases.entities.enums.ShipType.CONTAINER;

public class CanalDueCalculator extends StateDueCalculator<Case, CanalDueTariff> {

    @Override
    public BigDecimal calculate(final Case source, final CanalDueTariff tariff) {
        final BigDecimal baseDue = calculateBaseDue(source, tariff);
        final double discountCoefficient = evaluateDiscountCoefficient(source, tariff);
        return calculateDueTotal(baseDue, discountCoefficient);
    }

    @Override
    protected BigDecimal calculateBaseDue(final Case source, final CanalDueTariff tariff) {
        final PortArea portArea = source.getPort().getArea();
        final int grossTonnage = source.getShip().getGrossTonnage();
        final double euroPerTon = tariff.getCanalDuesByPortArea().get(portArea);
        return BigDecimal.valueOf(grossTonnage * euroPerTon);
    }

    @Override
    protected double evaluateDiscountCoefficient(final Case source, final CanalDueTariff tariff) {

        final PortArea portArea = source.getPort().getArea();
        final ShipType shipType = source.getShip().getType();

        final boolean satisfiesCallCountThreshold = source.getCallCount() >= tariff.getCallCountThreshold();

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
