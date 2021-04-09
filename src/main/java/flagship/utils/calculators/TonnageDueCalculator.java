package flagship.utils.calculators;

import flagship.domain.entities.Case;
import flagship.domain.entities.enums.CallPurpose;
import flagship.domain.entities.enums.PortArea;
import flagship.domain.entities.enums.ShipType;
import flagship.utils.tariffs.TonnageDueTariff;

import java.math.BigDecimal;
import java.util.*;

public class TonnageDueCalculator {

    public static BigDecimal calculateTonnageDue(Case activeCase, TonnageDueTariff tonnageDueTariff) {

        BigDecimal tonnageDue;

        boolean dueIsDependentOnShipType = evaluateDueDependencyOnShipType(activeCase, tonnageDueTariff);

        boolean DueIsDependantOnCallPurpose = evaluateDueDependencyOnCallPurpose(activeCase, tonnageDueTariff);

        if (dueIsDependentOnShipType) {
            tonnageDue = calculateTonnageDueByShipType(activeCase, tonnageDueTariff);
        } else if (DueIsDependantOnCallPurpose) {
            tonnageDue = calculateTonnageDueByCallPurpose(activeCase, tonnageDueTariff);
        } else {
            tonnageDue = calculateTonnageDueByPortArea(activeCase, tonnageDueTariff);
        }

        double discountCoefficient = evaluateDiscountCoefficient(activeCase, tonnageDueTariff);

        if (discountCoefficient > 0.0) {
            BigDecimal discount = BigDecimal.valueOf(tonnageDue.doubleValue() * discountCoefficient);
            tonnageDue = tonnageDue.subtract(discount);
        }

        return tonnageDue;
    }

    private static boolean evaluateDueDependencyOnShipType(Case activeCase, TonnageDueTariff tonnageDueTariff) {
        ShipType shipType = activeCase.getShip().getType();
        return tonnageDueTariff.getShipTypesAffectingTonnageDue().contains(shipType);
    }

    private static boolean evaluateDueDependencyOnCallPurpose(Case activeCase, TonnageDueTariff tonnageDueTariff) {
        CallPurpose callPurpose = activeCase.getCallPurpose();
        return tonnageDueTariff.getCallPurposesAffectingTonnageDue().contains(callPurpose);
    }

    private static BigDecimal calculateTonnageDueByShipType(Case activeCase, TonnageDueTariff tonnageDueTariff) {
        ShipType shipType = activeCase.getShip().getType();
        double euroPerTon = tonnageDueTariff.getTonnageDuesByShipType().get(shipType);
        int grossTonnage = activeCase.getShip().getGrossTonnage();
        return new BigDecimal(grossTonnage * euroPerTon);
    }

    private static BigDecimal calculateTonnageDueByCallPurpose(Case activeCase, TonnageDueTariff tonnageDueTariff) {
        CallPurpose callPurpose = activeCase.getCallPurpose();
        double euroPerTon = tonnageDueTariff.getTonnageDuesByCallPurpose().get(callPurpose);
        int grossTonnage = activeCase.getShip().getGrossTonnage();
        return new BigDecimal(grossTonnage * euroPerTon);
    }

    private static BigDecimal calculateTonnageDueByPortArea(Case activeCase, TonnageDueTariff tonnageDueTariff) {
        PortArea portArea = activeCase.getPort().getArea();
        double euroPerTon = tonnageDueTariff.getTonnageDuesByPortArea().get(portArea);
        int grossTonnage = activeCase.getShip().getGrossTonnage();
        return new BigDecimal(grossTonnage * euroPerTon);
    }


    private static double evaluateDiscountCoefficient(Case activeCase, TonnageDueTariff tonnageDueTariff) {

        List<ShipType> shipTypesNotEligibleForDiscount = tonnageDueTariff.getShipTypesNotEligibleForDiscount();
        List<CallPurpose> callPurposesNotEligibleForDiscount = tonnageDueTariff.getCallPurposesNotEligibleForDiscount();
        List<CallPurpose> callPurposesEligibleForDiscount = tonnageDueTariff.getCallPurposesEligibleForDiscount();

        Map<ShipType, Double> discountCoefficientsByShipType = tonnageDueTariff.getDiscountCoefficientsByShipType();

        ShipType shipType = activeCase.getShip().getType();
        CallPurpose callPurpose = activeCase.getCallPurpose();

        double discountCoefficient = 0;

        boolean isEligibleForDiscount = !shipTypesNotEligibleForDiscount.contains(shipType) && !callPurposesNotEligibleForDiscount.contains(callPurpose);

        if (isEligibleForDiscount) {

            if (activeCase.getCallCount() >= tonnageDueTariff.getCallCountThreshold()) {
                double callCountDiscountCoefficient = tonnageDueTariff.getCallCountDiscountCoefficient();
                discountCoefficient = Double.max(discountCoefficient, callCountDiscountCoefficient);
            }

            if (callPurposesEligibleForDiscount.contains(callPurpose)) {
                double callPurposeDiscountCoefficient = tonnageDueTariff.getCallPurposeDiscountCoefficient();
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
