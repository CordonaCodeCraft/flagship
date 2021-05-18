package flagship.bootstrap.newinstalation;

import flagship.domain.tariffs.mix.Due;
import flagship.domain.tariffs.mix.HolidayCalendar;
import flagship.domain.tariffs.mix.Range;
import flagship.domain.tariffs.servicedues.MooringDueTariff;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static flagship.domain.PdaWarningsGenerator.WarningType;
import static flagship.domain.PdaWarningsGenerator.WarningType.HOLIDAY;
import static flagship.domain.tariffs.Tariff.MAX_GT;
import static flagship.domain.tariffs.Tariff.MIN_GT;
import static flagship.domain.tariffs.servicedues.MooringDueTariff.MooringServiceProvider;

@Component
@Slf4j
@RequiredArgsConstructor
public class MooringDueTariffInitializer {

  private static final MooringDueTariff mooringDueTariff = new MooringDueTariff();

  public static MooringDueTariff getTariff(HolidayCalendar holidayCalendar) {

    final Map<MooringServiceProvider, Map<Range, Due>> mooringDuesByProvider =
        new EnumMap<>(MooringServiceProvider.class);

    final Map<Range, Due> lesportMooringDues = new LinkedHashMap<>();

    lesportMooringDues.put(new Range(MIN_GT, 3000), new Due(100.00));
    lesportMooringDues.put(new Range(3001, 6000), new Due(130.00));
    lesportMooringDues.put(new Range(6001, 8000), new Due(160.00));
    lesportMooringDues.put(new Range(8001, 10000), new Due(190.00));
    lesportMooringDues.put(new Range(10001, MAX_GT), new Due(190.00, 40.00));

    final Map<Range, Due> odessosMooringDues = new LinkedHashMap<>();

    odessosMooringDues.put(new Range(MIN_GT, 1000), new Due(120.00));
    odessosMooringDues.put(new Range(1001, 3000), new Due(160.00));
    odessosMooringDues.put(new Range(3001, 5000), new Due(240.00));
    odessosMooringDues.put(new Range(5001, MAX_GT), new Due(300.00));

    final Map<Range, Due> balchikMooringDues = new LinkedHashMap<>();

    balchikMooringDues.put(new Range(MIN_GT, 3000), new Due(80.00));
    balchikMooringDues.put(new Range(3001, 6000), new Due(110.00));
    balchikMooringDues.put(new Range(6001, 8000), new Due(130.00));
    balchikMooringDues.put(new Range(8001, 10000), new Due(160.00));
    balchikMooringDues.put(new Range(10001, MAX_GT), new Due(160.00));

    final Map<Range, Due> pchvmMooringDues = new LinkedHashMap<>();

    pchvmMooringDues.put(new Range(MIN_GT, 1000), new Due(50.00));
    pchvmMooringDues.put(new Range(1001, 2000), new Due(70.00));
    pchvmMooringDues.put(new Range(2001, 3000), new Due(80.00));
    pchvmMooringDues.put(new Range(3001, 4000), new Due(100.00));
    pchvmMooringDues.put(new Range(4001, 5000), new Due(110.00));
    pchvmMooringDues.put(new Range(5001, 6000), new Due(120.00));
    pchvmMooringDues.put(new Range(6001, 8000), new Due(130.00));
    pchvmMooringDues.put(new Range(8001, 10000), new Due(150.00));
    pchvmMooringDues.put(new Range(10001, MAX_GT), new Due(150.00, 15.00));

    final Map<Range, Due> vtcMooringDues = new LinkedHashMap<>();

    vtcMooringDues.put(new Range(MIN_GT, 1000), new Due(60.00));
    vtcMooringDues.put(new Range(1001, 2000), new Due(90.00));
    vtcMooringDues.put(new Range(2001, 3000), new Due(120.00));
    vtcMooringDues.put(new Range(3001, 4000), new Due(140.00));
    vtcMooringDues.put(new Range(4001, 5000), new Due(160.00));
    vtcMooringDues.put(new Range(5001, 6000), new Due(180.00));
    vtcMooringDues.put(new Range(6001, 7000), new Due(200.00));
    vtcMooringDues.put(new Range(7001, 8000), new Due(220.00));
    vtcMooringDues.put(new Range(8001, 9000), new Due(230.00));
    vtcMooringDues.put(new Range(9001, 10000), new Due(240.00));
    vtcMooringDues.put(new Range(10001, MAX_GT), new Due(240.00, 35.00));

    final Map<Range, Due> portfleetMooringDues = new LinkedHashMap<>();

    portfleetMooringDues.put(new Range(MIN_GT, 1000), new Due(60.00));
    portfleetMooringDues.put(new Range(1001, 2000), new Due(90.00));
    portfleetMooringDues.put(new Range(2001, 3000), new Due(120.00));
    portfleetMooringDues.put(new Range(3001, 4000), new Due(140.00));
    portfleetMooringDues.put(new Range(4001, 5000), new Due(160.00));
    portfleetMooringDues.put(new Range(5001, 6000), new Due(180.00));
    portfleetMooringDues.put(new Range(6001, 7000), new Due(200.00));
    portfleetMooringDues.put(new Range(7001, 8000), new Due(220.00));
    portfleetMooringDues.put(new Range(8001, 9000), new Due(230.00));
    portfleetMooringDues.put(new Range(9001, 10000), new Due(240.00));
    portfleetMooringDues.put(new Range(10001, MAX_GT), new Due(240.00, 35.00));

    mooringDuesByProvider.put(MooringServiceProvider.LESPORT, lesportMooringDues);
    mooringDuesByProvider.put(MooringServiceProvider.ODESSOS, odessosMooringDues);
    mooringDuesByProvider.put(MooringServiceProvider.BALCHIK, balchikMooringDues);
    mooringDuesByProvider.put(MooringServiceProvider.PCHMV, pchvmMooringDues);
    mooringDuesByProvider.put(MooringServiceProvider.VTC, vtcMooringDues);
    mooringDuesByProvider.put(MooringServiceProvider.PORTFLEET, portfleetMooringDues);

    final Map<WarningType, BigDecimal> increaseCoefficientsByWarningType =
        new EnumMap<>(WarningType.class);

    increaseCoefficientsByWarningType.put(HOLIDAY, BigDecimal.valueOf(1.0));

    mooringDueTariff.setMooringDuesByProvider(Collections.unmodifiableMap(mooringDuesByProvider));
    mooringDueTariff.setHolidayCalendar(holidayCalendar.getHolidayCalendar());
    mooringDueTariff.setIncreaseCoefficientsByWarningType(
        Collections.unmodifiableMap(increaseCoefficientsByWarningType));
    mooringDueTariff.setLesportGrossTonnageThreshold(BigDecimal.valueOf(10000.00));
    mooringDueTariff.setBalchikGrossTonnageThreshold(BigDecimal.valueOf(10000.00));
    mooringDueTariff.setPchvmGrossTonnageThreshold(BigDecimal.valueOf(10000.00));
    mooringDueTariff.setVtcGrossTonnageThreshold(BigDecimal.valueOf(10000.00));
    mooringDueTariff.setPortfleetGrossTonnageThreshold(BigDecimal.valueOf(10000.00));
    mooringDueTariff.setOdessosGrossTonnageThreshold(BigDecimal.valueOf(5000.00));

    log.info("Mooring due tariff initialized");

    return mooringDueTariff;
  }
}
