package flagship.bootstrap;

import flagship.domain.resolvers.HolidayCalendarResolver;
import flagship.domain.tariffs.Due;
import flagship.domain.tariffs.HolidayCalendar;
import flagship.domain.tariffs.PortName;
import flagship.domain.tariffs.Range;
import flagship.domain.tariffs.serviceduestariffs.MooringDueTariff;
import flagship.domain.tariffs.serviceduestariffs.PilotageDueTariff;
import flagship.domain.tariffs.serviceduestariffs.PilotageDueTariff.PilotageArea;
import flagship.domain.tariffs.serviceduestariffs.TugDueTariff;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static flagship.domain.tariffs.PdaWarningsGenerator.PdaWarning;
import static flagship.domain.tariffs.PdaWarningsGenerator.PdaWarning.*;
import static flagship.domain.tariffs.Tariff.MAX_GT;
import static flagship.domain.tariffs.Tariff.MIN_GT;
import static flagship.domain.tariffs.serviceduestariffs.MooringDueTariff.MooringServiceProvider;
import static flagship.domain.tariffs.serviceduestariffs.PilotageDueTariff.PilotageArea.*;
import static flagship.domain.tariffs.serviceduestariffs.TugDueTariff.TugArea;
import static flagship.domain.tariffs.serviceduestariffs.TugDueTariff.TugArea.*;
import static flagship.domain.tariffs.serviceduestariffs.TugDueTariff.TugServiceProvider;
import static flagship.domain.tariffs.serviceduestariffs.TugDueTariff.TugServiceProvider.PORTFLEET;
import static flagship.domain.tariffs.serviceduestariffs.TugDueTariff.TugServiceProvider.VTC;
import static java.time.Month.*;

@Component
public class ServiceDuesTariffsInitializer {

  public static void initializeTariffs(
      PilotageDueTariff pilotageDueTariff,
      TugDueTariff tugDueTariff,
      MooringDueTariff mooringDueTariff,
      HolidayCalendar holidayCalendar) {
    initializeHolidayCalendar(holidayCalendar);
    initializePilotageDueTariff(pilotageDueTariff, holidayCalendar);
    initializeTugDueTariff(tugDueTariff, holidayCalendar);
    initializeMooringDueTariff(mooringDueTariff, holidayCalendar);
  }

  private static void initializePilotageDueTariff(
      PilotageDueTariff pilotageDueTariff, HolidayCalendar holidayCalendar) {

    Map<PilotageArea, Set<PortName>> portNamesInPilotageAreas = new EnumMap<>(PilotageArea.class);

    Set<PortName> portNamesInFirstVarnaPilotageArea =
        EnumSet.of(
            PortName.VARNA_EAST,
            PortName.PETROL,
            PortName.BULYARD,
            PortName.BULPORT_LOGISTIK,
            PortName.SRY,
            PortName.PCHMV);

    Set<PortName> portNamesInSecondVarnaPilotageArea =
        EnumSet.of(
            PortName.TEC_POWER_STATION,
            PortName.BALCHIK_PORT,
            PortName.LESPORT,
            PortName.TEREM_FA,
            PortName.SRY_DOLPHIN,
            PortName.TRANSSTROI_VARNA,
            PortName.ODESSOS_PBM,
            PortName.BUOY_9,
            PortName.ANCHORAGE);

    Set<PortName> portNamesInThirdVarnaPilotageArea =
        EnumSet.of(PortName.VARNA_WEST, PortName.FERRY_COMPLEX);

    Set<PortName> portNamesInFirstBourgasPilotageArea =
        EnumSet.of(
            PortName.BOURGAS_CENTER,
            PortName.BOURGAS_EAST_2,
            PortName.BMF_PORT_BOURGAS,
            PortName.BOURGAS_WEST_TERMINAL,
            PortName.SRY_PORT_BOURGAS,
            PortName.PORT_BULGARIA_WEST,
            PortName.BOURGAS_SHIPYARD,
            PortName.PORT_EUROPA,
            PortName.TRANSSTROI_BOURGAS,
            PortName.PORT_ROSENETZ,
            PortName.NESSEBAR,
            PortName.POMORIE,
            PortName.SOZOPOL,
            PortName.TZAREVO,
            PortName.SHIFTING_ANCHORAGE_AREA,
            PortName.DEVIATION,
            PortName.XX_A_K_M,
            PortName.XX_B_K_M);

    portNamesInPilotageAreas.put(VARNA_FIRST, portNamesInFirstVarnaPilotageArea);
    portNamesInPilotageAreas.put(VARNA_SECOND, portNamesInSecondVarnaPilotageArea);
    portNamesInPilotageAreas.put(VARNA_THIRD, portNamesInThirdVarnaPilotageArea);
    portNamesInPilotageAreas.put(BOURGAS_FIRST, portNamesInFirstBourgasPilotageArea);

    pilotageDueTariff.setPortNamesInPilotageAreas(
        Collections.unmodifiableMap(portNamesInPilotageAreas));

    Map<PilotageArea, Map<Range, Due>> pilotageDuesByArea = new EnumMap<>(PilotageArea.class);

    Map<Range, Due> varnaFirstAreaPilotageDues = new LinkedHashMap<>();
    varnaFirstAreaPilotageDues.put(new Range(MIN_GT, 999), new Due(190.00));
    varnaFirstAreaPilotageDues.put(new Range(1000, 1999), new Due(220.00));
    varnaFirstAreaPilotageDues.put(new Range(2000, 2999), new Due(250.00));
    varnaFirstAreaPilotageDues.put(new Range(3000, 3999), new Due(290.00));
    varnaFirstAreaPilotageDues.put(new Range(4000, 4999), new Due(320.00));
    varnaFirstAreaPilotageDues.put(new Range(5000, 5999), new Due(350.00));
    varnaFirstAreaPilotageDues.put(new Range(6000, 6999), new Due(390.00));
    varnaFirstAreaPilotageDues.put(new Range(7000, 7999), new Due(430.00));
    varnaFirstAreaPilotageDues.put(new Range(8000, 8999), new Due(460.00));
    varnaFirstAreaPilotageDues.put(new Range(9000, 9999), new Due(500.00));
    varnaFirstAreaPilotageDues.put(new Range(10000, MAX_GT), new Due(500.00, 60));

    Map<Range, Due> varnaSecondAreaPilotageDues = new LinkedHashMap<>();
    varnaSecondAreaPilotageDues.put(new Range(MIN_GT, 999), new Due(260.00));
    varnaSecondAreaPilotageDues.put(new Range(1000, 1999), new Due(310.00));
    varnaSecondAreaPilotageDues.put(new Range(2000, 2999), new Due(370.00));
    varnaSecondAreaPilotageDues.put(new Range(3000, 3999), new Due(420.00));
    varnaSecondAreaPilotageDues.put(new Range(4000, 4999), new Due(480.00));
    varnaSecondAreaPilotageDues.put(new Range(5000, 5999), new Due(530.00));
    varnaSecondAreaPilotageDues.put(new Range(6000, 6999), new Due(590.00));
    varnaSecondAreaPilotageDues.put(new Range(7000, 7999), new Due(640.00));
    varnaSecondAreaPilotageDues.put(new Range(8000, 8999), new Due(690.00));
    varnaSecondAreaPilotageDues.put(new Range(9000, 9999), new Due(750.00));
    varnaSecondAreaPilotageDues.put(new Range(10000, MAX_GT), new Due(750.00, 70));

    Map<Range, Due> varnaThirdAreaPilotageDues = new LinkedHashMap<>();
    varnaThirdAreaPilotageDues.put(new Range(MIN_GT, 999), new Due(400.00));
    varnaThirdAreaPilotageDues.put(new Range(1000, 1999), new Due(450.00));
    varnaThirdAreaPilotageDues.put(new Range(2000, 2999), new Due(520.00));
    varnaThirdAreaPilotageDues.put(new Range(3000, 3999), new Due(570.00));
    varnaThirdAreaPilotageDues.put(new Range(4000, 4999), new Due(640.00));
    varnaThirdAreaPilotageDues.put(new Range(5000, 5999), new Due(690.00));
    varnaThirdAreaPilotageDues.put(new Range(6000, 6999), new Due(760.00));
    varnaThirdAreaPilotageDues.put(new Range(7000, 7999), new Due(810.00));
    varnaThirdAreaPilotageDues.put(new Range(8000, 8999), new Due(870.00));
    varnaThirdAreaPilotageDues.put(new Range(9000, 9999), new Due(940.00));
    varnaThirdAreaPilotageDues.put(new Range(10000, MAX_GT), new Due(940.00, 80));

    Map<Range, Due> bourgasFirstAreaPilotageDues = new LinkedHashMap<>();
    bourgasFirstAreaPilotageDues.put(new Range(MIN_GT, 499), new Due(240.00));
    bourgasFirstAreaPilotageDues.put(new Range(500, 999), new Due(280.00));
    bourgasFirstAreaPilotageDues.put(new Range(1000, 1999), new Due(320.00));
    bourgasFirstAreaPilotageDues.put(new Range(2000, 2999), new Due(380.00));
    bourgasFirstAreaPilotageDues.put(new Range(3000, 3999), new Due(440.00));
    bourgasFirstAreaPilotageDues.put(new Range(4000, 4999), new Due(480.00));
    bourgasFirstAreaPilotageDues.put(new Range(5000, 5999), new Due(520.00));
    bourgasFirstAreaPilotageDues.put(new Range(6000, 6999), new Due(560.00));
    bourgasFirstAreaPilotageDues.put(new Range(7000, 7999), new Due(600.00));
    bourgasFirstAreaPilotageDues.put(new Range(8000, 8999), new Due(640.00));
    bourgasFirstAreaPilotageDues.put(new Range(9000, 9999), new Due(680.00));
    bourgasFirstAreaPilotageDues.put(new Range(10000, MAX_GT), new Due(680.00, 75));

    pilotageDuesByArea.put(VARNA_FIRST, varnaFirstAreaPilotageDues);
    pilotageDuesByArea.put(VARNA_SECOND, varnaSecondAreaPilotageDues);
    pilotageDuesByArea.put(VARNA_THIRD, varnaThirdAreaPilotageDues);
    pilotageDuesByArea.put(BOURGAS_FIRST, bourgasFirstAreaPilotageDues);

    pilotageDueTariff.setPilotageDuesByArea(Collections.unmodifiableMap(pilotageDuesByArea));

    Map<PdaWarning, BigDecimal> increaseCoefficientsByWarningType = new EnumMap<>(PdaWarning.class);

    increaseCoefficientsByWarningType.put(HOLIDAY, BigDecimal.valueOf(0.5));
    increaseCoefficientsByWarningType.put(SPECIAL_PILOT, BigDecimal.valueOf(0.5));
    increaseCoefficientsByWarningType.put(HAZARDOUS_PILOTAGE_CARGO, BigDecimal.valueOf(0.2));
    increaseCoefficientsByWarningType.put(SPECIAL_PILOTAGE_CARGO, BigDecimal.valueOf(1.0));

    pilotageDueTariff.setIncreaseCoefficientsByWarningType(
        Collections.unmodifiableMap(increaseCoefficientsByWarningType));

    pilotageDueTariff.setGrossTonnageThreshold(BigDecimal.valueOf(10000.00));

    pilotageDueTariff.setHolidayCalendar(holidayCalendar.getHolidayCalendar());
  }

  private static void initializeHolidayCalendar(HolidayCalendar holidayCalendar) {

    int year = LocalDate.now().getYear();
    LocalDate easter = LocalDate.of(LocalDate.now().getYear(), MAY, 2);

    Set<LocalDate> officialHolidays = new TreeSet<>();

    officialHolidays.add(easter);

    officialHolidays.add(LocalDate.of(year, JANUARY, 1));
    officialHolidays.add(LocalDate.of(year, MARCH, 3));
    // todo: reserach why April of 30th is in the official holidays.
    officialHolidays.add(LocalDate.of(year, APRIL, 30));
    officialHolidays.add(LocalDate.of(year, MAY, 1));
    officialHolidays.add(LocalDate.of(year, MAY, 6));
    officialHolidays.add(LocalDate.of(year, MAY, 24));
    officialHolidays.add(LocalDate.of(year, SEPTEMBER, 6));
    officialHolidays.add(LocalDate.of(year, SEPTEMBER, 22));
    officialHolidays.add(LocalDate.of(year, DECEMBER, 24));
    officialHolidays.add(LocalDate.of(year, DECEMBER, 25));
    officialHolidays.add(LocalDate.of(year, DECEMBER, 26));

    Set<LocalDate> resolvedHolidays = HolidayCalendarResolver.resolve(officialHolidays);
    holidayCalendar.setHolidayCalendar(resolvedHolidays);
  }

  private static void initializeTugDueTariff(
      TugDueTariff tugDueTariff, HolidayCalendar holidayCalendar) {

    Map<TugServiceProvider, Map<TugArea, Set<PortName>>> portNamesInTugAreas =
        new EnumMap<>(TugServiceProvider.class);

    Map<TugArea, Set<PortName>> portNamesInVtcTugAreas = new EnumMap<>(TugArea.class);

    Set<PortName> portNamesInVtcFirstTugArea =
        EnumSet.of(
            PortName.VARNA_EAST,
            PortName.PCHMV,
            PortName.ODESSOS_PBM,
            PortName.PETROL,
            PortName.LESPORT,
            PortName.TEC_POWER_STATION);

    Set<PortName> portNamesInVtcSecondTugArea =
        EnumSet.of(PortName.VARNA_WEST, PortName.FERRY_COMPLEX);

    Set<PortName> portNamesInVtcThirdTugArea =
        EnumSet.of(PortName.BULYARD, PortName.SRY_ODESSOS, PortName.MTG_DOLPHIN, PortName.TEREM_FA);

    Set<PortName> portNamesInVtcFourthTugArea =
        EnumSet.of(
            PortName.SHIFTING_BULYARD,
            PortName.SHIFTING_SRY_ODESSOS,
            PortName.SHIFTING_MTG_DOLPHIN,
            PortName.SHIFTING_TEREM_FA);

    Set<PortName> portNamesInVtcFifthTugArea = EnumSet.of(PortName.BALCHIK_PORT);

    portNamesInVtcTugAreas.put(VTC_FIRST, portNamesInVtcFirstTugArea);
    portNamesInVtcTugAreas.put(VTC_SECOND, portNamesInVtcSecondTugArea);
    portNamesInVtcTugAreas.put(VTC_THIRD, portNamesInVtcThirdTugArea);
    portNamesInVtcTugAreas.put(VTC_FOURTH, portNamesInVtcFourthTugArea);
    portNamesInVtcTugAreas.put(VTC_FIFTH, portNamesInVtcFifthTugArea);

    Map<TugArea, Set<PortName>> portNamesInPortFleetTugAreas = new EnumMap<>(TugArea.class);

    Set<PortName> portNamesInPortfleetFirstTugArea =
        EnumSet.of(
            PortName.VARNA_EAST,
            PortName.TEC_EZEROVO,
            PortName.PETROL,
            PortName.LESPORT,
            PortName.ODESSOS_PBM,
            PortName.PCHMV);

    Set<PortName> portNamesInPortfleetSecondTugArea =
        EnumSet.of(PortName.VARNA_WEST, PortName.FERRY_COMPLEX);

    Set<PortName> portNamesInPortfleetThirdTugArea =
        EnumSet.of(PortName.BULYARD, PortName.SRY_ODESSOS, PortName.MTG_DOLPHIN, PortName.TEREM_FA);

    Set<PortName> portNamesInPortfleetFourthTugArea =
        EnumSet.of(
            PortName.SHIFTING_BULYARD,
            PortName.SHIFTING_SRY_ODESSOS,
            PortName.SHIFTING_MTG_DOLPHIN,
            PortName.SHIFTING_TEREM_FA);

    Set<PortName> portNamesInPortfleetFifthTugArea = EnumSet.of(PortName.BALCHIK_PORT);

    portNamesInPortFleetTugAreas.put(PORTFLEET_FIRST, portNamesInPortfleetFirstTugArea);
    portNamesInPortFleetTugAreas.put(PORTFLEET_SECOND, portNamesInPortfleetSecondTugArea);
    portNamesInPortFleetTugAreas.put(PORTFLEET_THIRD, portNamesInPortfleetThirdTugArea);
    portNamesInPortFleetTugAreas.put(PORTFLEET_FOURTH, portNamesInPortfleetFourthTugArea);
    portNamesInPortFleetTugAreas.put(PORTFLEET_FIFTH, portNamesInPortfleetFifthTugArea);

    portNamesInTugAreas.put(VTC, portNamesInVtcTugAreas);
    portNamesInTugAreas.put(PORTFLEET, portNamesInPortFleetTugAreas);

    Map<TugArea, Map<Range, Due>> tugDuesByArea = new EnumMap<>(TugArea.class);

    Map<Range, Due> tugDuesInVtcFirstTugArea = new LinkedHashMap<>();
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

    Map<Range, Due> tugDuesInVtcSecondTugArea = new LinkedHashMap<>();
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

    Map<Range, Due> tugDuesInVtcThirdTugArea = new LinkedHashMap<>();
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

    Map<Range, Due> tugDuesInVtcFourthTugArea = new LinkedHashMap<>();
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

    Map<Range, Due> tugDuesInVtcFifthTugArea = new LinkedHashMap<>();
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

    Map<Range, Due> tugDuesInPfFirstTugArea = new LinkedHashMap<>();
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

    Map<Range, Due> tugDuesInPfSecondTugArea = new LinkedHashMap<>();
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

    Map<Range, Due> tugDuesInPfThirdTugArea = new LinkedHashMap<>();
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

    Map<Range, Due> tugDuesInPfFourthTugArea = new LinkedHashMap<>();
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

    Map<Range, Due> tugDuesInPfFifthTugArea = new LinkedHashMap<>();
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

    Map<Range, BigDecimal> tugCountByGrossTonnage = new LinkedHashMap<>();
    tugCountByGrossTonnage.put(new Range(MIN_GT, 4499), BigDecimal.valueOf(1.00));
    tugCountByGrossTonnage.put(new Range(4500, 17999), BigDecimal.valueOf(2.00));
    tugCountByGrossTonnage.put(new Range(18000, MAX_GT), BigDecimal.valueOf(3.00));

    Map<PdaWarning, BigDecimal> increaseCoefficientsByWarningType = new EnumMap<>(PdaWarning.class);
    increaseCoefficientsByWarningType.put(HOLIDAY, BigDecimal.valueOf(1.0));

    tugDueTariff.setPortNamesInTugAreas(Collections.unmodifiableMap(portNamesInTugAreas));
    tugDueTariff.setTugDuesByArea(Collections.unmodifiableMap(tugDuesByArea));
    tugDueTariff.setTugCountByGrossTonnage(Collections.unmodifiableMap(tugCountByGrossTonnage));
    tugDueTariff.setIncreaseCoefficientsByWarningType(
        Collections.unmodifiableMap(increaseCoefficientsByWarningType));

    tugDueTariff.setHolidayCalendar(holidayCalendar.getHolidayCalendar());
    tugDueTariff.setGrossTonnageThreshold(BigDecimal.valueOf(10000.00));
    tugDueTariff.setGrossTonnageThresholdForTugCountReduce(BigDecimal.valueOf(18000.00));
  }

  private static void initializeMooringDueTariff(
      MooringDueTariff mooringDueTariff, HolidayCalendar holidayCalendar) {

    Map<MooringServiceProvider, Map<Range, Due>> mooringDuesByProvider =
        new EnumMap<>(MooringServiceProvider.class);

    Map<Range, Due> lesportMooringDues = new LinkedHashMap<>();
    lesportMooringDues.put(new Range(MIN_GT, 3000), new Due(100.00));
    lesportMooringDues.put(new Range(3001, 6000), new Due(130.00));
    lesportMooringDues.put(new Range(6001, 8000), new Due(160.00));
    lesportMooringDues.put(new Range(8001, 10000), new Due(190.00));
    lesportMooringDues.put(new Range(10001, MAX_GT), new Due(190.00, 40.00));

    Map<Range, Due> odessosMooringDues = new LinkedHashMap<>();
    odessosMooringDues.put(new Range(MIN_GT, 1000), new Due(120.00));
    odessosMooringDues.put(new Range(1001, 3000), new Due(160.00));
    odessosMooringDues.put(new Range(3001, 5000), new Due(240.00));
    odessosMooringDues.put(new Range(5001, MAX_GT), new Due(300.00));

    Map<Range, Due> balchikMooringDues = new LinkedHashMap<>();
    balchikMooringDues.put(new Range(MIN_GT, 3000), new Due(80.00));
    balchikMooringDues.put(new Range(3001, 6000), new Due(110.00));
    balchikMooringDues.put(new Range(6001, 8000), new Due(130.00));
    balchikMooringDues.put(new Range(8001, 10000), new Due(160.00));
    balchikMooringDues.put(new Range(10001, MAX_GT), new Due(160.00));

    Map<Range, Due> pchvmMooringDues = new LinkedHashMap<>();
    pchvmMooringDues.put(new Range(MIN_GT, 1000), new Due(50.00));
    pchvmMooringDues.put(new Range(1001, 2000), new Due(70.00));
    pchvmMooringDues.put(new Range(2001, 3000), new Due(80.00));
    pchvmMooringDues.put(new Range(3001, 4000), new Due(100.00));
    pchvmMooringDues.put(new Range(4001, 5000), new Due(110.00));
    pchvmMooringDues.put(new Range(5001, 6000), new Due(120.00));
    pchvmMooringDues.put(new Range(6001, 8000), new Due(130.00));
    pchvmMooringDues.put(new Range(8001, 10000), new Due(150.00));
    pchvmMooringDues.put(new Range(10001, MAX_GT), new Due(150.00, 15.00));

    Map<Range, Due> vtcMooringDues = new LinkedHashMap<>();
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

    Map<Range, Due> portfleetMooringDues = new LinkedHashMap<>();
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

    Map<PdaWarning, BigDecimal> increaseCoefficientsByWarningType = new EnumMap<>(PdaWarning.class);
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
  }
}
