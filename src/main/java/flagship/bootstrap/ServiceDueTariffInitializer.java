package flagship.bootstrap;

import flagship.domain.calculators.HolidayCalendar;
import flagship.domain.calculators.resolvers.HolidayCalendarResolver;
import flagship.domain.calculators.tariffs.enums.PdaWarning;
import flagship.domain.calculators.tariffs.enums.PortName;
import flagship.domain.calculators.tariffs.serviceduestariffs.MooringDueTariff;
import flagship.domain.calculators.tariffs.serviceduestariffs.PilotageDueTariff;
import flagship.domain.calculators.tariffs.serviceduestariffs.PilotageDueTariff.PilotageArea;
import flagship.domain.calculators.tariffs.serviceduestariffs.TugDueTariff;
import flagship.domain.cases.entities.enums.CargoType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static flagship.domain.calculators.tariffs.enums.PdaWarning.HOLIDAY;
import static flagship.domain.calculators.tariffs.enums.PdaWarning.PILOT;
import static flagship.domain.calculators.tariffs.serviceduestariffs.MooringDueTariff.MooringServiceProvider;
import static flagship.domain.calculators.tariffs.serviceduestariffs.PilotageDueTariff.PilotageArea.*;
import static flagship.domain.calculators.tariffs.serviceduestariffs.TugDueTariff.TugArea;
import static flagship.domain.calculators.tariffs.serviceduestariffs.TugDueTariff.TugArea.*;
import static flagship.domain.calculators.tariffs.serviceduestariffs.TugDueTariff.TugServiceProvider;
import static flagship.domain.calculators.tariffs.serviceduestariffs.TugDueTariff.TugServiceProvider.PORTFLEET;
import static flagship.domain.calculators.tariffs.serviceduestariffs.TugDueTariff.TugServiceProvider.VTC;
import static flagship.domain.cases.entities.enums.CargoType.HAZARDOUS;
import static flagship.domain.cases.entities.enums.CargoType.SPECIAL;
import static java.time.Month.*;

@Component
public class ServiceDueTariffInitializer {

  public static void initializeTariff(
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
            PortName.WEST_TERMINAL,
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

    Map<PilotageArea, Map<BigDecimal, Integer[]>> pilotageDuesByArea =
        new EnumMap<>(PilotageArea.class);

    Map<BigDecimal, Integer[]> varnaFirstAreaPilotageDues = new LinkedHashMap<>();
    varnaFirstAreaPilotageDues.put(BigDecimal.valueOf(190.00), new Integer[] {0, 999, 60});
    varnaFirstAreaPilotageDues.put(BigDecimal.valueOf(220.00), new Integer[] {1000, 1999, 60});
    varnaFirstAreaPilotageDues.put(BigDecimal.valueOf(250.00), new Integer[] {2000, 2999, 60});
    varnaFirstAreaPilotageDues.put(BigDecimal.valueOf(290.00), new Integer[] {3000, 3999, 60});
    varnaFirstAreaPilotageDues.put(BigDecimal.valueOf(320.00), new Integer[] {4000, 4999, 60});
    varnaFirstAreaPilotageDues.put(BigDecimal.valueOf(350.00), new Integer[] {5000, 5999, 60});
    varnaFirstAreaPilotageDues.put(BigDecimal.valueOf(390.00), new Integer[] {6000, 6999, 60});
    varnaFirstAreaPilotageDues.put(BigDecimal.valueOf(430.00), new Integer[] {7000, 7999, 60});
    varnaFirstAreaPilotageDues.put(BigDecimal.valueOf(460.00), new Integer[] {8000, 8999, 60});
    varnaFirstAreaPilotageDues.put(BigDecimal.valueOf(500.00), new Integer[] {9000, 9999, 60});

    Map<BigDecimal, Integer[]> varnaSecondAreaPilotageDues = new LinkedHashMap<>();
    varnaSecondAreaPilotageDues.put(BigDecimal.valueOf(260.00), new Integer[] {0, 999, 70});
    varnaSecondAreaPilotageDues.put(BigDecimal.valueOf(310.00), new Integer[] {1000, 1999, 70});
    varnaSecondAreaPilotageDues.put(BigDecimal.valueOf(370.00), new Integer[] {2000, 2999, 70});
    varnaSecondAreaPilotageDues.put(BigDecimal.valueOf(420.00), new Integer[] {3000, 3999, 70});
    varnaSecondAreaPilotageDues.put(BigDecimal.valueOf(480.00), new Integer[] {4000, 4999, 70});
    varnaSecondAreaPilotageDues.put(BigDecimal.valueOf(530.00), new Integer[] {5000, 5999, 70});
    varnaSecondAreaPilotageDues.put(BigDecimal.valueOf(590.00), new Integer[] {6000, 6999, 70});
    varnaSecondAreaPilotageDues.put(BigDecimal.valueOf(640.00), new Integer[] {7000, 7999, 70});
    varnaSecondAreaPilotageDues.put(BigDecimal.valueOf(690.00), new Integer[] {8000, 8999, 70});
    varnaSecondAreaPilotageDues.put(BigDecimal.valueOf(750.00), new Integer[] {9000, 9999, 70});

    Map<BigDecimal, Integer[]> varnaThirdAreaPilotageDues = new LinkedHashMap<>();
    varnaThirdAreaPilotageDues.put(BigDecimal.valueOf(400.00), new Integer[] {0, 999, 80});
    varnaThirdAreaPilotageDues.put(BigDecimal.valueOf(450.00), new Integer[] {1000, 1999, 80});
    varnaThirdAreaPilotageDues.put(BigDecimal.valueOf(520.00), new Integer[] {2000, 2999, 80});
    varnaThirdAreaPilotageDues.put(BigDecimal.valueOf(570.00), new Integer[] {3000, 3999, 80});
    varnaThirdAreaPilotageDues.put(BigDecimal.valueOf(640.00), new Integer[] {4000, 4999, 80});
    varnaThirdAreaPilotageDues.put(BigDecimal.valueOf(690.00), new Integer[] {5000, 5999, 80});
    varnaThirdAreaPilotageDues.put(BigDecimal.valueOf(760.00), new Integer[] {6000, 6999, 80});
    varnaThirdAreaPilotageDues.put(BigDecimal.valueOf(810.00), new Integer[] {7000, 7999, 80});
    varnaThirdAreaPilotageDues.put(BigDecimal.valueOf(870.00), new Integer[] {8000, 8999, 80});
    varnaThirdAreaPilotageDues.put(BigDecimal.valueOf(940.00), new Integer[] {9000, 9999, 80});

    Map<PilotageArea, Map<BigDecimal, Integer[]>> bourgasAreaPilotageFees =
        new EnumMap<>(PilotageArea.class);

    Map<BigDecimal, Integer[]> bourgasFirstAreaPilotageDues = new LinkedHashMap<>();
    bourgasFirstAreaPilotageDues.put(BigDecimal.valueOf(240.00), new Integer[] {0, 499, 75});
    bourgasFirstAreaPilotageDues.put(BigDecimal.valueOf(280.00), new Integer[] {500, 999, 75});
    bourgasFirstAreaPilotageDues.put(BigDecimal.valueOf(320.00), new Integer[] {1000, 1999, 75});
    bourgasFirstAreaPilotageDues.put(BigDecimal.valueOf(380.00), new Integer[] {2000, 2999, 75});
    bourgasFirstAreaPilotageDues.put(BigDecimal.valueOf(440.00), new Integer[] {3000, 3999, 75});
    bourgasFirstAreaPilotageDues.put(BigDecimal.valueOf(480.00), new Integer[] {4000, 4999, 75});
    bourgasFirstAreaPilotageDues.put(BigDecimal.valueOf(520.00), new Integer[] {5000, 5999, 75});
    bourgasFirstAreaPilotageDues.put(BigDecimal.valueOf(560.00), new Integer[] {6000, 6999, 75});
    bourgasFirstAreaPilotageDues.put(BigDecimal.valueOf(600.00), new Integer[] {7000, 7999, 75});
    bourgasFirstAreaPilotageDues.put(BigDecimal.valueOf(640.00), new Integer[] {8000, 8999, 75});
    bourgasFirstAreaPilotageDues.put(BigDecimal.valueOf(680.00), new Integer[] {9000, 9999, 75});

    pilotageDuesByArea.put(VARNA_FIRST, varnaFirstAreaPilotageDues);
    pilotageDuesByArea.put(VARNA_SECOND, varnaSecondAreaPilotageDues);
    pilotageDuesByArea.put(VARNA_THIRD, varnaThirdAreaPilotageDues);
    pilotageDuesByArea.put(BOURGAS_FIRST, bourgasFirstAreaPilotageDues);

    pilotageDueTariff.setPilotageDuesByArea(Collections.unmodifiableMap(pilotageDuesByArea));

    Map<CargoType, BigDecimal> increaseCoefficientByCargoType = new EnumMap<>(CargoType.class);
    increaseCoefficientByCargoType.put(HAZARDOUS, BigDecimal.valueOf(0.2));
    increaseCoefficientByCargoType.put(SPECIAL, BigDecimal.valueOf(1.0));

    pilotageDueTariff.setIncreaseCoefficientsByCargoType(
        Collections.unmodifiableMap(increaseCoefficientByCargoType));

    Map<PdaWarning, BigDecimal> increaseCoefficientsByWarningType = new EnumMap<>(PdaWarning.class);

    increaseCoefficientsByWarningType.put(HOLIDAY, BigDecimal.valueOf(0.5));
    increaseCoefficientsByWarningType.put(PILOT, BigDecimal.valueOf(0.5));

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

    Map<TugArea, Map<BigDecimal, Integer[]>> tugDuesByArea = new EnumMap<>(TugArea.class);

    Map<BigDecimal, Integer[]> tugDuesInVtcFirstTugArea = new TreeMap<>();
    tugDuesInVtcFirstTugArea.put(BigDecimal.valueOf(300.00), new Integer[] {150, 1000, 45});
    tugDuesInVtcFirstTugArea.put(BigDecimal.valueOf(410.00), new Integer[] {1001, 2000, 45});
    tugDuesInVtcFirstTugArea.put(BigDecimal.valueOf(520.00), new Integer[] {2001, 2499, 45});
    tugDuesInVtcFirstTugArea.put(BigDecimal.valueOf(820.00), new Integer[] {2500, 3000, 55});
    tugDuesInVtcFirstTugArea.put(BigDecimal.valueOf(1020.00), new Integer[] {3001, 4000, 55});
    tugDuesInVtcFirstTugArea.put(BigDecimal.valueOf(1220.00), new Integer[] {4001, 5000, 55});
    tugDuesInVtcFirstTugArea.put(BigDecimal.valueOf(1420.00), new Integer[] {5001, 6000, 55});
    tugDuesInVtcFirstTugArea.put(BigDecimal.valueOf(1620.00), new Integer[] {6001, 7000, 55});
    tugDuesInVtcFirstTugArea.put(BigDecimal.valueOf(1820.00), new Integer[] {7001, 8000, 55});
    tugDuesInVtcFirstTugArea.put(BigDecimal.valueOf(2020.00), new Integer[] {8001, 9000, 55});
    tugDuesInVtcFirstTugArea.put(BigDecimal.valueOf(2220.00), new Integer[] {9001, 10000, 55});

    Map<BigDecimal, Integer[]> tugDuesInVtcSecondTugArea = new TreeMap<>();
    tugDuesInVtcSecondTugArea.put(BigDecimal.valueOf(300.00), new Integer[] {150, 1000, 45});
    tugDuesInVtcSecondTugArea.put(BigDecimal.valueOf(410.00), new Integer[] {1001, 2000, 45});
    tugDuesInVtcSecondTugArea.put(BigDecimal.valueOf(520.00), new Integer[] {2001, 2499, 45});
    tugDuesInVtcSecondTugArea.put(BigDecimal.valueOf(1060.00), new Integer[] {2500, 3000, 65});
    tugDuesInVtcSecondTugArea.put(BigDecimal.valueOf(1320.00), new Integer[] {3001, 4000, 65});
    tugDuesInVtcSecondTugArea.put(BigDecimal.valueOf(1580.00), new Integer[] {4001, 5000, 65});
    tugDuesInVtcSecondTugArea.put(BigDecimal.valueOf(1840.00), new Integer[] {5001, 6000, 65});
    tugDuesInVtcSecondTugArea.put(BigDecimal.valueOf(2100.00), new Integer[] {6001, 7000, 65});
    tugDuesInVtcSecondTugArea.put(BigDecimal.valueOf(2360.00), new Integer[] {7001, 8000, 65});
    tugDuesInVtcSecondTugArea.put(BigDecimal.valueOf(2620.00), new Integer[] {8001, 9000, 65});
    tugDuesInVtcSecondTugArea.put(BigDecimal.valueOf(2880.00), new Integer[] {9001, 10000, 65});

    Map<BigDecimal, Integer[]> tugDuesInVtcThirdTugArea = new TreeMap<>();
    tugDuesInVtcThirdTugArea.put(BigDecimal.valueOf(300.00), new Integer[] {150, 1000, 45});
    tugDuesInVtcThirdTugArea.put(BigDecimal.valueOf(410.00), new Integer[] {1001, 2000, 45});
    tugDuesInVtcThirdTugArea.put(BigDecimal.valueOf(520.00), new Integer[] {2001, 2499, 45});
    tugDuesInVtcThirdTugArea.put(BigDecimal.valueOf(720.00), new Integer[] {2500, 3000, 50});
    tugDuesInVtcThirdTugArea.put(BigDecimal.valueOf(870.00), new Integer[] {3001, 4000, 50});
    tugDuesInVtcThirdTugArea.put(BigDecimal.valueOf(1020.00), new Integer[] {4001, 5000, 50});
    tugDuesInVtcThirdTugArea.put(BigDecimal.valueOf(1170.00), new Integer[] {5001, 6000, 50});
    tugDuesInVtcThirdTugArea.put(BigDecimal.valueOf(1320.00), new Integer[] {6001, 7000, 50});
    tugDuesInVtcThirdTugArea.put(BigDecimal.valueOf(1470.00), new Integer[] {7001, 8000, 50});
    tugDuesInVtcThirdTugArea.put(BigDecimal.valueOf(1620.00), new Integer[] {8001, 9000, 50});
    tugDuesInVtcThirdTugArea.put(BigDecimal.valueOf(1770.00), new Integer[] {9001, 10000, 50});

    Map<BigDecimal, Integer[]> tugDuesInVtcFourthTugArea = new TreeMap<>();
    tugDuesInVtcFourthTugArea.put(BigDecimal.valueOf(300.00), new Integer[] {150, 1000, 45});
    tugDuesInVtcFourthTugArea.put(BigDecimal.valueOf(410.00), new Integer[] {1001, 2000, 45});
    tugDuesInVtcFourthTugArea.put(BigDecimal.valueOf(520.00), new Integer[] {2001, 2499, 45});
    tugDuesInVtcFourthTugArea.put(BigDecimal.valueOf(650.00), new Integer[] {2500, 3000, 50});
    tugDuesInVtcFourthTugArea.put(BigDecimal.valueOf(780.00), new Integer[] {3001, 4000, 50});
    tugDuesInVtcFourthTugArea.put(BigDecimal.valueOf(910.00), new Integer[] {4001, 5000, 50});
    tugDuesInVtcFourthTugArea.put(BigDecimal.valueOf(1040.00), new Integer[] {5001, 6000, 50});
    tugDuesInVtcFourthTugArea.put(BigDecimal.valueOf(1170.00), new Integer[] {6001, 7000, 50});
    tugDuesInVtcFourthTugArea.put(BigDecimal.valueOf(1300.00), new Integer[] {7001, 8000, 50});
    tugDuesInVtcFourthTugArea.put(BigDecimal.valueOf(1430.00), new Integer[] {8001, 9000, 50});
    tugDuesInVtcFourthTugArea.put(BigDecimal.valueOf(1560.00), new Integer[] {9001, 10000, 50});

    Map<BigDecimal, Integer[]> tugDuesInVtcFifthTugArea = new TreeMap<>();
    tugDuesInVtcFifthTugArea.put(BigDecimal.valueOf(430.00), new Integer[] {150, 1000, 65});
    tugDuesInVtcFifthTugArea.put(BigDecimal.valueOf(560.00), new Integer[] {1001, 2000, 65});
    tugDuesInVtcFifthTugArea.put(BigDecimal.valueOf(690.00), new Integer[] {2001, 2499, 65});
    tugDuesInVtcFifthTugArea.put(BigDecimal.valueOf(1060.00), new Integer[] {2500, 3000, 65});
    tugDuesInVtcFifthTugArea.put(BigDecimal.valueOf(1320.00), new Integer[] {3001, 4000, 65});
    tugDuesInVtcFifthTugArea.put(BigDecimal.valueOf(1580.00), new Integer[] {4001, 5000, 65});
    tugDuesInVtcFifthTugArea.put(BigDecimal.valueOf(1840.00), new Integer[] {5001, 6000, 65});
    tugDuesInVtcFifthTugArea.put(BigDecimal.valueOf(2100.00), new Integer[] {6001, 7000, 65});
    tugDuesInVtcFifthTugArea.put(BigDecimal.valueOf(2360.00), new Integer[] {7001, 8000, 65});
    tugDuesInVtcFifthTugArea.put(BigDecimal.valueOf(2620.00), new Integer[] {8001, 9000, 65});
    tugDuesInVtcFifthTugArea.put(BigDecimal.valueOf(2880.00), new Integer[] {9001, 10000, 65});

    Map<BigDecimal, Integer[]> tugDuesInPortFleetFirstTugArea = new TreeMap<>();
    tugDuesInPortFleetFirstTugArea.put(BigDecimal.valueOf(300.00), new Integer[] {150, 1000, 45});
    tugDuesInPortFleetFirstTugArea.put(BigDecimal.valueOf(410.00), new Integer[] {1001, 2000, 45});
    tugDuesInPortFleetFirstTugArea.put(BigDecimal.valueOf(520.00), new Integer[] {2001, 2499, 45});
    tugDuesInPortFleetFirstTugArea.put(BigDecimal.valueOf(820.00), new Integer[] {2500, 3000, 55});
    tugDuesInPortFleetFirstTugArea.put(BigDecimal.valueOf(1020.00), new Integer[] {3001, 4000, 55});
    tugDuesInPortFleetFirstTugArea.put(BigDecimal.valueOf(1220.00), new Integer[] {4001, 5000, 55});
    tugDuesInPortFleetFirstTugArea.put(BigDecimal.valueOf(1420.00), new Integer[] {5001, 6000, 55});
    tugDuesInPortFleetFirstTugArea.put(BigDecimal.valueOf(1620.00), new Integer[] {6001, 7000, 55});
    tugDuesInPortFleetFirstTugArea.put(BigDecimal.valueOf(1820.00), new Integer[] {7001, 8000, 55});
    tugDuesInPortFleetFirstTugArea.put(BigDecimal.valueOf(2020.00), new Integer[] {8001, 9000, 55});
    tugDuesInPortFleetFirstTugArea.put(
        BigDecimal.valueOf(2220.00), new Integer[] {9001, 10000, 55});

    Map<BigDecimal, Integer[]> tugDuesInPFSecondTugArea = new TreeMap<>();
    tugDuesInPFSecondTugArea.put(BigDecimal.valueOf(300.00), new Integer[] {150, 1000, 45});
    tugDuesInPFSecondTugArea.put(BigDecimal.valueOf(410.00), new Integer[] {1001, 2000, 45});
    tugDuesInPFSecondTugArea.put(BigDecimal.valueOf(520.00), new Integer[] {2001, 2499, 45});
    tugDuesInPFSecondTugArea.put(BigDecimal.valueOf(1060.00), new Integer[] {2500, 3000, 65});
    tugDuesInPFSecondTugArea.put(BigDecimal.valueOf(1320.00), new Integer[] {3001, 4000, 65});
    tugDuesInPFSecondTugArea.put(BigDecimal.valueOf(1580.00), new Integer[] {4001, 5000, 65});
    tugDuesInPFSecondTugArea.put(BigDecimal.valueOf(1840.00), new Integer[] {5001, 6000, 65});
    tugDuesInPFSecondTugArea.put(BigDecimal.valueOf(2100.00), new Integer[] {6001, 7000, 65});
    tugDuesInPFSecondTugArea.put(BigDecimal.valueOf(2360.00), new Integer[] {7001, 8000, 65});
    tugDuesInPFSecondTugArea.put(BigDecimal.valueOf(2620.00), new Integer[] {8001, 9000, 65});
    tugDuesInPFSecondTugArea.put(BigDecimal.valueOf(2880.00), new Integer[] {9001, 10000, 65});

    Map<BigDecimal, Integer[]> tugDuesInPFThirdTugArea = new TreeMap<>();
    tugDuesInPFThirdTugArea.put(BigDecimal.valueOf(300.00), new Integer[] {150, 1000, 45});
    tugDuesInPFThirdTugArea.put(BigDecimal.valueOf(410.00), new Integer[] {1001, 2000, 45});
    tugDuesInPFThirdTugArea.put(BigDecimal.valueOf(520.00), new Integer[] {2001, 2499, 45});
    tugDuesInPFThirdTugArea.put(BigDecimal.valueOf(720.00), new Integer[] {2500, 3000, 50});
    tugDuesInPFThirdTugArea.put(BigDecimal.valueOf(870.00), new Integer[] {3001, 4000, 50});
    tugDuesInPFThirdTugArea.put(BigDecimal.valueOf(1020.00), new Integer[] {4001, 5000, 50});
    tugDuesInPFThirdTugArea.put(BigDecimal.valueOf(1170.00), new Integer[] {5001, 6000, 50});
    tugDuesInPFThirdTugArea.put(BigDecimal.valueOf(1320.00), new Integer[] {6001, 7000, 50});
    tugDuesInPFThirdTugArea.put(BigDecimal.valueOf(1470.00), new Integer[] {7001, 8000, 50});
    tugDuesInPFThirdTugArea.put(BigDecimal.valueOf(1620.00), new Integer[] {8001, 9000, 50});
    tugDuesInPFThirdTugArea.put(BigDecimal.valueOf(1770.00), new Integer[] {9001, 10000, 50});

    Map<BigDecimal, Integer[]> tugDuesInPFFourthTugArea = new TreeMap<>();
    tugDuesInPFFourthTugArea.put(BigDecimal.valueOf(300.00), new Integer[] {150, 1000, 45});
    tugDuesInPFFourthTugArea.put(BigDecimal.valueOf(410.00), new Integer[] {1001, 2000, 45});
    tugDuesInPFFourthTugArea.put(BigDecimal.valueOf(520.00), new Integer[] {2001, 2499, 45});
    tugDuesInPFFourthTugArea.put(BigDecimal.valueOf(650.00), new Integer[] {2500, 3000, 50});
    tugDuesInPFFourthTugArea.put(BigDecimal.valueOf(780.00), new Integer[] {3001, 4000, 50});
    tugDuesInPFFourthTugArea.put(BigDecimal.valueOf(910.00), new Integer[] {4001, 5000, 50});
    tugDuesInPFFourthTugArea.put(BigDecimal.valueOf(1040.00), new Integer[] {5001, 6000, 50});
    tugDuesInPFFourthTugArea.put(BigDecimal.valueOf(1170.00), new Integer[] {6001, 7000, 50});
    tugDuesInPFFourthTugArea.put(BigDecimal.valueOf(1300.00), new Integer[] {7001, 8000, 50});
    tugDuesInPFFourthTugArea.put(BigDecimal.valueOf(1430.00), new Integer[] {8001, 9000, 50});
    tugDuesInPFFourthTugArea.put(BigDecimal.valueOf(1560.00), new Integer[] {9001, 10000, 50});

    Map<BigDecimal, Integer[]> tugDuesInPFFifthTugArea = new TreeMap<>();
    tugDuesInPFFifthTugArea.put(BigDecimal.valueOf(430.00), new Integer[] {150, 1000, 65});
    tugDuesInPFFifthTugArea.put(BigDecimal.valueOf(560.00), new Integer[] {1001, 2000, 65});
    tugDuesInPFFifthTugArea.put(BigDecimal.valueOf(690.00), new Integer[] {2001, 2499, 65});
    tugDuesInPFFifthTugArea.put(BigDecimal.valueOf(1060.00), new Integer[] {2500, 3000, 65});
    tugDuesInPFFifthTugArea.put(BigDecimal.valueOf(1320.00), new Integer[] {3001, 4000, 65});
    tugDuesInPFFifthTugArea.put(BigDecimal.valueOf(1580.00), new Integer[] {4001, 5000, 65});
    tugDuesInPFFifthTugArea.put(BigDecimal.valueOf(1840.00), new Integer[] {5001, 6000, 65});
    tugDuesInPFFifthTugArea.put(BigDecimal.valueOf(2100.00), new Integer[] {6001, 7000, 65});
    tugDuesInPFFifthTugArea.put(BigDecimal.valueOf(2360.00), new Integer[] {7001, 8000, 65});
    tugDuesInPFFifthTugArea.put(BigDecimal.valueOf(2620.00), new Integer[] {8001, 9000, 65});
    tugDuesInPFFifthTugArea.put(BigDecimal.valueOf(2880.00), new Integer[] {9001, 10000, 65});

    tugDuesByArea.put(VTC_FIRST, tugDuesInVtcFirstTugArea);
    tugDuesByArea.put(VTC_SECOND, tugDuesInVtcSecondTugArea);
    tugDuesByArea.put(VTC_THIRD, tugDuesInVtcThirdTugArea);
    tugDuesByArea.put(VTC_FOURTH, tugDuesInVtcFourthTugArea);
    tugDuesByArea.put(VTC_FIFTH, tugDuesInVtcFifthTugArea);
    tugDuesByArea.put(PORTFLEET_FIRST, tugDuesInPortFleetFirstTugArea);
    tugDuesByArea.put(PORTFLEET_SECOND, tugDuesInPFSecondTugArea);
    tugDuesByArea.put(PORTFLEET_THIRD, tugDuesInPFThirdTugArea);
    tugDuesByArea.put(PORTFLEET_FOURTH, tugDuesInPFFourthTugArea);
    tugDuesByArea.put(PORTFLEET_FIFTH, tugDuesInPFFifthTugArea);

    Map<BigDecimal, Integer[]> tugCountByGrossTonnage = new TreeMap<>();
    tugCountByGrossTonnage.put(BigDecimal.valueOf(1.00), new Integer[] {150, 4499});
    tugCountByGrossTonnage.put(BigDecimal.valueOf(2.00), new Integer[] {4500, 17999});

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
    tugDueTariff.setMaximumTugCount(BigDecimal.valueOf(3.00));
  }

  private static void initializeMooringDueTariff(
      MooringDueTariff mooringDueTariff, HolidayCalendar holidayCalendar) {

    Map<MooringServiceProvider, Map<BigDecimal, Integer[]>> mooringDuesByProvider =
        new EnumMap<>(MooringServiceProvider.class);

    Map<BigDecimal, Integer[]> lesportMooringDues = new TreeMap<>();
    lesportMooringDues.put(BigDecimal.valueOf(100.00), new Integer[] {150, 3000, 40});
    lesportMooringDues.put(BigDecimal.valueOf(130.00), new Integer[] {3001, 6000, 40});
    lesportMooringDues.put(BigDecimal.valueOf(160.00), new Integer[] {6001, 8000, 40});
    lesportMooringDues.put(BigDecimal.valueOf(190.00), new Integer[] {8001, 10000, 40});

    Map<BigDecimal, Integer[]> odessosMooringDues = new TreeMap<>();
    odessosMooringDues.put(BigDecimal.valueOf(120.00), new Integer[] {150, 1000, 300});
    odessosMooringDues.put(BigDecimal.valueOf(160.00), new Integer[] {1001, 3000, 300});
    odessosMooringDues.put(BigDecimal.valueOf(240.00), new Integer[] {3001, 5000, 300});

    Map<BigDecimal, Integer[]> balchikMooringDues = new TreeMap<>();
    balchikMooringDues.put(BigDecimal.valueOf(80.00), new Integer[] {150, 3000, 160});
    balchikMooringDues.put(BigDecimal.valueOf(110.00), new Integer[] {3001, 6000, 160});
    balchikMooringDues.put(BigDecimal.valueOf(130.00), new Integer[] {6001, 8000, 160});
    balchikMooringDues.put(BigDecimal.valueOf(160.00), new Integer[] {8001, 10000, 160});

    Map<BigDecimal, Integer[]> vtcMooringDues = new TreeMap<>();
    vtcMooringDues.put(BigDecimal.valueOf(60.00), new Integer[] {150, 1000, 35});
    vtcMooringDues.put(BigDecimal.valueOf(90.00), new Integer[] {1001, 2000, 35});
    vtcMooringDues.put(BigDecimal.valueOf(120.00), new Integer[] {2001, 3000, 35});
    vtcMooringDues.put(BigDecimal.valueOf(140.00), new Integer[] {3001, 4000, 35});
    vtcMooringDues.put(BigDecimal.valueOf(160.00), new Integer[] {4001, 5000, 35});
    vtcMooringDues.put(BigDecimal.valueOf(180.00), new Integer[] {5001, 6000, 35});
    vtcMooringDues.put(BigDecimal.valueOf(200.00), new Integer[] {6001, 7000, 35});
    vtcMooringDues.put(BigDecimal.valueOf(220.00), new Integer[] {7001, 8000, 35});
    vtcMooringDues.put(BigDecimal.valueOf(230.00), new Integer[] {8001, 9000, 35});
    vtcMooringDues.put(BigDecimal.valueOf(240.00), new Integer[] {9001, 10000, 35});

    Map<BigDecimal, Integer[]> portfleetMooringDues = new TreeMap<>();
    portfleetMooringDues.put(BigDecimal.valueOf(60.00), new Integer[] {150, 1000, 35});
    portfleetMooringDues.put(BigDecimal.valueOf(90.00), new Integer[] {1001, 2000, 35});
    portfleetMooringDues.put(BigDecimal.valueOf(120.00), new Integer[] {2001, 3000, 35});
    portfleetMooringDues.put(BigDecimal.valueOf(140.00), new Integer[] {3001, 4000, 35});
    portfleetMooringDues.put(BigDecimal.valueOf(160.00), new Integer[] {4001, 5000, 35});
    portfleetMooringDues.put(BigDecimal.valueOf(180.00), new Integer[] {5001, 6000, 35});
    portfleetMooringDues.put(BigDecimal.valueOf(200.00), new Integer[] {6001, 7000, 35});
    portfleetMooringDues.put(BigDecimal.valueOf(220.00), new Integer[] {7001, 8000, 35});
    portfleetMooringDues.put(BigDecimal.valueOf(230.00), new Integer[] {8001, 9000, 35});
    portfleetMooringDues.put(BigDecimal.valueOf(240.00), new Integer[] {9001, 10000, 35});

    mooringDuesByProvider.put(MooringServiceProvider.LESPORT, lesportMooringDues);
    mooringDuesByProvider.put(MooringServiceProvider.ODESSOS, odessosMooringDues);
    mooringDuesByProvider.put(MooringServiceProvider.BALCHIK, balchikMooringDues);
    mooringDuesByProvider.put(MooringServiceProvider.VTC, vtcMooringDues);
    mooringDuesByProvider.put(MooringServiceProvider.PORTFLEET, portfleetMooringDues);

    mooringDueTariff.setMooringDuesByProvider(Collections.unmodifiableMap(mooringDuesByProvider));
    mooringDueTariff.setHolidayCalendar(holidayCalendar.getHolidayCalendar());

    mooringDueTariff.setLesportGrossTonnageThreshold(BigDecimal.valueOf(10000.00));
    mooringDueTariff.setBalchikGrossTonnageThreshold(BigDecimal.valueOf(10000.00));
    mooringDueTariff.setVtcGrossTonnageThreshold(BigDecimal.valueOf(10000.00));
    mooringDueTariff.setPortfleetGrossTonnageThreshold(BigDecimal.valueOf(10000.00));
    mooringDueTariff.setOdessosGrossTonnageThreshold(BigDecimal.valueOf(5000.00));
  }
}
