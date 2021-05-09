package flagship.domain.calculators.agencyduescalculators;

import flagship.domain.calculators.DueCalculator;
import flagship.domain.cases.dto.PdaCase;
import flagship.domain.tariffs.Due;
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

  private BigDecimal getCarsDue() {
    return tariff.getCarsDueByGrossTonnageAndAlongsideDaysExpected().entrySet().stream()
        .filter(this::grossTonnageIsInRange)
        .map(this::getDue)
        .findFirst()
        .get();
  }

  private boolean grossTonnageIsInRange(final Map.Entry<Range, Map<Range, Due>> entry) {
    return source.getShip().getGrossTonnage().intValue() >= entry.getKey().getMin()
        && source.getShip().getGrossTonnage().intValue() <= entry.getKey().getMax();
  }

  private BigDecimal getDue(final Map.Entry<Range, Map<Range, Due>> entry) {
    return entry.getValue().entrySet().stream()
        .filter(this::daysAreInRange)
        .map(e -> e.getValue().getBase())
        .findFirst()
        .get();
  }

  private boolean daysAreInRange(final Map.Entry<Range, Due> entry) {
    return source.getAlongsideDaysExpected() >= entry.getKey().getMin()
        && source.getAlongsideDaysExpected() <= entry.getKey().getMax();
  }

  private BigDecimal getIncreaseCoefficient() {

    final PortName portName =
        Arrays.stream(PortName.values())
            .filter(e -> e.name.equals(source.getPort().getName()))
            .findFirst()
            .get();

    return tariff
        .getCarsDuesIncreaseCoefficientByPortName()
        .getOrDefault(portName, BigDecimal.ZERO);
  }
}
