package flagship.utils.calculators;

import flagship.domain.entities.Case;
import flagship.domain.entities.enums.CallPurpose;
import flagship.domain.entities.enums.PortArea;
import flagship.domain.entities.enums.ShipType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.*;

import static flagship.domain.entities.enums.CallPurpose.*;
import static flagship.domain.entities.enums.PortArea.*;
import static flagship.domain.entities.enums.ShipType.*;

@Getter
@Setter
@NoArgsConstructor

//todo: Implement logic for multiplying the tonnageDue by month count if shipType is
// SPECIAL and the expected alongside days are greater than the remaining days of the month of arriving

public class TonnageDueCalculator {

    public static BigDecimal calculateTonnageDue(Case activeCase) {

        BigDecimal tonnageDue;

        boolean dueIsDependentOnShipType = evaluateDueDependencyOnShipType(activeCase);

        boolean DueIsDependantOnCallPurpose = evaluateDueDependencyOnCallPurpose(activeCase);

        if (dueIsDependentOnShipType) {
            tonnageDue = calculateTonnageDueByShipType(activeCase);
        } else if (DueIsDependantOnCallPurpose) {
            tonnageDue = calculateTonnageDueByCallPurpose(activeCase);
        } else {
            tonnageDue = calculateTonnageDueByPortArea(activeCase);
        }

        double discountCoefficient = evaluateDiscountCoefficient(activeCase);

        if (discountCoefficient > 0.0) {
            BigDecimal discount = new BigDecimal(tonnageDue.doubleValue() * discountCoefficient);
            tonnageDue = tonnageDue.subtract(discount);
        }

        return tonnageDue;
    }

    private static boolean evaluateDueDependencyOnShipType(Case activeCase) {

        List<ShipType> shipTypesAffectingTonnageDue = Arrays.asList(OIL_TANKER, RECREATIONAL, MILITARY, SPECIAL);

        ShipType shipType = activeCase.getShip().getType();

        return shipTypesAffectingTonnageDue.contains(shipType);
    }

    private static boolean evaluateDueDependencyOnCallPurpose(Case activeCase) {

        List<CallPurpose> callPurposesAffectingTonnageDue = Arrays.asList(SPECIAL_PURPOSE_PORT_VISIT);

        CallPurpose callPurpose = activeCase.getCallPurpose();

        return callPurposesAffectingTonnageDue.contains(callPurpose);
    }

    private static BigDecimal calculateTonnageDueByShipType(Case activeCase) {

        Map<ShipType, Double> tonnageDuesByShipType = new HashMap<>();

        tonnageDuesByShipType.put(OIL_TANKER, 0.50);
        tonnageDuesByShipType.put(RECREATIONAL, 0.10);
        tonnageDuesByShipType.put(MILITARY, 0.25);
        tonnageDuesByShipType.put(SPECIAL, 0.50);

        ShipType shipType = activeCase.getShip().getType();
        double euroPerTon = tonnageDuesByShipType.get(shipType);
        int grossTonnage = activeCase.getShip().getGrossTonnage();

        return new BigDecimal(grossTonnage * euroPerTon);
    }

    private static BigDecimal calculateTonnageDueByCallPurpose(Case activeCase) {

        Map<CallPurpose, Double> tonnageDuesByCallPurpose = new HashMap<>();

        tonnageDuesByCallPurpose.put(SPECIAL_PURPOSE_PORT_VISIT, 0.05);

        CallPurpose callPurpose = activeCase.getCallPurpose();
        double euroPerTon = tonnageDuesByCallPurpose.get(callPurpose);
        int grossTonnage = activeCase.getShip().getGrossTonnage();

        return new BigDecimal(grossTonnage * euroPerTon);
    }

    private static BigDecimal calculateTonnageDueByPortArea(Case activeCase) {

        Map<PortArea, Double> tonnageDuesByPortArea = new HashMap<>();

        tonnageDuesByPortArea.put(FIRST, 0.55);
        tonnageDuesByPortArea.put(SECOND, 0.40);
        tonnageDuesByPortArea.put(THIRD, 0.55);
        tonnageDuesByPortArea.put(FOURTH, 0.55);

        PortArea portArea = activeCase.getPort().getArea();
        double euroPerTon = tonnageDuesByPortArea.get(portArea);
        int grossTonnage = activeCase.getShip().getGrossTonnage();

        return new BigDecimal(grossTonnage * euroPerTon);
    }


    private static double evaluateDiscountCoefficient(Case activeCase) {

        List<ShipType> shipTypesNotEligibleForDiscount = Arrays.asList(RECREATIONAL, MILITARY, SPECIAL);
        List<CallPurpose> callPurposesNotEligibleForDiscount = Collections.singletonList(SPECIAL_PURPOSE_PORT_VISIT);
        List<CallPurpose> callPurposesEligibleForDiscount = Arrays.asList(RESUPPLY, RECRUITMENT, POSTAL, REPAIR);

        Map<ShipType, Double> discountCoefficientByShipType = new HashMap<>();

        discountCoefficientByShipType.put(REEFER, 0.6);
        discountCoefficientByShipType.put(CONTAINER, 0.6);
        discountCoefficientByShipType.put(PASSENGER, 0.4);

        ShipType shipType = activeCase.getShip().getType();
        CallPurpose callPurpose = activeCase.getCallPurpose();

        double discountCoefficient = 0;

        boolean isEligibleForDiscount = !shipTypesNotEligibleForDiscount.contains(shipType) && !callPurposesNotEligibleForDiscount.contains(callPurpose);

        if (isEligibleForDiscount) {

            if (activeCase.getCallCount() >= 4) {
                double callCountDiscountCoefficient = 0.7;
                discountCoefficient = Double.max(discountCoefficient, callCountDiscountCoefficient);
            }

            if (callPurposesEligibleForDiscount.contains(callPurpose)) {
                double callPurposeDiscountCoefficient = 0.65;
                discountCoefficient = Double.max(discountCoefficient, callPurposeDiscountCoefficient);
            }

            if (discountCoefficientByShipType.containsKey(shipType)) {
                double shipTypeDiscountCoefficient = discountCoefficientByShipType.get(shipType);
                discountCoefficient = Double.max(discountCoefficient, shipTypeDiscountCoefficient);
            }
        }

        return discountCoefficient;
    }

}
