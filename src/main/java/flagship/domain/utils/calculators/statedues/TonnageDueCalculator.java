package flagship.domain.utils.calculators.statedues;

import flagship.domain.cases.entities.Case;
import flagship.domain.cases.entities.enums.CallPurpose;
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
        final BigDecimal dueTotal = calculateDueTotal(source, tariff);
        final BigDecimal discountCoefficient = evaluateDiscountCoefficient(source, tariff);
        return calculateDueFinal(dueTotal, discountCoefficient);
    }

    @Override
    protected BigDecimal calculateDueTotal(final Case source, final TonnageDueTariff tariff) {
        final BigDecimal grossTonnage = BigDecimal.valueOf(source.getShip().getGrossTonnage());
        final BigDecimal tonnageDuePerTon = evaluateTonnageDuePerTon(source, tariff);
        return grossTonnage.multiply(tonnageDuePerTon);
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


    private BigDecimal evaluateTonnageDuePerTon(Case source, TonnageDueTariff tariff) {

        final boolean dueIsDependentOnShipType = evaluateDueDependencyOnShipType(source, tariff);
        final boolean dueIsDependantOnCallPurpose = evaluateDueDependencyOnCallPurpose(source, tariff);

        if (dueIsDependantOnCallPurpose) {
            return tariff.getTonnageDuesByCallPurpose().get(source.getCallPurpose());
        } else if (dueIsDependentOnShipType) {
            return tariff.getTonnageDuesByShipType().get(source.getShip().getType());
        } else {
            return tariff.getTonnageDuesByPortArea().get(source.getPort().getArea());
        }

    }

    private boolean evaluateDueDependencyOnShipType(final Case source, final TonnageDueTariff tariff) {
        return tariff.getTonnageDuesByShipType().containsKey(source.getShip().getType());
    }

    private boolean evaluateDueDependencyOnCallPurpose(final Case source, final TonnageDueTariff tariff) {
        return tariff.getTonnageDuesByCallPurpose().containsKey(source.getCallPurpose());
    }


}
