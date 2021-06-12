package flagship.bootstrap.initializers;

import flagship.domain.calculation.tariffs.state.BoomContainmentTariff;
import flagship.domain.tuples.due.Due;
import flagship.domain.tuples.range.Range;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedHashMap;
import java.util.Map;

import static flagship.domain.calculation.tariffs.Tariff.MAX_GT;
import static flagship.domain.calculation.tariffs.Tariff.MIN_GT;

@Slf4j
public class BoomContainmentTariffInitializer extends Initializer {

  public static BoomContainmentTariff getTariff() {

    final Map<Range, Due> boomContainmentDuePerGrossTonnage = new LinkedHashMap<>();

    boomContainmentDuePerGrossTonnage.put(new Range(MIN_GT, 3000), new Due(700.00));
    boomContainmentDuePerGrossTonnage.put(new Range(3001, 5000), new Due(1000.00));
    boomContainmentDuePerGrossTonnage.put(new Range(5001, 10000), new Due(1500.00));
    boomContainmentDuePerGrossTonnage.put(new Range(10001, 20000), new Due(1800.00));
    boomContainmentDuePerGrossTonnage.put(new Range(20001, MAX_GT), new Due(2500.00));

    final BoomContainmentTariff boomContainmentTariff = new BoomContainmentTariff();

    boomContainmentTariff.setBoomContainmentDuePerGrossTonnage(
        withImmutableMap(boomContainmentDuePerGrossTonnage));

    log.info("Boom containment due tariff initialized");

    return boomContainmentTariff;
  }
}
