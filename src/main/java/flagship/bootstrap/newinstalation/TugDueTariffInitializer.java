package flagship.bootstrap.newinstalation;

import flagship.domain.tariffs.mix.Due;
import flagship.domain.tariffs.mix.HolidayCalendar;
import flagship.domain.tariffs.mix.PortName;
import flagship.domain.tariffs.mix.Range;
import flagship.domain.tariffs.servicedues.TugDueTariff;
import flagship.domain.tariffs.servicedues.TugDueTariff.TugServiceProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

import static flagship.domain.PdaWarningsGenerator.WarningType;
import static flagship.domain.PdaWarningsGenerator.WarningType.HOLIDAY;
import static flagship.domain.tariffs.Tariff.MAX_GT;
import static flagship.domain.tariffs.Tariff.MIN_GT;
import static flagship.domain.tariffs.mix.PortName.*;
import static flagship.domain.tariffs.servicedues.TugDueTariff.TugArea;
import static flagship.domain.tariffs.servicedues.TugDueTariff.TugArea.*;
import static flagship.domain.tariffs.servicedues.TugDueTariff.TugServiceProvider.PORTFLEET;
import static flagship.domain.tariffs.servicedues.TugDueTariff.TugServiceProvider.VTC;

@Component
@Slf4j
@RequiredArgsConstructor
public class TugDueTariffInitializer {

  public static TugDueTariff getTariff(HolidayCalendar holidayCalendar) {

    final TugDueTariff tugDueTariff = new TugDueTariff();

    final Map<TugServiceProvider, Map<TugArea, Set<PortName>>> portNamesInTugAreas =
        new EnumMap<>(TugServiceProvider.class);

    final Map<TugArea, Set<PortName>> portNamesInVtcTugAreas = new EnumMap<>(TugArea.class);

    final Set<PortName> portNamesInVtcFirstTugArea =
        EnumSet.of(VARNA_EAST, PCHMV, ODESSOS_PBM, PETROL, LESPORT, TEC_POWER_STATION);

    final Set<PortName> portNamesInVtcSecondTugArea = EnumSet.of(VARNA_WEST, FERRY_COMPLEX);

    final Set<PortName> portNamesInVtcThirdTugArea =
        EnumSet.of(BULYARD, SRY_ODESSOS, MTG_DOLPHIN, TEREM_FA);

    final Set<PortName> portNamesInVtcFourthTugArea =
        EnumSet.of(SHIFTING_BULYARD, SHIFTING_SRY_ODESSOS, SHIFTING_MTG_DOLPHIN, SHIFTING_TEREM_FA);

    final Set<PortName> portNamesInVtcFifthTugArea = EnumSet.of(BALCHIK_PORT);

    portNamesInVtcTugAreas.put(VTC_FIRST, portNamesInVtcFirstTugArea);
    portNamesInVtcTugAreas.put(VTC_SECOND, portNamesInVtcSecondTugArea);
    portNamesInVtcTugAreas.put(VTC_THIRD, portNamesInVtcThirdTugArea);
    portNamesInVtcTugAreas.put(VTC_FOURTH, portNamesInVtcFourthTugArea);
    portNamesInVtcTugAreas.put(VTC_FIFTH, portNamesInVtcFifthTugArea);

    final Map<TugArea, Set<PortName>> portNamesInPortFleetTugAreas = new EnumMap<>(TugArea.class);

    final Set<PortName> portNamesInPortfleetFirstTugArea =
        EnumSet.of(VARNA_EAST, TEC_EZEROVO, PETROL, LESPORT, ODESSOS_PBM, PCHMV);

    final Set<PortName> portNamesInPortfleetSecondTugArea = EnumSet.of(VARNA_WEST, FERRY_COMPLEX);

    final Set<PortName> portNamesInPortfleetThirdTugArea =
        EnumSet.of(BULYARD, SRY_ODESSOS, MTG_DOLPHIN, TEREM_FA);

    final Set<PortName> portNamesInPortfleetFourthTugArea =
        EnumSet.of(SHIFTING_BULYARD, SHIFTING_SRY_ODESSOS, SHIFTING_MTG_DOLPHIN, SHIFTING_TEREM_FA);

    final Set<PortName> portNamesInPortfleetFifthTugArea = EnumSet.of(BALCHIK_PORT);

    portNamesInPortFleetTugAreas.put(PORTFLEET_FIRST, portNamesInPortfleetFirstTugArea);
    portNamesInPortFleetTugAreas.put(PORTFLEET_SECOND, portNamesInPortfleetSecondTugArea);
    portNamesInPortFleetTugAreas.put(PORTFLEET_THIRD, portNamesInPortfleetThirdTugArea);
    portNamesInPortFleetTugAreas.put(PORTFLEET_FOURTH, portNamesInPortfleetFourthTugArea);
    portNamesInPortFleetTugAreas.put(PORTFLEET_FIFTH, portNamesInPortfleetFifthTugArea);

    portNamesInTugAreas.put(VTC, portNamesInVtcTugAreas);
    portNamesInTugAreas.put(PORTFLEET, portNamesInPortFleetTugAreas);

    final Map<TugArea, Map<Range, Due>> tugDuesByArea = new EnumMap<>(TugArea.class);

    final Map<Range, Due> tugDuesInVtcFirstTugArea = new LinkedHashMap<>();

    tugDuesInVtcFirstTugArea.put(new Range(MIN_GT, 1000), new Due(300.00));
    tugDuesInVtcFirstTugArea.put(new Range(1001, 2000), new Due(410.00));
    tugDuesInVtcFirstTugArea.put(new Range(2001, 2499), new Due(520.00));
    tugDuesInVtcFirstTugArea.put(new Range(2500, 3000), new Due(820.00));
    tugDuesInVtcFirstTugArea.put(new Range(3001, 4000), new Due(1020.00));
    tugDuesInVtcFirstTugArea.put(new Range(4001, 5000), new Due(1220.00));
    tugDuesInVtcFirstTugArea.put(new Range(5001, 6000), new Due(1420.00));
    tugDuesInVtcFirstTugArea.put(new Range(6001, 7000), new Due(1620.00));
    tugDuesInVtcFirstTugArea.put(new Range(7001, 8000), new Due(1820.00));
    tugDuesInVtcFirstTugArea.put(new Range(8001, 9000), new Due(2020.00));
    tugDuesInVtcFirstTugArea.put(new Range(9001, 10000), new Due(2220.00));
    tugDuesInVtcFirstTugArea.put(new Range(10001, MAX_GT), new Due(2220.00, 55.00));

    final Map<Range, Due> tugDuesInVtcSecondTugArea = new LinkedHashMap<>();

    tugDuesInVtcSecondTugArea.put(new Range(MIN_GT, 1000), new Due(300.00));
    tugDuesInVtcSecondTugArea.put(new Range(1001, 2000), new Due(410.00));
    tugDuesInVtcSecondTugArea.put(new Range(2001, 2499), new Due(520.00));
    tugDuesInVtcSecondTugArea.put(new Range(2500, 3000), new Due(1060.00));
    tugDuesInVtcSecondTugArea.put(new Range(3001, 4000), new Due(1320.00));
    tugDuesInVtcSecondTugArea.put(new Range(4001, 5000), new Due(1580.00));
    tugDuesInVtcSecondTugArea.put(new Range(5001, 6000), new Due(1840.00));
    tugDuesInVtcSecondTugArea.put(new Range(6001, 7000), new Due(2100.00));
    tugDuesInVtcSecondTugArea.put(new Range(7001, 8000), new Due(2360.00));
    tugDuesInVtcSecondTugArea.put(new Range(8001, 9000), new Due(2620.00));
    tugDuesInVtcSecondTugArea.put(new Range(9001, 10000), new Due(2880.00));
    tugDuesInVtcSecondTugArea.put(new Range(10001, MAX_GT), new Due(2880.00, 65.00));

    final Map<Range, Due> tugDuesInVtcThirdTugArea = new LinkedHashMap<>();

    tugDuesInVtcThirdTugArea.put(new Range(MIN_GT, 1000), new Due(300.00));
    tugDuesInVtcThirdTugArea.put(new Range(1001, 2000), new Due(410.00));
    tugDuesInVtcThirdTugArea.put(new Range(2001, 2499), new Due(520.00));
    tugDuesInVtcThirdTugArea.put(new Range(2500, 3000), new Due(720.00));
    tugDuesInVtcThirdTugArea.put(new Range(3001, 4000), new Due(870.00));
    tugDuesInVtcThirdTugArea.put(new Range(4001, 5000), new Due(1020.00));
    tugDuesInVtcThirdTugArea.put(new Range(5001, 6000), new Due(1170.00));
    tugDuesInVtcThirdTugArea.put(new Range(6001, 7000), new Due(1320.00));
    tugDuesInVtcThirdTugArea.put(new Range(7001, 8000), new Due(1470.00));
    tugDuesInVtcThirdTugArea.put(new Range(8001, 9000), new Due(1620.00));
    tugDuesInVtcThirdTugArea.put(new Range(9001, 10000), new Due(1770.00));
    tugDuesInVtcThirdTugArea.put(new Range(10001, MAX_GT), new Due(1770.00, 50.00));

    final Map<Range, Due> tugDuesInVtcFourthTugArea = new LinkedHashMap<>();

    tugDuesInVtcFourthTugArea.put(new Range(MIN_GT, 1000), new Due(300.00));
    tugDuesInVtcFourthTugArea.put(new Range(1001, 2000), new Due(410.00));
    tugDuesInVtcFourthTugArea.put(new Range(2001, 2499), new Due(520.00));
    tugDuesInVtcFourthTugArea.put(new Range(2500, 3000), new Due(650.00));
    tugDuesInVtcFourthTugArea.put(new Range(3001, 4000), new Due(780.00));
    tugDuesInVtcFourthTugArea.put(new Range(4001, 5000), new Due(910.00));
    tugDuesInVtcFourthTugArea.put(new Range(5001, 6000), new Due(1040.00));
    tugDuesInVtcFourthTugArea.put(new Range(6001, 7000), new Due(1170.00));
    tugDuesInVtcFourthTugArea.put(new Range(7001, 8000), new Due(1300.00));
    tugDuesInVtcFourthTugArea.put(new Range(8001, 9000), new Due(1430.00));
    tugDuesInVtcFourthTugArea.put(new Range(9001, 10000), new Due(1560.00));
    tugDuesInVtcFourthTugArea.put(new Range(10001, MAX_GT), new Due(1560.00, 50.00));

    final Map<Range, Due> tugDuesInVtcFifthTugArea = new LinkedHashMap<>();

    tugDuesInVtcFifthTugArea.put(new Range(MIN_GT, 1000), new Due(430.00));
    tugDuesInVtcFifthTugArea.put(new Range(1001, 2000), new Due(560.00));
    tugDuesInVtcFifthTugArea.put(new Range(2001, 2499), new Due(690.00));
    tugDuesInVtcFifthTugArea.put(new Range(2500, 3000), new Due(1060.00));
    tugDuesInVtcFifthTugArea.put(new Range(3001, 4000), new Due(1320.00));
    tugDuesInVtcFifthTugArea.put(new Range(4001, 5000), new Due(1580.00));
    tugDuesInVtcFifthTugArea.put(new Range(5001, 6000), new Due(1840.00));
    tugDuesInVtcFifthTugArea.put(new Range(6001, 7000), new Due(2100.00));
    tugDuesInVtcFifthTugArea.put(new Range(7001, 8000), new Due(2360.00));
    tugDuesInVtcFifthTugArea.put(new Range(8001, 9000), new Due(2620.00));
    tugDuesInVtcFifthTugArea.put(new Range(9001, 10000), new Due(2880.00));
    tugDuesInVtcFifthTugArea.put(new Range(10001, MAX_GT), new Due(2880.00, 65.00));

    final Map<Range, Due> tugDuesInPfFirstTugArea = new LinkedHashMap<>();

    tugDuesInPfFirstTugArea.put(new Range(MIN_GT, 1000), new Due(300.00));
    tugDuesInPfFirstTugArea.put(new Range(1001, 2000), new Due(410.00));
    tugDuesInPfFirstTugArea.put(new Range(2001, 2499), new Due(520.00));
    tugDuesInPfFirstTugArea.put(new Range(2500, 3000), new Due(820.00));
    tugDuesInPfFirstTugArea.put(new Range(3001, 4000), new Due(1020.00));
    tugDuesInPfFirstTugArea.put(new Range(4001, 5000), new Due(1220.00));
    tugDuesInPfFirstTugArea.put(new Range(5001, 6000), new Due(1420.00));
    tugDuesInPfFirstTugArea.put(new Range(6001, 7000), new Due(1620.00));
    tugDuesInPfFirstTugArea.put(new Range(7001, 8000), new Due(1820.00));
    tugDuesInPfFirstTugArea.put(new Range(8001, 9000), new Due(2020.00));
    tugDuesInPfFirstTugArea.put(new Range(9001, 10000), new Due(2220.00));
    tugDuesInPfFirstTugArea.put(new Range(10001, MAX_GT), new Due(2220.00, 55.00));

    final Map<Range, Due> tugDuesInPfSecondTugArea = new LinkedHashMap<>();

    tugDuesInPfSecondTugArea.put(new Range(MIN_GT, 1000), new Due(300.00));
    tugDuesInPfSecondTugArea.put(new Range(1001, 2000), new Due(410.00));
    tugDuesInPfSecondTugArea.put(new Range(2001, 2499), new Due(520.00));
    tugDuesInPfSecondTugArea.put(new Range(2500, 3000), new Due(1060.00));
    tugDuesInPfSecondTugArea.put(new Range(3001, 4000), new Due(1320.00));
    tugDuesInPfSecondTugArea.put(new Range(4001, 5000), new Due(1580.00));
    tugDuesInPfSecondTugArea.put(new Range(5001, 6000), new Due(1840.00));
    tugDuesInPfSecondTugArea.put(new Range(6001, 7000), new Due(2100.00));
    tugDuesInPfSecondTugArea.put(new Range(7001, 8000), new Due(2360.00));
    tugDuesInPfSecondTugArea.put(new Range(8001, 9000), new Due(2620.00));
    tugDuesInPfSecondTugArea.put(new Range(9001, 10000), new Due(2880.00));
    tugDuesInPfSecondTugArea.put(new Range(10001, MAX_GT), new Due(2880.00, 65.00));

    final Map<Range, Due> tugDuesInPfThirdTugArea = new LinkedHashMap<>();

    tugDuesInPfThirdTugArea.put(new Range(MIN_GT, 1000), new Due(300.00));
    tugDuesInPfThirdTugArea.put(new Range(1001, 2000), new Due(410.00));
    tugDuesInPfThirdTugArea.put(new Range(2001, 2499), new Due(520.00));
    tugDuesInPfThirdTugArea.put(new Range(2500, 3000), new Due(720.00));
    tugDuesInPfThirdTugArea.put(new Range(3001, 4000), new Due(870.00));
    tugDuesInPfThirdTugArea.put(new Range(4001, 5000), new Due(1020.00));
    tugDuesInPfThirdTugArea.put(new Range(5001, 6000), new Due(1170.00));
    tugDuesInPfThirdTugArea.put(new Range(6001, 7000), new Due(1320.00));
    tugDuesInPfThirdTugArea.put(new Range(7001, 8000), new Due(1470.00));
    tugDuesInPfThirdTugArea.put(new Range(8001, 9000), new Due(1620.00));
    tugDuesInPfThirdTugArea.put(new Range(9001, 10000), new Due(1770.00));
    tugDuesInPfThirdTugArea.put(new Range(10001, MAX_GT), new Due(1770.00, 50.00));

    final Map<Range, Due> tugDuesInPfFourthTugArea = new LinkedHashMap<>();

    tugDuesInPfFourthTugArea.put(new Range(MIN_GT, 1000), new Due(300.00));
    tugDuesInPfFourthTugArea.put(new Range(1001, 2000), new Due(410.00));
    tugDuesInPfFourthTugArea.put(new Range(2001, 2499), new Due(520.00));
    tugDuesInPfFourthTugArea.put(new Range(2500, 3000), new Due(650.00));
    tugDuesInPfFourthTugArea.put(new Range(3001, 4000), new Due(780.00));
    tugDuesInPfFourthTugArea.put(new Range(4001, 5000), new Due(910.00));
    tugDuesInPfFourthTugArea.put(new Range(5001, 6000), new Due(1040.00));
    tugDuesInPfFourthTugArea.put(new Range(6001, 7000), new Due(1170.00));
    tugDuesInPfFourthTugArea.put(new Range(7001, 8000), new Due(1300.00));
    tugDuesInPfFourthTugArea.put(new Range(8001, 9000), new Due(1430.00));
    tugDuesInPfFourthTugArea.put(new Range(9001, 10000), new Due(1560.00));
    tugDuesInPfFourthTugArea.put(new Range(10001, MAX_GT), new Due(1560.00, 50.00));

    final Map<Range, Due> tugDuesInPfFifthTugArea = new LinkedHashMap<>();

    tugDuesInPfFifthTugArea.put(new Range(MIN_GT, 1000), new Due(430.00));
    tugDuesInPfFifthTugArea.put(new Range(1001, 2000), new Due(560.00));
    tugDuesInPfFifthTugArea.put(new Range(2001, 2499), new Due(690.00));
    tugDuesInPfFifthTugArea.put(new Range(2500, 3000), new Due(1060.00));
    tugDuesInPfFifthTugArea.put(new Range(3001, 4000), new Due(1320.00));
    tugDuesInPfFifthTugArea.put(new Range(4001, 5000), new Due(1580.00));
    tugDuesInPfFifthTugArea.put(new Range(5001, 6000), new Due(1840.00));
    tugDuesInPfFifthTugArea.put(new Range(6001, 7000), new Due(2100.00));
    tugDuesInPfFifthTugArea.put(new Range(7001, 8000), new Due(2360.00));
    tugDuesInPfFifthTugArea.put(new Range(8001, 9000), new Due(2620.00));
    tugDuesInPfFifthTugArea.put(new Range(9001, 10000), new Due(2880.00));
    tugDuesInPfFifthTugArea.put(new Range(10001, MAX_GT), new Due(2880.00, 65.00));

    tugDuesByArea.put(VTC_FIRST, tugDuesInVtcFirstTugArea);
    tugDuesByArea.put(VTC_SECOND, tugDuesInVtcSecondTugArea);
    tugDuesByArea.put(VTC_THIRD, tugDuesInVtcThirdTugArea);
    tugDuesByArea.put(VTC_FOURTH, tugDuesInVtcFourthTugArea);
    tugDuesByArea.put(VTC_FIFTH, tugDuesInVtcFifthTugArea);
    tugDuesByArea.put(PORTFLEET_FIRST, tugDuesInPfFirstTugArea);
    tugDuesByArea.put(PORTFLEET_SECOND, tugDuesInPfSecondTugArea);
    tugDuesByArea.put(PORTFLEET_THIRD, tugDuesInPfThirdTugArea);
    tugDuesByArea.put(PORTFLEET_FOURTH, tugDuesInPfFourthTugArea);
    tugDuesByArea.put(PORTFLEET_FIFTH, tugDuesInPfFifthTugArea);

    final Map<Range, BigDecimal> tugCountByGrossTonnage = new LinkedHashMap<>();

    tugCountByGrossTonnage.put(new Range(MIN_GT, 4499), BigDecimal.valueOf(1.00));
    tugCountByGrossTonnage.put(new Range(4500, 17999), BigDecimal.valueOf(2.00));
    tugCountByGrossTonnage.put(new Range(18000, MAX_GT), BigDecimal.valueOf(3.00));

    final Map<WarningType, BigDecimal> increaseCoefficientsByWarningType =
        new EnumMap<>(WarningType.class);
    increaseCoefficientsByWarningType.put(HOLIDAY, BigDecimal.valueOf(1.0));

    tugDueTariff.setPortNamesInTugAreas(Collections.unmodifiableMap(portNamesInTugAreas));
    tugDueTariff.setTugDuesByArea(Collections.unmodifiableMap(tugDuesByArea));
    tugDueTariff.setTugCountByGrossTonnage(Collections.unmodifiableMap(tugCountByGrossTonnage));
    tugDueTariff.setIncreaseCoefficientsByWarningType(
        Collections.unmodifiableMap(increaseCoefficientsByWarningType));

    tugDueTariff.setHolidayCalendar(holidayCalendar.getHolidayCalendar());
    tugDueTariff.setGrossTonnageThreshold(BigDecimal.valueOf(10000.00));
    tugDueTariff.setGrossTonnageThresholdForTugCountReduce(BigDecimal.valueOf(18000.00));

    log.info("Tug due tariff initialized");

    return tugDueTariff;
  }
}
