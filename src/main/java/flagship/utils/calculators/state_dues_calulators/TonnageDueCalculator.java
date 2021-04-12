package flagship.utils.calculators.state_dues_calulators;

import flagship.domain.entities.Case;
import flagship.domain.entities.enums.CallPurpose;
import flagship.domain.entities.enums.PortArea;
import flagship.domain.entities.enums.ShipType;
import flagship.utils.tariffs.TonnageDueTariff;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

@NoArgsConstructor
public class TonnageDueCalculator extends StateDueCalculator<Case, TonnageDueTariff> {

    @Override
    public BigDecimal calculate(Case source, TonnageDueTariff tariff) {
        BigDecimal baseDue = calculateBaseDue(source, tariff);
        double discountCoefficient = evaluateDiscountCoefficient(source, tariff);
        return calculateDueTotal(baseDue, discountCoefficient);
    }

    @Override
    protected BigDecimal calculateBaseDue(Case source, TonnageDueTariff tariff) {

        BigDecimal baseDue;

        boolean dueIsDependentOnShipType = evaluateDueDependencyOnShipType(source, tariff);
        boolean dueIsDependantOnCallPurpose = evaluateDueDependencyOnCallPurpose(source, tariff);
        //todo: consider refactoring to lambda or stream
        if (dueIsDependentOnShipType) {
            baseDue = calculateBaseDueByShipType(source, tariff);
        } else if (dueIsDependantOnCallPurpose) {
            baseDue = calculateBaseDueByCallPurpose(source, tariff);
        } else {
            baseDue = calculateBaseDueByPortArea(source, tariff);
        }

        return baseDue;
    }

    private boolean evaluateDueDependencyOnShipType(Case source, TonnageDueTariff tariff) {
        return tariff.getTonnageDuesByShipType().containsKey(source.getShip().getType());
    }

    private boolean evaluateDueDependencyOnCallPurpose(Case source, TonnageDueTariff tariff) {
        return tariff.getTonnageDuesByCallPurpose().containsKey(source.getCallPurpose());
    }

    private BigDecimal calculateBaseDueByShipType(Case source, TonnageDueTariff tariff) {
        ShipType shipType = source.getShip().getType();
        int grossTonnage = source.getShip().getGrossTonnage();
        double euroPerTon = tariff.getTonnageDuesByShipType().get(shipType);
        return BigDecimal.valueOf(grossTonnage * euroPerTon);
    }

    private BigDecimal calculateBaseDueByCallPurpose(Case source, TonnageDueTariff tariff) {
        CallPurpose callPurpose = source.getCallPurpose();
        int grossTonnage = source.getShip().getGrossTonnage();
        double euroPerTon = tariff.getTonnageDuesByCallPurpose().get(callPurpose);
        return BigDecimal.valueOf(grossTonnage * euroPerTon);
    }

    private BigDecimal calculateBaseDueByPortArea(Case source, TonnageDueTariff tariff) {
        PortArea portArea = source.getPort().getArea();
        int grossTonnage = source.getShip().getGrossTonnage();
        double euroPerTon = tariff.getTonnageDuesByPortArea().get(portArea);
        return BigDecimal.valueOf(grossTonnage * euroPerTon);
    }

    @Override
    protected double evaluateDiscountCoefficient(Case source, TonnageDueTariff tariff) {

        Map<ShipType, Double> discountCoefficientsByShipType = tariff.getDiscountCoefficientsByShipType();
        Map<CallPurpose, Double> discountCoefficientsByCallPurpose = tariff.getDiscountCoefficientsByCallPurpose();
        Set<ShipType> shipTypesNotEligibleForDiscount = tariff.getShipTypesNotEligibleForDiscount();
        Set<CallPurpose> callPurposesNotEligibleForDiscount = tariff.getCallPurposesNotEligibleForDiscount();

        ShipType shipType = source.getShip().getType();
        CallPurpose callPurpose = source.getCallPurpose();
        int callCount = source.getCallCount();

        double discountCoefficient = 0;

        boolean isNotEligibleForDiscount = shipTypesNotEligibleForDiscount.contains(shipType)
                || callPurposesNotEligibleForDiscount.contains(callPurpose);

        if (isNotEligibleForDiscount) {
            discountCoefficient = 0;
        } else {

            if (callCount >= tariff.getCallCountThreshold()) {
                discountCoefficient = Math.max(discountCoefficient, tariff.getCallCountDiscountCoefficient());
            }

            if (discountCoefficientsByCallPurpose.containsKey(callPurpose)) {
                discountCoefficient = Math.max(discountCoefficient, discountCoefficientsByCallPurpose.get(callPurpose));
            }

            if (discountCoefficientsByShipType.containsKey(shipType)) {
                discountCoefficient = Math.max(discountCoefficient, discountCoefficientsByShipType.get(shipType));
            }
        }

        return discountCoefficient;
    }

}
