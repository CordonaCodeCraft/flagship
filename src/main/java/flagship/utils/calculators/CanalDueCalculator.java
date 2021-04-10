package flagship.utils.calculators;

import flagship.domain.entities.Case;
import flagship.domain.entities.enums.PortArea;
import flagship.domain.entities.enums.ShipType;
import flagship.utils.tariffs.CanalDueTariff;

import java.math.BigDecimal;

import static flagship.domain.entities.enums.ShipType.CONTAINER;

public class CanalDueCalculator {

    public static BigDecimal calculateCanalDue(Case activeCase, CanalDueTariff tariff) {

        BigDecimal canalDue = calculateDue(activeCase, tariff);
        double discountCoefficient = evaluateDiscountCoefficient(activeCase, tariff);

        if (discountCoefficient > 0) {
            BigDecimal discount = BigDecimal.valueOf(canalDue.doubleValue() * discountCoefficient);
            return canalDue.subtract(discount);
        }

        return canalDue;
    }

    private static BigDecimal calculateDue(Case activeCase, CanalDueTariff tariff) {
        PortArea portArea = activeCase.getPort().getArea();
        int grossTonnage = activeCase.getShip().getGrossTonnage();
        double euroPerTon = tariff.getCanalDuesByPortArea().get(portArea);
        return BigDecimal.valueOf(grossTonnage * euroPerTon);
    }

    private static double evaluateDiscountCoefficient(Case activeCase, CanalDueTariff tariff) {

        double discountCoefficient = 0;

        ShipType shipType = activeCase.getShip().getType();
        boolean satisfiesCallCountThreshold = activeCase.getCallCount() >= tariff.getCallCountThreshold();
        boolean isEligibleForDiscount = satisfiesCallCountThreshold || tariff.getDiscountCoefficientByShipType().containsKey(shipType);

        if (isEligibleForDiscount && shipType != CONTAINER) {

            if (satisfiesCallCountThreshold) {
                discountCoefficient = Math.max(discountCoefficient, tariff.getDefaultCallCountDiscountCoefficient());
            }

            if (tariff.getDiscountCoefficientByShipType().containsKey(shipType)) {
                discountCoefficient = Math.max(discountCoefficient, tariff.getDiscountCoefficientByShipType().get(shipType));
            }
        }

        if (shipType == CONTAINER) {

            PortArea portArea = activeCase.getPort().getArea();
            discountCoefficient = Math.max(discountCoefficient, tariff.getDiscountCoefficientsByPortAreaForContainers().get(portArea));

            if (satisfiesCallCountThreshold) {
                discountCoefficient = Math.max(discountCoefficient, tariff.getDiscountCoefficientsByPortAreaPerCallCountForContainers().get(portArea));
            }
        }

        return discountCoefficient;
    }
}
