package flagship.domain.resolvers;

import flagship.domain.calculation.calculators.TariffsInitializer;
import flagship.domain.calculation.tariffs.service.TugDueTariff;
import flagship.domain.caze.model.PdaCase;
import flagship.domain.caze.model.resolver.TugAreaResolver;
import flagship.domain.port.model.PdaPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static flagship.domain.calculation.tariffs.service.TugDueTariff.TugArea;
import static flagship.domain.calculation.tariffs.service.TugDueTariff.TugArea.*;
import static flagship.domain.calculation.tariffs.service.TugDueTariff.TugServiceProvider.PORTFLEET;
import static flagship.domain.calculation.tariffs.service.TugDueTariff.TugServiceProvider.VTC;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("Tug area resolver tests")
class TugAreaResolverTest extends TariffsInitializer {

  private PdaCase testCase;

  private static Stream<Arguments> getPortsInVtcFirstTugArea() {
    return getStreamOfPortNames(VTC, VTC_FIRST);
  }

  private static Stream<Arguments> getPortsInVtcSecondTugArea() {
    return getStreamOfPortNames(VTC, VTC_SECOND);
  }

  private static Stream<Arguments> getPortsInVtcThirdTugArea() {
    return getStreamOfPortNames(VTC, VTC_THIRD);
  }

  private static Stream<Arguments> getPortsInVtcFourthTugArea() {
    return getStreamOfPortNames(VTC, VTC_FOURTH);
  }

  private static Stream<Arguments> getPortsInVtcFifthTugArea() {
    return getStreamOfPortNames(VTC, VTC_FIFTH);
  }

  private static Stream<Arguments> getPortsInPortfleetFirstTugArea() {
    return getStreamOfPortNames(PORTFLEET, PORTFLEET_FIRST);
  }

  private static Stream<Arguments> getPortsInPortfleetSecondTugArea() {
    return getStreamOfPortNames(PORTFLEET, PORTFLEET_SECOND);
  }

  private static Stream<Arguments> getPortsInPortfleetThirdTugArea() {
    return getStreamOfPortNames(PORTFLEET, PORTFLEET_THIRD);
  }

  private static Stream<Arguments> getPortsInPortfleetFourthTugArea() {
    return getStreamOfPortNames(PORTFLEET, PORTFLEET_FOURTH);
  }

  private static Stream<Arguments> getPortsInPortfleetFifthTugArea() {
    return getStreamOfPortNames(PORTFLEET, PORTFLEET_FIFTH);
  }

  private static Stream<Arguments> getStreamOfPortNames(
      final TugDueTariff.TugServiceProvider vtc, final TugArea vtcFirst) {
    return tugDueTariff.getPortNamesInTugAreas().get(vtc).get(vtcFirst).stream()
        .map(e -> e.name)
        .map(Arguments::of);
  }

  @BeforeEach
  void setUp() {
    final PdaPort testPort = new PdaPort();
    testCase = PdaCase.builder().port(testPort).build();
  }

  @DisplayName("Should resolve port name to VTC first tug area")
  @ParameterizedTest(name = "port name : {arguments}")
  @MethodSource(value = "getPortsInVtcFirstTugArea")
  void shouldResolveTugAreaToVtcFirst(final String portName) {

    testCase.getPort().setTugServiceProvider(VTC);
    testCase.getPort().setName(portName);

    final TugArea result = TugAreaResolver.resolveTugArea(testCase, tugDueTariff);

    assertThat(result.name()).isEqualTo(VTC_FIRST.name());
  }

  @DisplayName("Should resolve port name to VTC second tug area")
  @ParameterizedTest(name = "port name : {arguments}")
  @MethodSource(value = "getPortsInVtcSecondTugArea")
  void shouldResolveTugAreaToVtcSecond(final String portName) {

    testCase.getPort().setTugServiceProvider(VTC);
    testCase.getPort().setName(portName);

    final TugArea result = TugAreaResolver.resolveTugArea(testCase, tugDueTariff);

    assertThat(result.name()).isEqualTo(VTC_SECOND.name());
  }

  @DisplayName("Should resolve port name to VTC third tug area")
  @ParameterizedTest(name = "port name : {arguments}")
  @MethodSource(value = "getPortsInVtcThirdTugArea")
  void shouldResolveTugAreaToVtcThird(final String portName) {

    testCase.getPort().setTugServiceProvider(VTC);
    testCase.getPort().setName(portName);

    final TugArea result = TugAreaResolver.resolveTugArea(testCase, tugDueTariff);

    assertThat(result.name()).isEqualTo(VTC_THIRD.name());
  }

  @DisplayName("Should resolve port name to VTC fourth tug area")
  @ParameterizedTest(name = "port name : {arguments}")
  @MethodSource(value = "getPortsInVtcFourthTugArea")
  void shouldResolveTugAreaToVtcFourth(final String portName) {

    testCase.getPort().setTugServiceProvider(VTC);
    testCase.getPort().setName(portName);

    final TugArea result = TugAreaResolver.resolveTugArea(testCase, tugDueTariff);

    assertThat(result.name()).isEqualTo(VTC_FOURTH.name());
  }

  @DisplayName("Should resolve port name to VTC fifth tug area")
  @ParameterizedTest(name = "port name : {arguments}")
  @MethodSource(value = "getPortsInVtcFifthTugArea")
  void shouldResolveTugAreaToVtcFifth(final String portName) {

    testCase.getPort().setTugServiceProvider(VTC);
    testCase.getPort().setName(portName);

    final TugArea result = TugAreaResolver.resolveTugArea(testCase, tugDueTariff);

    assertThat(result.name()).isEqualTo(VTC_FIFTH.name());
  }

  @DisplayName("Should resolve port name to Portfleet first tug area")
  @ParameterizedTest(name = "port name : {arguments}")
  @MethodSource(value = "getPortsInPortfleetFirstTugArea")
  void shouldResolveTugAreaToPortfleetFirst(final String portName) {

    testCase.getPort().setTugServiceProvider(PORTFLEET);
    testCase.getPort().setName(portName);

    final TugArea result = TugAreaResolver.resolveTugArea(testCase, tugDueTariff);

    assertThat(result.name()).isEqualTo(PORTFLEET_FIRST.name());
  }

  @DisplayName("Should resolve port name to Portfleet second tug area")
  @ParameterizedTest(name = "port name : {arguments}")
  @MethodSource(value = "getPortsInPortfleetSecondTugArea")
  void shouldResolveTugAreaToPortfleetSecond(final String portName) {

    testCase.getPort().setTugServiceProvider(PORTFLEET);
    testCase.getPort().setName(portName);

    final TugArea result = TugAreaResolver.resolveTugArea(testCase, tugDueTariff);

    assertThat(result.name()).isEqualTo(PORTFLEET_SECOND.name());
  }

  @DisplayName("Should resolve port name to Portfleet third tug area")
  @ParameterizedTest(name = "port name : {arguments}")
  @MethodSource(value = "getPortsInPortfleetThirdTugArea")
  void shouldResolveTugAreaToPortfleetThird(final String portName) {

    testCase.getPort().setTugServiceProvider(PORTFLEET);
    testCase.getPort().setName(portName);

    final TugArea result = TugAreaResolver.resolveTugArea(testCase, tugDueTariff);

    assertThat(result.name()).isEqualTo(PORTFLEET_THIRD.name());
  }

  @DisplayName("Should resolve port name to Portfleet fourth tug area")
  @ParameterizedTest(name = "port name : {arguments}")
  @MethodSource(value = "getPortsInPortfleetFourthTugArea")
  void shouldResolveTugAreaToPortfleetFourth(final String portName) {

    testCase.getPort().setTugServiceProvider(PORTFLEET);
    testCase.getPort().setName(portName);

    final TugArea result = TugAreaResolver.resolveTugArea(testCase, tugDueTariff);

    assertThat(result.name()).isEqualTo(PORTFLEET_FOURTH.name());
  }

  @DisplayName("Should resolve port name to Portfleet fifth tug area")
  @ParameterizedTest(name = "port name : {arguments}")
  @MethodSource(value = "getPortsInPortfleetFifthTugArea")
  void shouldResolveTugAreaToPortfleetFifth(final String portName) {

    testCase.getPort().setTugServiceProvider(PORTFLEET);
    testCase.getPort().setName(portName);

    final TugArea result = TugAreaResolver.resolveTugArea(testCase, tugDueTariff);

    assertThat(result.name()).isEqualTo(PORTFLEET_FIFTH.name());
  }
}
