package flagship.domain.caze.model.createrequest.resolvers;

import flagship.domain.caze.model.createrequest.CreateCaseRequest;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

import static flagship.domain.caze.model.createrequest.resolvers.TugAreaResolver.TugArea.*;
import static flagship.domain.caze.model.createrequest.resolvers.TugAreaResolver.TugServiceProvider.PORTFLEET;
import static flagship.domain.caze.model.createrequest.resolvers.TugAreaResolver.TugServiceProvider.VTC;
import static flagship.domain.port.entity.Port.PortName;
import static flagship.domain.port.entity.Port.PortName.*;

public class TugAreaResolver {

  public static final Map<TugServiceProvider, Map<TugArea, Set<PortName>>> PORT_NAMES_IN_TUG_AREAS =
      initializeResolver();

  public static TugArea resolveTugArea(final CreateCaseRequest source) {
    return PORT_NAMES_IN_TUG_AREAS.get(source.getTugServiceProvider()).entrySet().stream()
        .filter(
            entry ->
                entry.getValue().stream().anyMatch(port -> port.name.equals(source.getPortName())))
        .map(Map.Entry::getKey)
        .findFirst()
        .get();
  }

  private static Map<TugServiceProvider, Map<TugArea, Set<PortName>>> initializeResolver() {

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
    return portNamesInTugAreas;
  }

  public enum TugArea {
    VTC_FIRST,
    VTC_SECOND,
    VTC_THIRD,
    VTC_FOURTH,
    VTC_FIFTH,
    PORTFLEET_FIRST,
    PORTFLEET_SECOND,
    PORTFLEET_THIRD,
    PORTFLEET_FOURTH,
    PORTFLEET_FIFTH,
  }

  public enum TugServiceProvider {
    VTC,
    PORTFLEET,
  }
}
