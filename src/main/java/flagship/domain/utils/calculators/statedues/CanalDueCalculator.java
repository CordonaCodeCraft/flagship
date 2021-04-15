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
        final BigDecimal baseDue = calculateDueTotal(source, tariff);
        final BigDecimal discountCoefficient = evaluateDiscountCoefficient(source, tariff);
        return calculateDueAfterDiscount(baseDue, discountCoefficient);
    }

    @Override
    protected BigDecimal calculateDueTotal(final Case source, final CanalDueTariff tariff) {
        final PortArea portArea = source.getPort().getArea();
        final int grossTonnage = source.getShip().getGrossTonnage();
        final BigDecimal euroPerTon = tariff.getCanalDuesByPortArea().get(portArea);
        return BigDecimal.valueOf(grossTonnage).multiply(euroPerTon);
    }

    @Override
    protected BigDecimal evaluateDiscountCoefficient(final Case source, final CanalDueTariff tariff) {

        final PortArea portArea = source.getPort().getArea();
        final ShipType shipType = source.getShip().getType();

        final boolean satisfiesCallCountThreshold = source.getCallCount() >= tariff.getCallCountThreshold();

        BigDecimal discountCoefficient = BigDecimal.ZERO;

        if (!tariff.getShipTypesNotEligibleForDiscount().contains(shipType)) {
            if (shipType == CONTAINER) {
                discountCoefficient = discountCoefficient.max(tariff.getDiscountCoefficientsByPortAreaForContainers().get(portArea));
                if (satisfiesCallCountThreshold) {
                    discountCoefficient = discountCoefficient.max(tariff.getDiscountCoefficientsByPortAreaPerCallCountForContainers().get(portArea));
                }
            } else {
                discountCoefficient = discountCoefficient.max(tariff.getDiscountCoefficientByShipType().get(shipType));
                if (satisfiesCallCountThreshold) {
                    discountCoefficient = discountCoefficient.max(tariff.getDefaultCallCountDiscountCoefficient());
                }
            }
        }

        return discountCoefficient;
    }
}
