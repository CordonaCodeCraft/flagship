package flagship.domain.utils.calculators.statedues;

import flagship.domain.cases.entities.Case;
import flagship.domain.cases.entities.enums.CallPurpose;
import flagship.domain.cases.entities.enums.PortArea;
import flagship.domain.cases.entities.enums.ShipType;
import flagship.domain.utils.tariffs.TonnageDueTariff;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

@NoArgsConstructor
public class TonnageDueCalculator extends StateDueCalculator<Case, TonnageDueTariff> {

    @Override
    public BigDecimal calculate(Case source, TonnageDueTariff tariff) {
        final BigDecimal baseDue = calculateBaseDue(source, tariff);
        final double discountCoefficient = evaluateDiscountCoefficient(source, tariff);
        return calculateDueTotal(baseDue, discountCoefficient);
    }

    @Override
    protected BigDecimal calculateBaseDue(final Case source, final TonnageDueTariff tariff) {

        final boolean dueIsDependentOnShipType = evaluateDueDependencyOnShipType(source, tariff);
        final boolean dueIsDependantOnCallPurpose = evaluateDueDependencyOnCallPurpose(source, tariff);

        BigDecimal baseDue;

        // todo: consider refactoring to lambda or stream

        if (dueIsDependentOnShipType) {
            baseDue = calculateBaseDueByShipType(source, tariff);
        } else if (dueIsDependantOnCallPurpose) {
            baseDue = calculateBaseDueByCallPurpose(source, tariff);
        } else {
            baseDue = calculateBaseDueByPortArea(source, tariff);
        }

        return baseDue;
    }

    private boolean evaluateDueDependencyOnShipType(final Case source, final TonnageDueTariff tariff) {
        return tariff.getTonnageDuesByShipType().containsKey(source.getShip().getType());
    }

    private boolean evaluateDueDependencyOnCallPurpose(final Case source, final TonnageDueTariff tariff) {
        return tariff.getTonnageDuesByCallPurpose().containsKey(source.getCallPurpose());
    }

    private BigDecimal calculateBaseDueByShipType(final Case source, final TonnageDueTariff tariff) {
        final ShipType shipType = source.getShip().getType();
        final int grossTonnage = source.getShip().getGrossTonnage();
        final double euroPerTon = tariff.getTonnageDuesByShipType().get(shipType);
        return BigDecimal.valueOf(grossTonnage * euroPerTon);
    }

    private BigDecimal calculateBaseDueByCallPurpose(final Case source, final TonnageDueTariff tariff) {
        final CallPurpose callPurpose = source.getCallPurpose();
        final int grossTonnage = source.getShip().getGrossTonnage();
        final double euroPerTon = tariff.getTonnageDuesByCallPurpose().get(callPurpose);
        return BigDecimal.valueOf(grossTonnage * euroPerTon);
    }

    private BigDecimal calculateBaseDueByPortArea(final Case source, final TonnageDueTariff tariff) {
        final PortArea portArea = source.getPort().getArea();
        final int grossTonnage = source.getShip().getGrossTonnage();
        final double euroPerTon = tariff.getTonnageDuesByPortArea().get(portArea);
        return BigDecimal.valueOf(grossTonnage * euroPerTon);
    }

    @Override
    protected double evaluateDiscountCoefficient(final Case source, final TonnageDueTariff tariff) {

        final Map<ShipType, Double> discountCoefficientsByShipType = tariff.getDiscountCoefficientsByShipType();
        final Map<CallPurpose, Double> discountCoefficientsByCallPurpose = tariff.getDiscountCoefficientsByCallPurpose();
        final Set<ShipType> shipTypesNotEligibleForDiscount = tariff.getShipTypesNotEligibleForDiscount();
        final Set<CallPurpose> callPurposesNotEligibleForDiscount = tariff.getCallPurposesNotEligibleForDiscount();

        final ShipType shipType = source.getShip().getType();
        final CallPurpose callPurpose = source.getCallPurpose();
        final int callCount = source.getCallCount();

        final boolean isNotEligibleForDiscount = shipTypesNotEligibleForDiscount.contains(shipType)
                || callPurposesNotEligibleForDiscount.contains(callPurpose);

        double discountCoefficient = 0;

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
