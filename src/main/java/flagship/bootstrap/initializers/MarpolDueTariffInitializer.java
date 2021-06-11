package flagship.bootstrap.initializers;

import flagship.domain.base.due.tuple.Due;
import flagship.domain.base.range.tuple.Range;
import flagship.domain.calculation.tariffs.state.MarpolDueTariff;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import static flagship.domain.calculation.tariffs.Tariff.MAX_GT;
import static flagship.domain.calculation.tariffs.Tariff.MIN_GT;

@Component
@Slf4j
@RequiredArgsConstructor
public class MarpolDueTariffInitializer {

  public static MarpolDueTariff getTariff() {

    final MarpolDueTariff marpolDueTariff = new MarpolDueTariff();

    final Map<Range, Due> freeSewageDisposalQuantitiesPerGT = new LinkedHashMap<>();

    freeSewageDisposalQuantitiesPerGT.put(new Range(MIN_GT, 2000), new Due(8.6508));
    freeSewageDisposalQuantitiesPerGT.put(new Range(2001, 3000), new Due(10.5314));
    freeSewageDisposalQuantitiesPerGT.put(new Range(3001, 6000), new Due(10.5315));
    freeSewageDisposalQuantitiesPerGT.put(new Range(6001, 10000), new Due(15.0448));
    freeSewageDisposalQuantitiesPerGT.put(new Range(10001, 20000), new Due(19.5583));
    freeSewageDisposalQuantitiesPerGT.put(new Range(20001, 30000), new Due(21.0628));
    freeSewageDisposalQuantitiesPerGT.put(new Range(30001, 40000), new Due(27.0807));
    freeSewageDisposalQuantitiesPerGT.put(new Range(40001, 50000), new Due(28.5852));
    freeSewageDisposalQuantitiesPerGT.put(new Range(50001, MAX_GT), new Due(30.0897));

    final Map<Range, Due> freeGarbageDisposalQuantitiesPerGT = new LinkedHashMap<>();

    freeGarbageDisposalQuantitiesPerGT.put(new Range(MIN_GT, 2000), new Due(10.72));
    freeGarbageDisposalQuantitiesPerGT.put(new Range(2001, 3000), new Due(11.43));
    freeGarbageDisposalQuantitiesPerGT.put(new Range(3001, 6000), new Due(12.86));
    freeGarbageDisposalQuantitiesPerGT.put(new Range(6001, 10000), new Due(23.58));
    freeGarbageDisposalQuantitiesPerGT.put(new Range(10001, 20000), new Due(26.43));
    freeGarbageDisposalQuantitiesPerGT.put(new Range(20001, 30000), new Due(32.15));
    freeGarbageDisposalQuantitiesPerGT.put(new Range(30001, 40000), new Due(50.01));
    freeGarbageDisposalQuantitiesPerGT.put(new Range(40001, 50000), new Due(71.45));
    freeGarbageDisposalQuantitiesPerGT.put(new Range(50001, MAX_GT), new Due(107.17));

    final Map<Range, Due[]> marpolDuePerGrossTonnage = new LinkedHashMap<>();

    marpolDuePerGrossTonnage.put(
        new Range(MIN_GT, 2000), new Due[] {new Due(35.00), new Due(5.00), new Due(25.00)});
    marpolDuePerGrossTonnage.put(
        new Range(2001, 3000), new Due[] {new Due(100.00), new Due(10.00), new Due(50.00)});
    marpolDuePerGrossTonnage.put(
        new Range(3001, 6000), new Due[] {new Due(130.00), new Due(15.00), new Due(65.00)});
    marpolDuePerGrossTonnage.put(
        new Range(6001, 10000), new Due[] {new Due(200.00), new Due(20.00), new Due(85.00)});
    marpolDuePerGrossTonnage.put(
        new Range(10001, 20000), new Due[] {new Due(220.00), new Due(25.00), new Due(120.00)});
    marpolDuePerGrossTonnage.put(
        new Range(20001, 30000), new Due[] {new Due(250.00), new Due(30.00), new Due(180.00)});
    marpolDuePerGrossTonnage.put(
        new Range(30001, 40000), new Due[] {new Due(450.00), new Due(35.00), new Due(250.00)});
    marpolDuePerGrossTonnage.put(
        new Range(40001, 50000), new Due[] {new Due(700.00), new Due(40.00), new Due(400.00)});
    marpolDuePerGrossTonnage.put(
        new Range(50001, MAX_GT), new Due[] {new Due(900.00), new Due(50.00), new Due(550.00)});

    marpolDueTariff.setFreeSewageDisposalQuantitiesPerGrossTonnage(
        Collections.unmodifiableMap(freeSewageDisposalQuantitiesPerGT));
    marpolDueTariff.setFreeGarbageDisposalQuantitiesPerGrossTonnage(
        Collections.unmodifiableMap(freeGarbageDisposalQuantitiesPerGT));
    marpolDueTariff.setMarpolDuePerGrossTonnage(
        Collections.unmodifiableMap(marpolDuePerGrossTonnage));

    marpolDueTariff.setOdessosFixedMarpolDue(BigDecimal.valueOf(120.00));
    marpolDueTariff.setOdessosFreeGarbageDisposalQuantity(BigDecimal.valueOf(10.00));
    marpolDueTariff.setOdessosFreeSewageDisposalQuantity(BigDecimal.valueOf(1.00));

    log.info("Marpol due tariff initialized");

    return marpolDueTariff;
  }
}
