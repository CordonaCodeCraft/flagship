package flagship.bootstrap.newinstalation;

import flagship.domain.tariffs.mix.Due;
import flagship.domain.tariffs.mix.HolidayCalendar;
import flagship.domain.tariffs.mix.PortName;
import flagship.domain.tariffs.mix.Range;
import flagship.domain.tariffs.servicedues.PilotageDueTariff;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

import static flagship.domain.PdaWarningsGenerator.WarningType;
import static flagship.domain.PdaWarningsGenerator.WarningType.*;
import static flagship.domain.tariffs.Tariff.MAX_GT;
import static flagship.domain.tariffs.Tariff.MIN_GT;
import static flagship.domain.tariffs.servicedues.PilotageDueTariff.PilotageArea;
import static flagship.domain.tariffs.servicedues.PilotageDueTariff.PilotageArea.*;

@Component
@Slf4j
@RequiredArgsConstructor
public class PilotageDueTariffInitializer {

  public static PilotageDueTariff getTariff(HolidayCalendar holidayCalendar) {

    final PilotageDueTariff pilotageDueTariff = new PilotageDueTariff();

    final Map<PilotageArea, Set<PortName>> portNamesInPilotageAreas =
        new EnumMap<>(PilotageArea.class);

    final Set<PortName> portNamesInFirstVarnaPilotageArea =
        EnumSet.of(
            PortName.VARNA_EAST,
            PortName.PETROL,
            PortName.BULYARD,
            PortName.BULPORT_LOGISTIK,
            PortName.SRY,
            PortName.PCHMV);

    final Set<PortName> portNamesInSecondVarnaPilotageArea =
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

    final Set<PortName> portNamesInThirdVarnaPilotageArea =
        EnumSet.of(PortName.VARNA_WEST, PortName.FERRY_COMPLEX);

    final Set<PortName> portNamesInFirstBourgasPilotageArea =
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

    final Map<PilotageArea, Map<Range, Due>> pilotageDuesByArea = new EnumMap<>(PilotageArea.class);

    final Map<Range, Due> varnaFirstAreaPilotageDues = new LinkedHashMap<>();

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

    final Map<Range, Due> varnaSecondAreaPilotageDues = new LinkedHashMap<>();

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

    final Map<Range, Due> varnaThirdAreaPilotageDues = new LinkedHashMap<>();

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

    final Map<Range, Due> bourgasFirstAreaPilotageDues = new LinkedHashMap<>();

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

    final Map<WarningType, BigDecimal> increaseCoefficientsByWarningType =
        new EnumMap<>(WarningType.class);

    increaseCoefficientsByWarningType.put(HOLIDAY, BigDecimal.valueOf(0.5));
    increaseCoefficientsByWarningType.put(SPECIAL_PILOT, BigDecimal.valueOf(0.5));
    increaseCoefficientsByWarningType.put(HAZARDOUS_PILOTAGE_CARGO, BigDecimal.valueOf(0.2));
    increaseCoefficientsByWarningType.put(SPECIAL_PILOTAGE_CARGO, BigDecimal.valueOf(1.0));

    pilotageDueTariff.setIncreaseCoefficientsByWarningType(
        Collections.unmodifiableMap(increaseCoefficientsByWarningType));

    pilotageDueTariff.setGrossTonnageThreshold(BigDecimal.valueOf(10000.00));

    pilotageDueTariff.setHolidayCalendar(holidayCalendar.getHolidayCalendar());

    log.info("Pilotage due tariff initialized");

    return pilotageDueTariff;
  }
}
