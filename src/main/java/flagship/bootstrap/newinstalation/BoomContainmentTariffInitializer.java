package flagship.bootstrap.newinstalation;

import flagship.domain.tariffs.mix.Due;
import flagship.domain.tariffs.mix.Range;
import flagship.domain.tariffs.statedues.BoomContainmentTariff;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import static flagship.domain.tariffs.Tariff.MAX_GT;
import static flagship.domain.tariffs.Tariff.MIN_GT;

@Component
@Slf4j
@RequiredArgsConstructor
public class BoomContainmentTariffInitializer {

  public static BoomContainmentTariff getTariff() {

    final BoomContainmentTariff boomContainmentTariff = new BoomContainmentTariff();

    final Map<Range, Due> boomContainmentDuePerGrossTonnage = new LinkedHashMap<>();

    boomContainmentDuePerGrossTonnage.put(new Range(MIN_GT, 3000), new Due(700.00));
    boomContainmentDuePerGrossTonnage.put(new Range(3001, 5000), new Due(1000.00));
    boomContainmentDuePerGrossTonnage.put(new Range(5001, 10000), new Due(1500.00));
    boomContainmentDuePerGrossTonnage.put(new Range(10001, 20000), new Due(1800.00));
    boomContainmentDuePerGrossTonnage.put(new Range(20001, MAX_GT), new Due(2500.00));

    boomContainmentTariff.setBoomContainmentDuePerGrossTonnage(
        Collections.unmodifiableMap(boomContainmentDuePerGrossTonnage));

    log.info("Boom containment due tariff initialized");

    return boomContainmentTariff;
  }
}
