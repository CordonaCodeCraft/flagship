package flagship.domain.calculators.agencyduescalculators;

import flagship.domain.calculators.DueCalculator;
import flagship.domain.cases.dto.PdaCase;
import flagship.domain.tariffs.PortName;
import flagship.domain.tariffs.Range;
import flagship.domain.tariffs.Tariff;
import flagship.domain.tariffs.agencyduestariffs.AgencyDuesTariff;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;

@NoArgsConstructor
public class CarsDueCalculator implements DueCalculator<PdaCase, Tariff> {

  private PdaCase source;
  private AgencyDuesTariff tariff;

  @Override
  public void set(final PdaCase source, final Tariff tariff) {
    this.source = source;
    this.tariff = (AgencyDuesTariff) tariff;
  }

  @Override
  public BigDecimal calculate() {

    BigDecimal carsDue = getCarsDue();
    final BigDecimal increaseCoefficient = getIncreaseCoefficient();

    if (increaseCoefficient.doubleValue() > 0) {
      carsDue = carsDue.add(carsDue.multiply(increaseCoefficient));
    }

    return carsDue;
  }

  private BigDecimal getIncreaseCoefficient() {
    final PortName portName =
        Arrays.stream(PortName.values())
            .filter(e -> e.name.equals(source.getPort().getName()))
            .findFirst()
            .get();
    return tariff.getCarsDuesIncreaseCoefficientByPortName().get(portName);
  }

  private BigDecimal getCarsDue() {
    return tariff.getCarsDueByGrossTonnageAndAlongsideDaysExpected().entrySet().stream()
        .filter(
            entry ->
                source.getShip().getGrossTonnage().intValue() >= entry.getKey().getMin()
                    && source.getShip().getGrossTonnage().intValue() <= entry.getKey().getMax())
        .map(this::getDue)
        .findFirst()
        .get();
  }

  private BigDecimal getDue(final Map.Entry<Range, Map<Range, BigDecimal>> entry) {
    return entry.getValue().entrySet().stream()
        .filter(
            e ->
                source.getAlongsideDaysExpected() >= e.getKey().getMin()
                    && source.getAlongsideDaysExpected() <= e.getKey().getMax())
        .map(Map.Entry::getValue)
        .findFirst()
        .get();
  }
}
