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
        final BigDecimal discountCoefficient = evaluateDiscountCoefficient(source, tariff);
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
        final BigDecimal euroPerTon = tariff.getTonnageDuesByShipType().get(shipType);
        return BigDecimal.valueOf(grossTonnage).multiply(euroPerTon);
    }

    private BigDecimal calculateBaseDueByCallPurpose(final Case source, final TonnageDueTariff tariff) {
        final CallPurpose callPurpose = source.getCallPurpose();
        final int grossTonnage = source.getShip().getGrossTonnage();
        final BigDecimal euroPerTon = tariff.getTonnageDuesByCallPurpose().get(callPurpose);
        return BigDecimal.valueOf(grossTonnage).multiply(euroPerTon);
    }

    private BigDecimal calculateBaseDueByPortArea(final Case source, final TonnageDueTariff tariff) {
        final PortArea portArea = source.getPort().getArea();
        final int grossTonnage = source.getShip().getGrossTonnage();
        final BigDecimal euroPerTon = tariff.getTonnageDuesByPortArea().get(portArea);
        return BigDecimal.valueOf(grossTonnage).multiply(euroPerTon);
    }

    @Override
    protected BigDecimal evaluateDiscountCoefficient(final Case source, final TonnageDueTariff tariff) {

        final Map<ShipType, BigDecimal> discountCoefficientsByShipType = tariff.getDiscountCoefficientsByShipType();
        final Map<CallPurpose, BigDecimal> discountCoefficientsByCallPurpose = tariff.getDiscountCoefficientsByCallPurpose();
        final Set<ShipType> shipTypesNotEligibleForDiscount = tariff.getShipTypesNotEligibleForDiscount();
        final Set<CallPurpose> callPurposesNotEligibleForDiscount = tariff.getCallPurposesNotEligibleForDiscount();

        final ShipType shipType = source.getShip().getType();
        final CallPurpose callPurpose = source.getCallPurpose();
        final int callCount = source.getCallCount();

        final boolean isNotEligibleForDiscount = shipTypesNotEligibleForDiscount.contains(shipType)
                || callPurposesNotEligibleForDiscount.contains(callPurpose);

        BigDecimal discountCoefficient = BigDecimal.ZERO;

        if (isNotEligibleForDiscount) {
            discountCoefficient = BigDecimal.ZERO;
        } else {

            if (callCount >= tariff.getCallCountThreshold()) {
                discountCoefficient = discountCoefficient.max(tariff.getCallCountDiscountCoefficient());
            }

            if (discountCoefficientsByCallPurpose.containsKey(callPurpose)) {
                discountCoefficient = discountCoefficient.max(discountCoefficientsByCallPurpose.get(callPurpose));
            }

            if (discountCoefficientsByShipType.containsKey(shipType)) {
                discountCoefficient = discountCoefficient.max(discountCoefficientsByShipType.get(shipType));
            }
        }

        return discountCoefficient;
    }
}
