package flagship.domain.calculators.statedues;

import flagship.domain.cases.entities.Case;
import flagship.domain.cases.entities.enums.PortArea;
import flagship.domain.cases.entities.enums.ShipType;
import flagship.domain.calculators.tariffs.stateduestariffs.CanalDueTariff;

import java.math.BigDecimal;

import static flagship.domain.cases.entities.enums.ShipType.CONTAINER;

public class CanalDueCalculator extends StateDueCalculator<Case, CanalDueTariff> {

    @Override
    protected BigDecimal calculateDue(final Case source, final CanalDueTariff tariff) {
        final PortArea portArea = source.getPort().getArea();
        final int grossTonnage = source.getShip().getGrossTonnage();
        final BigDecimal euroPerTon = tariff.getCanalDuesByPortArea().get(portArea);
        return BigDecimal.valueOf(grossTonnage).multiply(euroPerTon);
    }

    @Override
    protected BigDecimal evaluateDiscountCoefficient(final Case source, final CanalDueTariff tariff) {

        final PortArea portArea = source.getPort().getArea();
        final ShipType shipType = source.getShip().getType();

        final boolean isEligibleForDiscount = !tariff.getShipTypesNotEligibleForDiscount().contains(shipType);
        final boolean satisfiesCallCountThreshold = source.getCallCount() >= tariff.getCallCountThreshold();

        BigDecimal discountCoefficient = BigDecimal.ZERO;

        if (isEligibleForDiscount) {
            if (shipType == CONTAINER) {
                if (satisfiesCallCountThreshold) {
                    discountCoefficient = discountCoefficient.max(tariff.getDiscountCoefficientsByPortAreaPerCallCountForContainers().get(portArea));
                }
                discountCoefficient = discountCoefficient.max(tariff.getDiscountCoefficientsByPortAreaForContainers().get(portArea));
            } else {
                if (satisfiesCallCountThreshold) {
                    discountCoefficient = discountCoefficient.max(tariff.getDefaultCallCountDiscountCoefficient());
                }

                if (tariff.getDiscountCoefficientByShipType().containsKey(shipType)) {
                    discountCoefficient = discountCoefficient.max(tariff.getDiscountCoefficientByShipType().get(shipType));
                }
            }
        }

        return discountCoefficient;
    }
}
