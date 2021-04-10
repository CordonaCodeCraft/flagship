package flagship.utils.calculators;

import flagship.domain.entities.Case;
import flagship.domain.entities.enums.CallPurpose;
import flagship.domain.entities.enums.PortArea;
import flagship.domain.entities.enums.ShipType;
import flagship.utils.tariffs.TonnageDueTariff;

import java.math.BigDecimal;
import java.util.Map;

import static flagship.domain.entities.enums.CallPurpose.*;
import static flagship.domain.entities.enums.ShipType.OIL_TANKER;

public class TonnageDueCalculator {

    public static BigDecimal calculateTonnageDue(Case activeCase, TonnageDueTariff tariff) {

        BigDecimal tonnageDue;

        boolean dueIsDependentOnShipType = evaluateDueDependencyOnShipType(activeCase, tariff);

        boolean DueIsDependantOnCallPurpose = evaluateDueDependencyOnCallPurpose(activeCase, tariff);

        if (dueIsDependentOnShipType) {
            tonnageDue = calculateTonnageDueByShipType(activeCase, tariff);
        } else if (DueIsDependantOnCallPurpose) {
            tonnageDue = calculateTonnageDueByCallPurpose(activeCase, tariff);
        } else {
            tonnageDue = calculateTonnageDueByPortArea(activeCase, tariff);
        }

        double discountCoefficient = evaluateDiscountCoefficient(activeCase, tariff);

        if (discountCoefficient > 0.0) {
            BigDecimal discount = BigDecimal.valueOf(tonnageDue.doubleValue() * discountCoefficient);
            tonnageDue = tonnageDue.subtract(discount);
        }

        return tonnageDue;
    }

    private static boolean evaluateDueDependencyOnShipType(Case activeCase, TonnageDueTariff tariff) {
        ShipType shipType = activeCase.getShip().getType();
        return tariff.getTonnageDuesByShipType().containsKey(shipType);
    }

    private static boolean evaluateDueDependencyOnCallPurpose(Case activeCase, TonnageDueTariff tariff) {
        CallPurpose callPurpose = activeCase.getCallPurpose();
        return tariff.getTonnageDuesByCallPurpose().containsKey(callPurpose);
    }

    private static BigDecimal calculateTonnageDueByShipType(Case activeCase, TonnageDueTariff tariff) {
        ShipType shipType = activeCase.getShip().getType();
        int grossTonnage = activeCase.getShip().getGrossTonnage();
        double euroPerTon = tariff.getTonnageDuesByShipType().get(shipType);
        return new BigDecimal(grossTonnage * euroPerTon);
    }

    private static BigDecimal calculateTonnageDueByCallPurpose(Case activeCase, TonnageDueTariff tariff) {
        CallPurpose callPurpose = activeCase.getCallPurpose();
        int grossTonnage = activeCase.getShip().getGrossTonnage();
        double euroPerTon = tariff.getTonnageDuesByCallPurpose().get(callPurpose);
        return new BigDecimal(grossTonnage * euroPerTon);
    }

    private static BigDecimal calculateTonnageDueByPortArea(Case activeCase, TonnageDueTariff tariff) {
        PortArea portArea = activeCase.getPort().getArea();
        int grossTonnage = activeCase.getShip().getGrossTonnage();
        double euroPerTon = tariff.getTonnageDuesByPortArea().get(portArea);
        return new BigDecimal(grossTonnage * euroPerTon);
    }

    private static double evaluateDiscountCoefficient(Case activeCase, TonnageDueTariff tariff) {

        double discountCoefficient = 0;

        ShipType shipType = activeCase.getShip().getType();
        CallPurpose callPurpose = activeCase.getCallPurpose();
        int callCount = activeCase.getCallCount();

        Map<ShipType, Double> discountCoefficientsByShipType = tariff.getDiscountCoefficientsByShipType();
        Map<CallPurpose, Double> discountCoefficientsByCallPurpose = tariff.getDiscountCoefficientsByCallPurpose();

        boolean isEligibleForDiscount = (callCount >= tariff.getCallCountThreshold()
                || discountCoefficientsByCallPurpose.containsKey(callPurpose)
                || discountCoefficientsByShipType.containsKey(shipType))
                && callPurpose != SPECIAL_PURPOSE_PORT_VISIT
                &&
                (!tariff.getTonnageDuesByShipType().containsKey(shipType) || shipType == OIL_TANKER);

        if (isEligibleForDiscount) {

            if (activeCase.getCallCount() >= tariff.getCallCountThreshold()) {
                double callCountDiscountCoefficient = tariff.getCallCountDiscountCoefficient();
                discountCoefficient = Double.max(discountCoefficient, callCountDiscountCoefficient);
            }

            if (discountCoefficientsByCallPurpose.containsKey(callPurpose)) {
                double callPurposeDiscountCoefficient = discountCoefficientsByCallPurpose.get(callPurpose);
                discountCoefficient = Double.max(discountCoefficient, callPurposeDiscountCoefficient);
            }

            if (discountCoefficientsByShipType.containsKey(shipType)) {
                double shipTypeDiscountCoefficient = discountCoefficientsByShipType.get(shipType);
                discountCoefficient = Double.max(discountCoefficient, shipTypeDiscountCoefficient);
            }
        }

        return discountCoefficient;
    }

}
