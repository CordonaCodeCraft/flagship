package flagship.bootstrap;

import flagship.domain.calculators.HolidayCalendar;
import flagship.domain.calculators.resolvers.HolidayCalendarResolver;
import flagship.domain.calculators.tariffs.serviceduestariffs.PilotageDueTariff;
import flagship.domain.calculators.tariffs.serviceduestariffs.TugDueTariff;
import flagship.domain.cases.entities.enums.CargoType;
import flagship.domain.cases.entities.enums.PdaWarning;
import flagship.domain.cases.entities.enums.PilotageArea;
import flagship.domain.cases.entities.enums.TugArea;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static flagship.domain.cases.entities.enums.CargoType.HAZARDOUS;
import static flagship.domain.cases.entities.enums.CargoType.SPECIAL;
import static flagship.domain.cases.entities.enums.PdaWarning.HOLIDAY;
import static flagship.domain.cases.entities.enums.PdaWarning.PILOT;
import static flagship.domain.cases.entities.enums.PilotageArea.*;
import static flagship.domain.cases.entities.enums.TugArea.*;
import static java.time.Month.*;

@Component
public class ServiceDueTariffInitializer {

  public static void initializeTariff(
      PilotageDueTariff pilotageDueTariff,
      TugDueTariff tugDueTariff,
      HolidayCalendar holidayCalendar) {
    initializePilotageDueTariff(pilotageDueTariff);
    initializeTugDueTariff(tugDueTariff);
    initializeHolidayCalendar(holidayCalendar);
  }

  private static void initializePilotageDueTariff(PilotageDueTariff pilotageDueTariff) {

    Map<PilotageArea, List<String>> portNamesInPilotageAreas = new EnumMap<>(PilotageArea.class);

    List<String> portNamesInFirstVarnaPilotageArea = new ArrayList<>();

    portNamesInFirstVarnaPilotageArea.add("Varna East");
    portNamesInFirstVarnaPilotageArea.add("Petrol");
    portNamesInFirstVarnaPilotageArea.add("Bulyard");
    portNamesInFirstVarnaPilotageArea.add("Bulport Logistik");
    portNamesInFirstVarnaPilotageArea.add("SRY");
    portNamesInFirstVarnaPilotageArea.add("PCHVM");

    List<String> portNamesInSecondVarnaPilotageArea = new ArrayList<>();

    portNamesInSecondVarnaPilotageArea.add("TEC (Power station)");
    portNamesInSecondVarnaPilotageArea.add("Balchik port");
    portNamesInSecondVarnaPilotageArea.add("Lesport");
    portNamesInSecondVarnaPilotageArea.add("Terem FA");
    portNamesInSecondVarnaPilotageArea.add("SRY Dolphin");
    portNamesInSecondVarnaPilotageArea.add("Transstroi Varna");
    portNamesInSecondVarnaPilotageArea.add("Odessos PBM");
    portNamesInSecondVarnaPilotageArea.add("Buoy 9");
    portNamesInSecondVarnaPilotageArea.add("Anchorage");

    List<String> portNamesInThirdVarnaPilotageArea = new ArrayList<>();

    portNamesInThirdVarnaPilotageArea.add("Varna West");
    portNamesInThirdVarnaPilotageArea.add("Ferry Complex");

    List<String> portNamesInFirstBourgasPilotageArea = new ArrayList<>();

    portNamesInFirstBourgasPilotageArea.add("Burgas – Center");
    portNamesInFirstBourgasPilotageArea.add("Burgas East 2");
    portNamesInFirstBourgasPilotageArea.add("BMF Port Burgas");
    portNamesInFirstBourgasPilotageArea.add("West Terminal");
    portNamesInFirstBourgasPilotageArea.add("Ship repair yard Port Burgas");
    portNamesInFirstBourgasPilotageArea.add("Port Bulgaria West");
    portNamesInFirstBourgasPilotageArea.add("Burgas Shipyard");
    portNamesInFirstBourgasPilotageArea.add("Port Europa");
    portNamesInFirstBourgasPilotageArea.add("Transstroi Burgas");
    portNamesInFirstBourgasPilotageArea.add("Port Rosenetz");
    portNamesInFirstBourgasPilotageArea.add("Nessebar");
    portNamesInFirstBourgasPilotageArea.add("Pomorie");
    portNamesInFirstBourgasPilotageArea.add("Sozopol");
    portNamesInFirstBourgasPilotageArea.add("Tzarevo");
    portNamesInFirstBourgasPilotageArea.add("Shifting of the anchorage area");
    portNamesInFirstBourgasPilotageArea.add("deviation");
    portNamesInFirstBourgasPilotageArea.add("XX A.k.m");
    portNamesInFirstBourgasPilotageArea.add("XX B.k.m");

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

    Map<BigDecimal, Integer[]> bourgasFirstAreaPilotageDues = new HashMap<>();
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

  private static void initializeTugDueTariff(TugDueTariff tugDueTariff) {

    Map<TugArea, List<String>> portNamesInTugAreas = new EnumMap<>(TugArea.class);

    List<String> portNamesInFirstTugArea = new ArrayList<>();
    portNamesInFirstTugArea.add("Varna East");
    portNamesInFirstTugArea.add("PchMV");
    portNamesInFirstTugArea.add("Odessos PBM");
    portNamesInFirstTugArea.add("Petrol");
    portNamesInFirstTugArea.add("Lesport");
    portNamesInFirstTugArea.add("Power Station");

    List<String> portNamesInSecondTugArea = new ArrayList<>();
    portNamesInSecondTugArea.add("Varna West");
    portNamesInSecondTugArea.add("Ferry Terminal");

    List<String> portNamesInThirdTugArea = new ArrayList<>();
    portNamesInThirdTugArea.add("Bulyard");
    portNamesInThirdTugArea.add("SRY Odessos");
    portNamesInThirdTugArea.add("MTG Dolphin");
    portNamesInThirdTugArea.add("SRY TEREM");

    List<String> portNamesInFourthTugArea = new ArrayList<>();
    portNamesInFourthTugArea.add("Shifting in Bulyard");
    portNamesInFourthTugArea.add("SRY Odessos");
    portNamesInFourthTugArea.add("MTG Dolphin");
    portNamesInFourthTugArea.add("SRY TEREM");

    List<String> portNamesInFifthTugArea = new ArrayList<>();
    portNamesInFifthTugArea.add("Port Balchik");

    portNamesInTugAreas.put(VTC_FIRST, portNamesInFirstTugArea);
    portNamesInTugAreas.put(VTC_SECOND, portNamesInSecondTugArea);
    portNamesInTugAreas.put(VTC_THIRD, portNamesInThirdTugArea);
    portNamesInTugAreas.put(VTC_FOURTH, portNamesInFourthTugArea);
    portNamesInTugAreas.put(VTC_FIFTH, portNamesInFifthTugArea);

    tugDueTariff.setPortNamesInTugAreas(Collections.unmodifiableMap(portNamesInTugAreas));
  }
}
