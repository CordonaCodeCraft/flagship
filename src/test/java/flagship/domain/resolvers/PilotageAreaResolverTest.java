package flagship.domain.resolvers;

import flagship.domain.caze.model.createrequest.CreateCaseRequest;
import flagship.domain.caze.model.createrequest.resolvers.PilotageAreaResolver;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static flagship.domain.caze.model.createrequest.resolvers.PilotageAreaResolver.PilotageArea;
import static flagship.domain.caze.model.createrequest.resolvers.PilotageAreaResolver.PilotageArea.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("Pilotage area resolver tests")
class PilotageAreaResolverTest {

  private final CreateCaseRequest testRequest = CreateCaseRequest.builder().build();

  @DisplayName("Should resolve port name to Varna first pilotage area")
  @ParameterizedTest(name = "port name : {arguments}")
  @MethodSource(value = "getPortsInVarnaFirstPilotageArea")
  void shouldResolvePilotageAreaToVarnaFirst(final String portName) {
    final PilotageArea result = PilotageAreaResolver.resolvePilotageArea(portName);
    assertThat(result.name()).isEqualTo(VARNA_FIRST.name());
  }

  @DisplayName("Should resolve port name to Varna second pilotage area")
  @ParameterizedTest(name = "port name : {arguments}")
  @MethodSource(value = "getPortsInVarnaSecondPilotageArea")
  void shouldResolvePilotageAreaToVarnaSecond(final String portName) {
    final PilotageArea result = PilotageAreaResolver.resolvePilotageArea(portName);
    assertThat(result.name()).isEqualTo(VARNA_SECOND.name());
  }

  @DisplayName("Should resolve port name to Varna third pilotage area")
  @ParameterizedTest(name = "port name : {arguments}")
  @MethodSource(value = "getPortsInVarnaThirdPilotageArea")
  void shouldResolvePilotageAreaToVarnaThird(final String portName) {
    final PilotageArea result = PilotageAreaResolver.resolvePilotageArea(portName);
    assertThat(result.name()).isEqualTo(VARNA_THIRD.name());
  }

  @DisplayName("Should resolve port name to Bourgas first pilotage area")
  @ParameterizedTest(name = "port name : {arguments}")
  @MethodSource(value = "getPortsInBourgasFirstPilotageArea")
  void shouldResolvePilotageAreaToBourgasFirst(final String portName) {
    final PilotageArea result = PilotageAreaResolver.resolvePilotageArea(portName);
    assertThat(result.name()).isEqualTo(BOURGAS_FIRST.name());
  }

  private static Stream<Arguments> getPortsInVarnaFirstPilotageArea() {
    return getStreamOfPortNamesForPilotageArea(VARNA_FIRST);
  }

  private static Stream<Arguments> getPortsInVarnaSecondPilotageArea() {
    return getStreamOfPortNamesForPilotageArea(VARNA_SECOND);
  }

  private static Stream<Arguments> getPortsInVarnaThirdPilotageArea() {
    return getStreamOfPortNamesForPilotageArea(VARNA_THIRD);
  }

  private static Stream<Arguments> getPortsInBourgasFirstPilotageArea() {
    return getStreamOfPortNamesForPilotageArea(BOURGAS_FIRST);
  }

  private static Stream<Arguments> getStreamOfPortNamesForPilotageArea(
      final PilotageArea pilotageArea) {
    return PilotageAreaResolver.PORT_NAMES_IN_PILOTAGE_AREAS.get(pilotageArea).stream()
        .map(port -> port.name)
        .map(Arguments::of);
  }
}
