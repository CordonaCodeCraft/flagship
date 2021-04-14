package flagship.domain.utils.calculators.statedues;

import flagship.domain.cases.entities.Case;
import flagship.domain.cases.entities.enums.ShipType;
import flagship.domain.utils.tariffs.LightDueTariff;

import java.math.BigDecimal;
import java.util.Map;

public class LightDueCalculator extends StateDueCalculator<Case, LightDueTariff> {
    @Override
    public BigDecimal calculate(final Case source, final LightDueTariff tariff) {
        final BigDecimal baseDue = calculateDueTotal(source, tariff);
        final BigDecimal discountCoefficient = evaluateDiscountCoefficient(source, tariff);
        return calculateDueFinal(baseDue, discountCoefficient);
    }

    // todo: Validate that gross tonnage is no less than 41, otherwise fixed due per year should be
    // applied.
    @Override
    protected BigDecimal calculateDueTotal(final Case source, final LightDueTariff tariff) {

        final ShipType shipType = source.getShip().getType();
        final int grossTonnage = source.getShip().getGrossTonnage();

        BigDecimal lightDue;

        if (tariff.getLightDuesPerTonByShipType().containsKey(shipType)) {
            lightDue = BigDecimal.valueOf(grossTonnage).multiply(tariff.getLightDuesPerTonByShipType().get(shipType));
        } else {
            lightDue = tariff.getLightDuesByGrossTonnage()
                    .entrySet()
                    .stream()
                    .filter(entry -> grossTonnage >= entry.getKey().getValue0() && grossTonnage <= entry.getKey().getValue1())
                    .findFirst()
                    .map(Map.Entry::getValue)
                    .orElse(tariff.getLightDueMaximumValue());
        }

        return lightDue.doubleValue() <= 150.0 ? lightDue : BigDecimal.valueOf(150);

    }

    @Override
    protected BigDecimal evaluateDiscountCoefficient(final Case source, final LightDueTariff tariff) {

        final int callCount = source.getCallCount();
        final ShipType shipType = source.getShip().getType();

        BigDecimal discountCoefficient = BigDecimal.ZERO;

        if (callCount >= tariff.getCallCountThreshold()) {
            discountCoefficient = discountCoefficient.max(tariff.getCallCountDiscountCoefficient());
        }

        if (tariff.getDiscountCoefficientsByShipType().containsKey(shipType)) {
            discountCoefficient = discountCoefficient.max(tariff.getDiscountCoefficientsByShipType().get(shipType));
        }

        return discountCoefficient;
    }
}
