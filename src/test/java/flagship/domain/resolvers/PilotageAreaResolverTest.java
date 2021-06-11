package flagship.domain.resolvers;

import flagship.domain.calculation.calculators.TariffsInitializer;
import flagship.domain.calculation.tariffs.service.PilotageDueTariff;
import flagship.domain.caze.model.PdaCase;
import flagship.domain.caze.model.resolver.PilotageAreaResolver;
import flagship.domain.port.model.PdaPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static flagship.domain.calculation.tariffs.service.PilotageDueTariff.PilotageArea.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("Pilotage area resolver tests")
class PilotageAreaResolverTest extends TariffsInitializer {

  private PdaCase testCase;

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
      final PilotageDueTariff.PilotageArea pilotageArea) {
    return pilotageDueTariff.getPortNamesInPilotageAreas().get(pilotageArea).stream()
        .map(e -> e.name)
        .map(Arguments::of);
  }

  @BeforeEach
  void setUp() {
    final PdaPort testPort = new PdaPort();
    testCase = PdaCase.builder().port(testPort).build();
  }

  @DisplayName("Should resolve port name to Varna first pilotage area")
  @ParameterizedTest(name = "port name : {arguments}")
  @MethodSource(value = "getPortsInVarnaFirstPilotageArea")
  void shouldResolvePilotageAreaToVarnaFirst(final String portName) {
    testCase.getPort().setName(portName);
    final PilotageDueTariff.PilotageArea result =
        PilotageAreaResolver.resolvePilotageArea(testCase, pilotageDueTariff);
    assertThat(result.name()).isEqualTo(VARNA_FIRST.name());
  }

  @DisplayName("Should resolve port name to Varna second pilotage area")
  @ParameterizedTest(name = "port name : {arguments}")
  @MethodSource(value = "getPortsInVarnaSecondPilotageArea")
  void shouldResolvePilotageAreaToVarnaSecond(final String portName) {
    testCase.getPort().setName(portName);
    final PilotageDueTariff.PilotageArea result =
        PilotageAreaResolver.resolvePilotageArea(testCase, pilotageDueTariff);
    assertThat(result.name()).isEqualTo(VARNA_SECOND.name());
  }

  @DisplayName("Should resolve port name to Varna third pilotage area")
  @ParameterizedTest(name = "port name : {arguments}")
  @MethodSource(value = "getPortsInVarnaThirdPilotageArea")
  void shouldResolvePilotageAreaToVarnaThird(final String portName) {
    testCase.getPort().setName(portName);
    final PilotageDueTariff.PilotageArea result =
        PilotageAreaResolver.resolvePilotageArea(testCase, pilotageDueTariff);
    assertThat(result.name()).isEqualTo(VARNA_THIRD.name());
  }

  @DisplayName("Should resolve port name to Bourgas first pilotage area")
  @ParameterizedTest(name = "port name : {arguments}")
  @MethodSource(value = "getPortsInBourgasFirstPilotageArea")
  void shouldResolvePilotageAreaToBourgasFirst(final String portName) {
    testCase.getPort().setName(portName);
    final PilotageDueTariff.PilotageArea result =
        PilotageAreaResolver.resolvePilotageArea(testCase, pilotageDueTariff);
    assertThat(result.name()).isEqualTo(BOURGAS_FIRST.name());
  }
}
