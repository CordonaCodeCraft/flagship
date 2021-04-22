package flagship.domain.utils.calculators.servicedues;

import com.fasterxml.jackson.databind.ObjectMapper;
import flagship.domain.cases.entities.Case;
import flagship.domain.cases.entities.Port;
import flagship.domain.cases.entities.enums.PilotageArea;
import flagship.domain.utils.resolvers.PilotageAreaResolver;
import flagship.domain.utils.tariffs.serviceduestariffs.PilotageAreaLookupTable;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.io.IOException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static flagship.domain.cases.entities.enums.PilotageArea.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("Pilotage area resolver tests")
class PilotageAreaResolverTest {

    private static PilotageAreaLookupTable lookupTable;
    private Case testCase;

    @BeforeAll
    public static void BeforeClass() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        lookupTable = mapper.readValue(
                new File("src/main/resources/pilotageAreaLookupTable.json"),
                PilotageAreaLookupTable.class);
    }

    @BeforeEach
    void setUp() {
        Port testPort = new Port();
        testCase = Case.builder().port(testPort).build();
    }

    @DisplayName("Should resolve port name to VARNA_FIRST pilotage area")
    @ParameterizedTest(name = "port name : {arguments}")
    @MethodSource(value = "getPortsInVarnaFirstPilotageArea")
    void shouldResolvePilotageAreaToVarnaFirst(String portName) {
        testCase.getPort().setName(portName);
        PilotageArea result = PilotageAreaResolver.resolveArea(testCase, lookupTable);
        assertThat(result.name()).isEqualTo(VARNA_FIRST.name());
    }

    @DisplayName("Should resolve port name to VARNA_SECOND pilotage area")
    @ParameterizedTest(name = "port name : {arguments}")
    @MethodSource(value = "getPortsInVarnaSecondPilotageArea")
    void shouldResolvePilotageAreaToVarnaSecond(String portName) {
        testCase.getPort().setName(portName);
        PilotageArea result = PilotageAreaResolver.resolveArea(testCase, lookupTable);
        assertThat(result.name()).isEqualTo(VARNA_SECOND.name());
    }

    @DisplayName("Should resolve port name to VARNA_THIRD pilotage area")
    @ParameterizedTest(name = "port name : {arguments}")
    @MethodSource(value = "getPortsInVarnaThirdPilotageArea")
    void shouldResolvePilotageAreaToVarnaThird(String portName) {
        testCase.getPort().setName(portName);
        PilotageArea result = PilotageAreaResolver.resolveArea(testCase, lookupTable);
        assertThat(result.name()).isEqualTo(VARNA_THIRD.name());
    }

    @DisplayName("Should resolve port name to BOURGAS_FIRST pilotage area")
    @ParameterizedTest(name = "port name : {arguments}")
    @MethodSource(value = "getPortsInBourgasFirstPilotageArea")
    void shouldResolvePilotageAreaToBourgasFirst(String portName) {
        testCase.getPort().setName(portName);
        PilotageArea result = PilotageAreaResolver.resolveArea(testCase, lookupTable);
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

    private static Stream<Arguments> getStreamOfPortNamesForPilotageArea(PilotageArea pilotageArea) {
        return lookupTable.getPortNamesInPilotageAreas().get(pilotageArea)
                .stream()
                .map(Arguments::of)
                .collect(Collectors.toList())
                .stream();
    }
}