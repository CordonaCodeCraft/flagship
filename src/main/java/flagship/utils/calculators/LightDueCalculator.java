package flagship.utils.calculators;

import flagship.domain.entities.Case;
import flagship.domain.entities.enums.ShipType;
import flagship.utils.tariffs.LightDueTariff;

import java.math.BigDecimal;
import java.util.Map;

public class LightDueCalculator {

    public static BigDecimal calculateLightDue(Case activeCase, LightDueTariff tariff) {

        BigDecimal lightDue;

        boolean dueIsDependantOnShipType = evaluateDueDependencyOnShipType(activeCase, tariff);

        if (dueIsDependantOnShipType) {
            lightDue = calculateLightDueByShipType(activeCase, tariff);
        } else {
            lightDue = calculateLightDueByGrossTonnage(activeCase, tariff);
        }

        double discountCoefficient = evaluateDiscountCoefficient(activeCase, tariff);

        if (discountCoefficient > 0) {
            BigDecimal discount = BigDecimal.valueOf(lightDue.doubleValue() * discountCoefficient);
            lightDue = lightDue.subtract(discount);
        }

        return lightDue;
    }

    private static boolean evaluateDueDependencyOnShipType(Case activeCase, LightDueTariff tariff) {
        return tariff.getLightDuesByShipType().containsKey(activeCase.getShip().getType());
    }

    private static BigDecimal calculateLightDueByShipType(Case activeCase, LightDueTariff tariff) {
        ShipType shipType = activeCase.getShip().getType();
        int grossTonnage = activeCase.getShip().getGrossTonnage();
        double lightDuePerTon = tariff.getLightDuesByShipType().get(shipType);
        return BigDecimal.valueOf(grossTonnage * lightDuePerTon);
    }

    //todo: Validate that gross tonnage is no less than 41, otherwise fixed due per year should be applied.

    private static BigDecimal calculateLightDueByGrossTonnage(Case activeCase, LightDueTariff tariff) {

        int grossTonnage = activeCase.getShip().getGrossTonnage();

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

    private static double evaluateDiscountCoefficient(Case activeCase, LightDueTariff tariff) {

        double discountCoefficient = 0;

        int callCount = activeCase.getCallCount();
        ShipType shipType = activeCase.getShip().getType();

        if (callCount >= tariff.getCallCountThreshold()) {
            discountCoefficient = Math.max(discountCoefficient, tariff.getCallCountDiscountCoefficient());
        }

        if (tariff.getDiscountCoefficientsByShipType().containsKey(shipType)) {
            discountCoefficient = Math.max(discountCoefficient, tariff.getDiscountCoefficientsByShipType().get(shipType));
        }

        return discountCoefficient;
    }
}
