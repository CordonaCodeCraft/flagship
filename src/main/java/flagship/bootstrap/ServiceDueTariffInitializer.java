package flagship.bootstrap;

import flagship.domain.calculators.HolidayCalendar;
import flagship.domain.calculators.resolvers.HolidayCalendarResolver;
import flagship.domain.calculators.tariffs.enums.PdaWarning;
import flagship.domain.calculators.tariffs.serviceduestariffs.PilotageDueTariff;
import flagship.domain.calculators.tariffs.serviceduestariffs.PilotageDueTariff.PilotageArea;
import flagship.domain.calculators.tariffs.serviceduestariffs.TugDueTariff;
import flagship.domain.cases.entities.enums.CargoType;
import flagship.domain.calculators.tariffs.enums.PortName;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static flagship.domain.calculators.tariffs.enums.PdaWarning.HOLIDAY;
import static flagship.domain.calculators.tariffs.enums.PdaWarning.PILOT;
import static flagship.domain.calculators.tariffs.serviceduestariffs.PilotageDueTariff.PilotageArea.*;
import static flagship.domain.calculators.tariffs.serviceduestariffs.TugDueTariff.TugArea;
import static flagship.domain.calculators.tariffs.serviceduestariffs.TugDueTariff.TugArea.*;
import static flagship.domain.calculators.tariffs.serviceduestariffs.TugDueTariff.TugProvider;
import static flagship.domain.calculators.tariffs.serviceduestariffs.TugDueTariff.TugProvider.PORTFLEET;
import static flagship.domain.calculators.tariffs.serviceduestariffs.TugDueTariff.TugProvider.VTC;
import static flagship.domain.cases.entities.enums.CargoType.HAZARDOUS;
import static flagship.domain.cases.entities.enums.CargoType.SPECIAL;
import static flagship.domain.calculators.tariffs.enums.PortName.*;
import static java.time.Month.*;

@Component
public class ServiceDueTariffInitializer {

  public static void initializeTariff(
      PilotageDueTariff pilotageDueTariff,
      TugDueTariff tugDueTariff,
      HolidayCalendar holidayCalendar) {
    initializeHolidayCalendar(holidayCalendar);
    initializePilotageDueTariff(pilotageDueTariff, holidayCalendar);
    initializeTugDueTariff(tugDueTariff, holidayCalendar);
  }

  private static void initializePilotageDueTariff(
      PilotageDueTariff pilotageDueTariff, HolidayCalendar holidayCalendar) {

    Map<PilotageArea, Set<PortName>> portNamesInPilotageAreas = new EnumMap<>(PilotageArea.class);

    Set<PortName> portNamesInFirstVarnaPilotageArea =
        EnumSet.of(VARNA_EAST, PETROL, BULYARD, BULPORT_LOGISTIK, SRY, PCHMV);

    Set<PortName> portNamesInSecondVarnaPilotageArea =
        EnumSet.of(
            TEC_POWER_STATION,
            BALCHIK_PORT,
            LESPORT,
            TEREM_FA,
            SRY_DOLPHIN,
            TRANSSTROI_VARNA,
            ODESSOS_PBM,
            BUOY_9,
            ANCHORAGE);

    Set<PortName> portNamesInThirdVarnaPilotageArea = EnumSet.of(VARNA_WEST, FERRY_COMPLEX);

    Set<PortName> portNamesInFirstBourgasPilotageArea =
        EnumSet.of(
            BOURGAS_CENTER,
            BOURGAS_EAST_2,
            BMF_PORT_BOURGAS,
            WEST_TERMINAL,
            SRY_PORT_BOURGAS,
            PORT_BULGARIA_WEST,
            BOURGAS_SHIPYARD,
            PORT_EUROPA,
            TRANSSTROI_BOURGAS,
            PORT_ROSENETZ,
            NESSEBAR,
            POMORIE,
            SOZOPOL,
            TZAREVO,
            SHIFTING_ANCHORAGE_AREA,
            DEVIATION,
            XX_A_K_M,
            XX_B_K_M);

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

    Map<TugProvider, Map<TugArea, Set<PortName>>> portNamesInTugAreas =
        new EnumMap<>(TugProvider.class);

    Map<TugArea, Set<PortName>> portNamesInVtcTugAreas = new EnumMap<>(TugArea.class);

    Set<PortName> portNamesInVtcFirstTugArea =
        EnumSet.of(VARNA_EAST, PCHMV, ODESSOS_PBM, PETROL, LESPORT, TEC_POWER_STATION);

    Set<PortName> portNamesInVtcSecondTugArea = EnumSet.of(VARNA_WEST, FERRY_COMPLEX);

    Set<PortName> portNamesInVtcThirdTugArea =
        EnumSet.of(BULYARD, SRY_ODESSOS, MTG_DOLPHIN, TEREM_FA);

    Set<PortName> portNamesInVtcFourthTugArea =
        EnumSet.of(
            SHIFTING_BULYARD, SHIFTING_SRY_ODESSOS, SHIFTING_MTG_DOLPHIN, SHIFTING_TEREM_FA);

    Set<PortName> portNamesInVtcFifthTugArea = EnumSet.of(BALCHIK_PORT);

    portNamesInVtcTugAreas.put(VTC_FIRST, portNamesInVtcFirstTugArea);
    portNamesInVtcTugAreas.put(VTC_SECOND, portNamesInVtcSecondTugArea);
    portNamesInVtcTugAreas.put(VTC_THIRD, portNamesInVtcThirdTugArea);
    portNamesInVtcTugAreas.put(VTC_FOURTH, portNamesInVtcFourthTugArea);
    portNamesInVtcTugAreas.put(VTC_FIFTH, portNamesInVtcFifthTugArea);

    Map<TugArea, Set<PortName>> portNamesInPortFleetTugAreas = new EnumMap<>(TugArea.class);

    Set<PortName> portNamesInPortfleetFirstTugArea =
        EnumSet.of(VARNA_EAST, TEC_EZEROVO, PETROL, LESPORT, ODESSOS_PBM, PCHMV);

    Set<PortName> portNamesInPortfleetSecondTugArea = EnumSet.of(VARNA_WEST, FERRY_COMPLEX);

    Set<PortName> portNamesInPortfleetThirdTugArea =
        EnumSet.of(BULYARD, SRY_ODESSOS, MTG_DOLPHIN, TEREM_FA);

    Set<PortName> portNamesInPortfleetFourthTugArea =
        EnumSet.of(
            SHIFTING_BULYARD, SHIFTING_SRY_ODESSOS, SHIFTING_MTG_DOLPHIN, SHIFTING_TEREM_FA);

    Set<PortName> portNamesInPortfleetFifthTugArea = EnumSet.of(BALCHIK_PORT);

    portNamesInPortFleetTugAreas.put(PORTFLEET_FIRST, portNamesInPortfleetFirstTugArea);
    portNamesInPortFleetTugAreas.put(PORTFLEET_SECOND, portNamesInPortfleetSecondTugArea);
    portNamesInPortFleetTugAreas.put(PORTFLEET_THIRD, portNamesInPortfleetThirdTugArea);
    portNamesInPortFleetTugAreas.put(PORTFLEET_FOURTH, portNamesInPortfleetFourthTugArea);
    portNamesInPortFleetTugAreas.put(PORTFLEET_FIFTH, portNamesInPortfleetFifthTugArea);

    portNamesInTugAreas.put(VTC, portNamesInVtcTugAreas);
    portNamesInTugAreas.put(PORTFLEET, portNamesInPortFleetTugAreas);

    Map<TugArea, Map<BigDecimal, Integer[]>> tugDuesByArea = new EnumMap<>(TugArea.class);

    Map<BigDecimal, Integer[]> tugDuesInVtcFirstTugArea = new TreeMap<>();
    tugDuesInVtcFirstTugArea.put(BigDecimal.valueOf(300), new Integer[] {150, 1000, 45});
    tugDuesInVtcFirstTugArea.put(BigDecimal.valueOf(410), new Integer[] {1001, 2000, 45});
    tugDuesInVtcFirstTugArea.put(BigDecimal.valueOf(520), new Integer[] {2001, 2499, 45});
    tugDuesInVtcFirstTugArea.put(BigDecimal.valueOf(820), new Integer[] {2500, 3000, 55});
    tugDuesInVtcFirstTugArea.put(BigDecimal.valueOf(1020), new Integer[] {3001, 4000, 55});
    tugDuesInVtcFirstTugArea.put(BigDecimal.valueOf(1220), new Integer[] {4001, 5000, 55});
    tugDuesInVtcFirstTugArea.put(BigDecimal.valueOf(1420), new Integer[] {5001, 6000, 55});
    tugDuesInVtcFirstTugArea.put(BigDecimal.valueOf(1620), new Integer[] {6001, 7000, 55});
    tugDuesInVtcFirstTugArea.put(BigDecimal.valueOf(1820), new Integer[] {7001, 8000, 55});
    tugDuesInVtcFirstTugArea.put(BigDecimal.valueOf(2020), new Integer[] {8001, 9000, 55});
    tugDuesInVtcFirstTugArea.put(BigDecimal.valueOf(2220), new Integer[] {9001, 10000, 55});

    Map<BigDecimal, Integer[]> tugDuesInVtcSecondTugArea = new TreeMap<>();
    tugDuesInVtcSecondTugArea.put(BigDecimal.valueOf(300), new Integer[] {150, 1000, 45});
    tugDuesInVtcSecondTugArea.put(BigDecimal.valueOf(410), new Integer[] {1001, 2000, 45});
    tugDuesInVtcSecondTugArea.put(BigDecimal.valueOf(520), new Integer[] {2001, 2499, 45});
    tugDuesInVtcSecondTugArea.put(BigDecimal.valueOf(1060), new Integer[] {2500, 3000, 65});
    tugDuesInVtcSecondTugArea.put(BigDecimal.valueOf(1320), new Integer[] {3001, 4000, 65});
    tugDuesInVtcSecondTugArea.put(BigDecimal.valueOf(1580), new Integer[] {4001, 5000, 65});
    tugDuesInVtcSecondTugArea.put(BigDecimal.valueOf(1840), new Integer[] {5001, 6000, 65});
    tugDuesInVtcSecondTugArea.put(BigDecimal.valueOf(2100), new Integer[] {6001, 7000, 65});
    tugDuesInVtcSecondTugArea.put(BigDecimal.valueOf(2360), new Integer[] {7001, 8000, 65});
    tugDuesInVtcSecondTugArea.put(BigDecimal.valueOf(2620), new Integer[] {8001, 9000, 65});
    tugDuesInVtcSecondTugArea.put(BigDecimal.valueOf(2880), new Integer[] {9001, 10000, 65});

    Map<BigDecimal, Integer[]> tugDuesInVtcThirdTugArea = new TreeMap<>();
    tugDuesInVtcThirdTugArea.put(BigDecimal.valueOf(300), new Integer[] {150, 1000, 45});
    tugDuesInVtcThirdTugArea.put(BigDecimal.valueOf(410), new Integer[] {1001, 2000, 45});
    tugDuesInVtcThirdTugArea.put(BigDecimal.valueOf(520), new Integer[] {2001, 2499, 45});
    tugDuesInVtcThirdTugArea.put(BigDecimal.valueOf(720), new Integer[] {2500, 3000, 50});
    tugDuesInVtcThirdTugArea.put(BigDecimal.valueOf(870), new Integer[] {3001, 4000, 50});
    tugDuesInVtcThirdTugArea.put(BigDecimal.valueOf(1020), new Integer[] {4001, 5000, 50});
    tugDuesInVtcThirdTugArea.put(BigDecimal.valueOf(1170), new Integer[] {5001, 6000, 50});
    tugDuesInVtcThirdTugArea.put(BigDecimal.valueOf(1320), new Integer[] {6001, 7000, 50});
    tugDuesInVtcThirdTugArea.put(BigDecimal.valueOf(1470), new Integer[] {7001, 8000, 50});
    tugDuesInVtcThirdTugArea.put(BigDecimal.valueOf(1620), new Integer[] {8001, 9000, 50});
    tugDuesInVtcThirdTugArea.put(BigDecimal.valueOf(1770), new Integer[] {9001, 10000, 50});

    Map<BigDecimal, Integer[]> tugDuesInVtcFourthTugArea = new TreeMap<>();
    tugDuesInVtcFourthTugArea.put(BigDecimal.valueOf(300), new Integer[] {150, 1000, 45});
    tugDuesInVtcFourthTugArea.put(BigDecimal.valueOf(410), new Integer[] {1001, 2000, 45});
    tugDuesInVtcFourthTugArea.put(BigDecimal.valueOf(520), new Integer[] {2001, 2499, 45});
    tugDuesInVtcFourthTugArea.put(BigDecimal.valueOf(650), new Integer[] {2500, 3000, 50});
    tugDuesInVtcFourthTugArea.put(BigDecimal.valueOf(780), new Integer[] {3001, 4000, 50});
    tugDuesInVtcFourthTugArea.put(BigDecimal.valueOf(910), new Integer[] {4001, 5000, 50});
    tugDuesInVtcFourthTugArea.put(BigDecimal.valueOf(1040), new Integer[] {5001, 6000, 50});
    tugDuesInVtcFourthTugArea.put(BigDecimal.valueOf(1170), new Integer[] {6001, 7000, 50});
    tugDuesInVtcFourthTugArea.put(BigDecimal.valueOf(1300), new Integer[] {7001, 8000, 50});
    tugDuesInVtcFourthTugArea.put(BigDecimal.valueOf(1430), new Integer[] {8001, 9000, 50});
    tugDuesInVtcFourthTugArea.put(BigDecimal.valueOf(1560), new Integer[] {9001, 10000, 50});

    Map<BigDecimal, Integer[]> tugDuesInVtcFifthTugArea = new TreeMap<>();
    tugDuesInVtcFifthTugArea.put(BigDecimal.valueOf(430), new Integer[] {150, 1000, 65});
    tugDuesInVtcFifthTugArea.put(BigDecimal.valueOf(560), new Integer[] {1001, 2000, 65});
    tugDuesInVtcFifthTugArea.put(BigDecimal.valueOf(690), new Integer[] {2001, 2499, 65});
    tugDuesInVtcFifthTugArea.put(BigDecimal.valueOf(1060), new Integer[] {2500, 3000, 65});
    tugDuesInVtcFifthTugArea.put(BigDecimal.valueOf(1320), new Integer[] {3001, 4000, 65});
    tugDuesInVtcFifthTugArea.put(BigDecimal.valueOf(1580), new Integer[] {4001, 5000, 65});
    tugDuesInVtcFifthTugArea.put(BigDecimal.valueOf(1840), new Integer[] {5001, 6000, 65});
    tugDuesInVtcFifthTugArea.put(BigDecimal.valueOf(2100), new Integer[] {6001, 7000, 65});
    tugDuesInVtcFifthTugArea.put(BigDecimal.valueOf(2360), new Integer[] {7001, 8000, 65});
    tugDuesInVtcFifthTugArea.put(BigDecimal.valueOf(2620), new Integer[] {8001, 9000, 65});
    tugDuesInVtcFifthTugArea.put(BigDecimal.valueOf(2880), new Integer[] {9001, 10000, 65});

    Map<BigDecimal, Integer[]> tugDuesInPortFleetFirstTugArea = new TreeMap<>();
    tugDuesInPortFleetFirstTugArea.put(BigDecimal.valueOf(300), new Integer[] {150, 1000, 45});
    tugDuesInPortFleetFirstTugArea.put(BigDecimal.valueOf(410), new Integer[] {1001, 2000, 45});
    tugDuesInPortFleetFirstTugArea.put(BigDecimal.valueOf(520), new Integer[] {2001, 2499, 45});
    tugDuesInPortFleetFirstTugArea.put(BigDecimal.valueOf(820), new Integer[] {2500, 3000, 55});
    tugDuesInPortFleetFirstTugArea.put(BigDecimal.valueOf(1020), new Integer[] {3001, 4000, 55});
    tugDuesInPortFleetFirstTugArea.put(BigDecimal.valueOf(1220), new Integer[] {4001, 5000, 55});
    tugDuesInPortFleetFirstTugArea.put(BigDecimal.valueOf(1420), new Integer[] {5001, 6000, 55});
    tugDuesInPortFleetFirstTugArea.put(BigDecimal.valueOf(1620), new Integer[] {6001, 7000, 55});
    tugDuesInPortFleetFirstTugArea.put(BigDecimal.valueOf(1820), new Integer[] {7001, 8000, 55});
    tugDuesInPortFleetFirstTugArea.put(BigDecimal.valueOf(2020), new Integer[] {8001, 9000, 55});
    tugDuesInPortFleetFirstTugArea.put(BigDecimal.valueOf(2220), new Integer[] {9001, 10000, 55});

    Map<BigDecimal, Integer[]> tugDuesInPortFleetSecondTugArea = new TreeMap<>();
    tugDuesInPortFleetSecondTugArea.put(BigDecimal.valueOf(300), new Integer[] {150, 1000, 45});
    tugDuesInPortFleetSecondTugArea.put(BigDecimal.valueOf(410), new Integer[] {1001, 2000, 45});
    tugDuesInPortFleetSecondTugArea.put(BigDecimal.valueOf(520), new Integer[] {2001, 2499, 45});
    tugDuesInPortFleetSecondTugArea.put(BigDecimal.valueOf(1060), new Integer[] {2500, 3000, 65});
    tugDuesInPortFleetSecondTugArea.put(BigDecimal.valueOf(1320), new Integer[] {3001, 4000, 65});
    tugDuesInPortFleetSecondTugArea.put(BigDecimal.valueOf(1580), new Integer[] {4001, 5000, 65});
    tugDuesInPortFleetSecondTugArea.put(BigDecimal.valueOf(1840), new Integer[] {5001, 6000, 65});
    tugDuesInPortFleetSecondTugArea.put(BigDecimal.valueOf(2100), new Integer[] {6001, 7000, 65});
    tugDuesInPortFleetSecondTugArea.put(BigDecimal.valueOf(2360), new Integer[] {7001, 8000, 65});
    tugDuesInPortFleetSecondTugArea.put(BigDecimal.valueOf(2620), new Integer[] {8001, 9000, 65});
    tugDuesInPortFleetSecondTugArea.put(BigDecimal.valueOf(2880), new Integer[] {9001, 10000, 65});

    Map<BigDecimal, Integer[]> tugDuesInPortFleetThirdTugArea = new TreeMap<>();
    tugDuesInPortFleetThirdTugArea.put(BigDecimal.valueOf(300), new Integer[] {150, 1000, 45});
    tugDuesInPortFleetThirdTugArea.put(BigDecimal.valueOf(410), new Integer[] {1001, 2000, 45});
    tugDuesInPortFleetThirdTugArea.put(BigDecimal.valueOf(520), new Integer[] {2001, 2499, 45});
    tugDuesInPortFleetThirdTugArea.put(BigDecimal.valueOf(720), new Integer[] {2500, 3000, 50});
    tugDuesInPortFleetThirdTugArea.put(BigDecimal.valueOf(870), new Integer[] {3001, 4000, 50});
    tugDuesInPortFleetThirdTugArea.put(BigDecimal.valueOf(1020), new Integer[] {4001, 5000, 50});
    tugDuesInPortFleetThirdTugArea.put(BigDecimal.valueOf(1170), new Integer[] {5001, 6000, 50});
    tugDuesInPortFleetThirdTugArea.put(BigDecimal.valueOf(1320), new Integer[] {6001, 7000, 50});
    tugDuesInPortFleetThirdTugArea.put(BigDecimal.valueOf(1470), new Integer[] {7001, 8000, 50});
    tugDuesInPortFleetThirdTugArea.put(BigDecimal.valueOf(1620), new Integer[] {8001, 9000, 50});
    tugDuesInPortFleetThirdTugArea.put(BigDecimal.valueOf(1770), new Integer[] {9001, 10000, 50});

    Map<BigDecimal, Integer[]> tugDuesInPortFleetFourthTugArea = new TreeMap<>();
    tugDuesInPortFleetFourthTugArea.put(BigDecimal.valueOf(300), new Integer[] {150, 1000, 45});
    tugDuesInPortFleetFourthTugArea.put(BigDecimal.valueOf(410), new Integer[] {1001, 2000, 45});
    tugDuesInPortFleetFourthTugArea.put(BigDecimal.valueOf(520), new Integer[] {2001, 2499, 45});
    tugDuesInPortFleetFourthTugArea.put(BigDecimal.valueOf(650), new Integer[] {2500, 3000, 50});
    tugDuesInPortFleetFourthTugArea.put(BigDecimal.valueOf(780), new Integer[] {3001, 4000, 50});
    tugDuesInPortFleetFourthTugArea.put(BigDecimal.valueOf(910), new Integer[] {4001, 5000, 50});
    tugDuesInPortFleetFourthTugArea.put(BigDecimal.valueOf(1040), new Integer[] {5001, 6000, 50});
    tugDuesInPortFleetFourthTugArea.put(BigDecimal.valueOf(1170), new Integer[] {6001, 7000, 50});
    tugDuesInPortFleetFourthTugArea.put(BigDecimal.valueOf(1300), new Integer[] {7001, 8000, 50});
    tugDuesInPortFleetFourthTugArea.put(BigDecimal.valueOf(1430), new Integer[] {8001, 9000, 50});
    tugDuesInPortFleetFourthTugArea.put(BigDecimal.valueOf(1560), new Integer[] {9001, 10000, 50});

    Map<BigDecimal, Integer[]> tugDuesInPortFleetFifthTugArea = new TreeMap<>();
    tugDuesInPortFleetFifthTugArea.put(BigDecimal.valueOf(430), new Integer[] {150, 1000, 65});
    tugDuesInPortFleetFifthTugArea.put(BigDecimal.valueOf(560), new Integer[] {1001, 2000, 65});
    tugDuesInPortFleetFifthTugArea.put(BigDecimal.valueOf(690), new Integer[] {2001, 2499, 65});
    tugDuesInPortFleetFifthTugArea.put(BigDecimal.valueOf(1060), new Integer[] {2500, 3000, 65});
    tugDuesInPortFleetFifthTugArea.put(BigDecimal.valueOf(1320), new Integer[] {3001, 4000, 65});
    tugDuesInPortFleetFifthTugArea.put(BigDecimal.valueOf(1580), new Integer[] {4001, 5000, 65});
    tugDuesInPortFleetFifthTugArea.put(BigDecimal.valueOf(1840), new Integer[] {5001, 6000, 65});
    tugDuesInPortFleetFifthTugArea.put(BigDecimal.valueOf(2100), new Integer[] {6001, 7000, 65});
    tugDuesInPortFleetFifthTugArea.put(BigDecimal.valueOf(2360), new Integer[] {7001, 8000, 65});
    tugDuesInPortFleetFifthTugArea.put(BigDecimal.valueOf(2620), new Integer[] {8001, 9000, 65});
    tugDuesInPortFleetFifthTugArea.put(BigDecimal.valueOf(2880), new Integer[] {9001, 10000, 65});

    tugDuesByArea.put(VTC_FIRST, tugDuesInVtcFirstTugArea);
    tugDuesByArea.put(VTC_SECOND, tugDuesInVtcSecondTugArea);
    tugDuesByArea.put(VTC_THIRD, tugDuesInVtcThirdTugArea);
    tugDuesByArea.put(VTC_FOURTH, tugDuesInVtcFourthTugArea);
    tugDuesByArea.put(VTC_FIFTH, tugDuesInVtcFifthTugArea);
    tugDuesByArea.put(PORTFLEET_FIRST, tugDuesInPortFleetFirstTugArea);
    tugDuesByArea.put(PORTFLEET_SECOND, tugDuesInPortFleetSecondTugArea);
    tugDuesByArea.put(PORTFLEET_THIRD, tugDuesInPortFleetThirdTugArea);
    tugDuesByArea.put(PORTFLEET_FOURTH, tugDuesInPortFleetFourthTugArea);
    tugDuesByArea.put(PORTFLEET_FIFTH, tugDuesInPortFleetFifthTugArea);

    Map<BigDecimal, Integer[]> tugCountByGrossTonnage = new TreeMap<>();
    tugCountByGrossTonnage.put(BigDecimal.valueOf(1), new Integer[] {150, 4499});
    tugCountByGrossTonnage.put(BigDecimal.valueOf(2), new Integer[] {4500, 17999});

    Map<PdaWarning, BigDecimal> increaseCoefficientsByWarningType = new EnumMap<>(PdaWarning.class);
    increaseCoefficientsByWarningType.put(HOLIDAY, BigDecimal.valueOf(1.0));

    tugDueTariff.setPortNamesInTugAreas(Collections.unmodifiableMap(portNamesInTugAreas));
    tugDueTariff.setTugDuesByArea(Collections.unmodifiableMap(tugDuesByArea));
    tugDueTariff.setTugCountByGrossTonnage(Collections.unmodifiableMap(tugCountByGrossTonnage));
    tugDueTariff.setIncreaseCoefficientsByWarningType(
        Collections.unmodifiableMap(increaseCoefficientsByWarningType));

    tugDueTariff.setHolidayCalendar(holidayCalendar.getHolidayCalendar());
    tugDueTariff.setGrossTonnageThreshold(BigDecimal.valueOf(10000.00));
    tugDueTariff.setGrossTonnageThresholdForTugCountReduce(BigDecimal.valueOf(18000));
    tugDueTariff.setMaximumTugCount(BigDecimal.valueOf(3));
  }
}
