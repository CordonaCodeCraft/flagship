package flagship.domain.calculators.resolvers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import flagship.domain.calculators.tariffs.serviceduestariffs.TugDueTariff;
import flagship.domain.cases.dto.PdaCase;
import flagship.domain.cases.dto.PdaPort;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.io.IOException;
import java.util.stream.Stream;

import static flagship.domain.calculators.tariffs.serviceduestariffs.TugDueTariff.TugArea;
import static flagship.domain.calculators.tariffs.serviceduestariffs.TugDueTariff.TugArea.*;
import static flagship.domain.calculators.tariffs.serviceduestariffs.TugDueTariff.TugProvider.PORTFLEET;
import static flagship.domain.calculators.tariffs.serviceduestariffs.TugDueTariff.TugProvider.VTC;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("Tug area resolver tests")
class TugAreaResolverTest {

  private static TugDueTariff tariff;
  private PdaCase testCase;

  @BeforeAll
  public static void BeforeClass() throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    tariff = mapper.readValue(new File("src/main/resources/tugDueTariff.json"), TugDueTariff.class);
  }

  @BeforeEach
  void setUp() {
    PdaPort testPort = new PdaPort();
    testCase = PdaCase.builder().port(testPort).build();
  }

  @DisplayName("Should resolve port name to VTC first tug area")
  @ParameterizedTest(name = "port name : {arguments}")
  @MethodSource(value = "getPortsInVtcFirstTugArea")
  void shouldResolveTugAreaToVtcFirst(String portName) {

    testCase.getPort().setTugServiceProvider(VTC);
    testCase.getPort().setName(portName);

    TugArea result = TugAreaResolver.resolveTugArea(testCase, tariff);

    assertThat(result.name()).isEqualTo(VTC_FIRST.name());
  }

  @DisplayName("Should resolve port name to VTC second tug area")
  @ParameterizedTest(name = "port name : {arguments}")
  @MethodSource(value = "getPortsInVtcSecondTugArea")
  void shouldResolveTugAreaToVtcSecond(String portName) {

    testCase.getPort().setTugServiceProvider(VTC);
    testCase.getPort().setName(portName);

    TugArea result = TugAreaResolver.resolveTugArea(testCase, tariff);

    assertThat(result.name()).isEqualTo(VTC_SECOND.name());
  }

  @DisplayName("Should resolve port name to VTC third tug area")
  @ParameterizedTest(name = "port name : {arguments}")
  @MethodSource(value = "getPortsInVtcThirdTugArea")
  void shouldResolveTugAreaToVtcThird(String portName) {

    testCase.getPort().setTugServiceProvider(VTC);
    testCase.getPort().setName(portName);

    TugArea result = TugAreaResolver.resolveTugArea(testCase, tariff);

    assertThat(result.name()).isEqualTo(VTC_THIRD.name());
  }

  @DisplayName("Should resolve port name to VTC fourth tug area")
  @ParameterizedTest(name = "port name : {arguments}")
  @MethodSource(value = "getPortsInVtcFourthTugArea")
  void shouldResolveTugAreaToVtcFourth(String portName) {

    testCase.getPort().setTugServiceProvider(VTC);
    testCase.getPort().setName(portName);

    TugArea result = TugAreaResolver.resolveTugArea(testCase, tariff);

    assertThat(result.name()).isEqualTo(VTC_FOURTH.name());
  }

  @DisplayName("Should resolve port name to VTC fifth tug area")
  @ParameterizedTest(name = "port name : {arguments}")
  @MethodSource(value = "getPortsInVtcFifthTugArea")
  void shouldResolveTugAreaToVtcFifth(String portName) {

    testCase.getPort().setTugServiceProvider(VTC);
    testCase.getPort().setName(portName);

    TugArea result = TugAreaResolver.resolveTugArea(testCase, tariff);

    assertThat(result.name()).isEqualTo(VTC_FIFTH.name());
  }

  @DisplayName("Should resolve port name to Portfleet first tug area")
  @ParameterizedTest(name = "port name : {arguments}")
  @MethodSource(value = "getPortsInPortfleetFirstTugArea")
  void shouldResolveTugAreaToPortfleetFirst(String portName) {

    testCase.getPort().setTugServiceProvider(PORTFLEET);
    testCase.getPort().setName(portName);

    TugArea result = TugAreaResolver.resolveTugArea(testCase, tariff);

    assertThat(result.name()).isEqualTo(PORTFLEET_FIRST.name());
  }

  @DisplayName("Should resolve port name to Portfleet second tug area")
  @ParameterizedTest(name = "port name : {arguments}")
  @MethodSource(value = "getPortsInPortfleetSecondTugArea")
  void shouldResolveTugAreaToPortfleetSecond(String portName) {

    testCase.getPort().setTugServiceProvider(PORTFLEET);
    testCase.getPort().setName(portName);

    TugArea result = TugAreaResolver.resolveTugArea(testCase, tariff);

    assertThat(result.name()).isEqualTo(PORTFLEET_SECOND.name());
  }

  @DisplayName("Should resolve port name to Portfleet third tug area")
  @ParameterizedTest(name = "port name : {arguments}")
  @MethodSource(value = "getPortsInPortfleetThirdTugArea")
  void shouldResolveTugAreaToPortfleetThird(String portName) {

    testCase.getPort().setTugServiceProvider(PORTFLEET);
    testCase.getPort().setName(portName);

    TugArea result = TugAreaResolver.resolveTugArea(testCase, tariff);

    assertThat(result.name()).isEqualTo(PORTFLEET_THIRD.name());
  }

  @DisplayName("Should resolve port name to Portfleet fourth tug area")
  @ParameterizedTest(name = "port name : {arguments}")
  @MethodSource(value = "getPortsInPortfleetFourthTugArea")
  void shouldResolveTugAreaToPortfleetFourth(String portName) {

    testCase.getPort().setTugServiceProvider(PORTFLEET);
    testCase.getPort().setName(portName);

    TugArea result = TugAreaResolver.resolveTugArea(testCase, tariff);

    assertThat(result.name()).isEqualTo(PORTFLEET_FOURTH.name());
  }

  @DisplayName("Should resolve port name to Portfleet fifth tug area")
  @ParameterizedTest(name = "port name : {arguments}")
  @MethodSource(value = "getPortsInPortfleetFifthTugArea")
  void shouldResolveTugAreaToPortfleetFifth(String portName) {

    testCase.getPort().setTugServiceProvider(PORTFLEET);
    testCase.getPort().setName(portName);

    TugArea result = TugAreaResolver.resolveTugArea(testCase, tariff);

    assertThat(result.name()).isEqualTo(PORTFLEET_FIFTH.name());
  }

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

  private static Stream<Arguments> getStreamOfPortNames(TugDueTariff.TugProvider vtc, TugArea vtcFirst) {
    return tariff.getPortNamesInTugAreas().get(vtc).get(vtcFirst).stream().map(Arguments::of);
  }
}
