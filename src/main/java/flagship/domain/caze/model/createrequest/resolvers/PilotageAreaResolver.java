package flagship.domain.caze.model.createrequest.resolvers;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

import static flagship.domain.caze.model.createrequest.resolvers.PilotageAreaResolver.PilotageArea.*;
import static flagship.domain.port.entity.Port.PortName;

public class PilotageAreaResolver {

  public static final Map<PilotageArea, Set<PortName>> PORT_NAMES_IN_PILOTAGE_AREAS =
      initializeResolver();

  public static PilotageArea resolvePilotageArea(final String portName) {
    return PORT_NAMES_IN_PILOTAGE_AREAS.entrySet().stream()
        .filter(entry -> entry.getValue().stream().anyMatch(port -> port.name.equals(portName)))
        .map(Map.Entry::getKey)
        .findFirst()
        .get();
  }

  private static Map<PilotageArea, Set<PortName>> initializeResolver() {
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

    return portNamesInPilotageAreas;
  }

  public enum PilotageArea {
    VARNA_FIRST,
    VARNA_SECOND,
    VARNA_THIRD,
    BOURGAS_FIRST,
  }
}
