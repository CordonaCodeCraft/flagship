package flagship.domain.caze.composer;

import flagship.domain.calculation.tariffs.TariffsFactory;
import flagship.domain.caze.model.PdaCase;
import flagship.domain.caze.model.request.CreateCaseRequest;
import flagship.domain.caze.model.request.resolvers.MooringServiceProviderResolver;
import flagship.domain.caze.model.request.resolvers.PilotageAreaResolver;
import flagship.domain.caze.model.request.resolvers.PortAreaResolver;
import flagship.domain.pda.composer.PdaComposer;
import flagship.domain.port.model.PdaPort;
import flagship.domain.ship.model.PdaShip;
import flagship.domain.warning.generator.WarningsGenerator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static flagship.domain.caze.entity.Case.CallPurpose;
import static flagship.domain.caze.model.request.resolvers.TugAreaResolver.resolveTugArea;
import static flagship.domain.ship.entity.Ship.ShipType;
import static flagship.domain.warning.generator.WarningsGenerator.WarningType;

public class PdaCaseComposer {

  private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

  public static PdaCase composePdaCase(
      final CreateCaseRequest source, final TariffsFactory tariffsFactory) {

    final PdaCase pdaCase =
        PdaCase.builder()
            .ship(composeShip(source))
            .port(composePort(source))
            .warningTypes(getWarningTypes(source))
            .cargoManifest(source.getCargoManifest())
            .callPurpose(getCallPurpose(source))
            .callCount(source.getCallCount())
            .alongsideDaysExpected(source.getAlongsideDaysExpected())
            .estimatedDateOfArrival(getEta(source))
            .estimatedDateOfDeparture(getEtd(source))
            .arrivesFromBulgarianPort(source.getArrivesFromBulgarianPort())
            .clientDiscountCoefficient(source.getClientDiscountCoefficient())
            .build();

    final WarningsGenerator warningsGenerator = new WarningsGenerator(pdaCase, tariffsFactory);

    pdaCase.setWarnings(warningsGenerator.generateWarnings());

    final PdaComposer pdaComposer = new PdaComposer();
    pdaComposer.setSource(pdaCase);
    pdaComposer.setTariffsFactory(tariffsFactory);
    pdaComposer.setCommissionCoefficient(source.getAgencyCommissionCoefficient());

    pdaCase.setProformaDisbursementAccount(pdaComposer.composePda());

    return pdaCase;
  }

  private static PdaShip composeShip(final CreateCaseRequest source) {
    return PdaShip.builder()
        .name(source.getShipName())
        .type(getShipType(source.getShipType()))
        .lengthOverall(source.getShipLengthOverall())
        .grossTonnage(source.getShipGrossTonnage())
        .hasIncreasedManeuverability(source.getShipHasIncreasedManeuverability())
        .build();
  }

  private static PdaPort composePort(final CreateCaseRequest source) {

    final String portName = source.getPortName();

    return PdaPort.builder()
        .name(portName)
        .portArea(PortAreaResolver.resolvePortArea(portName))
        .pilotageArea(PilotageAreaResolver.resolvePilotageArea(portName))
        .tugArea(resolveTugArea(source))
        .tugServiceProvider(source.getTugServiceProvider())
        .mooringServiceProvider(
            MooringServiceProviderResolver.resolveMooringServiceProvider(portName))
        .build();
  }

  private static CallPurpose getCallPurpose(final CreateCaseRequest source) {
    return Arrays.stream(CallPurpose.values())
        .filter(purpose -> purpose.type.equals(source.getCallPurpose()))
        .findFirst()
        .orElse(null);
  }

  private static LocalDate getEta(final CreateCaseRequest source) {
    if (source.getEstimatedDateOfArrival().equals("ETA not provided")) {
      return null;
    } else {
      return LocalDate.parse(source.getEstimatedDateOfArrival(), formatter);
    }
  }

  private static LocalDate getEtd(final CreateCaseRequest source) {
    if (source.getEstimatedDateOfDeparture().equals("ETD not provided")) {
      return null;
    } else {
      return LocalDate.parse(source.getEstimatedDateOfDeparture(), formatter);
    }
  }

  private static Set<WarningType> getWarningTypes(final CreateCaseRequest source) {
    return Arrays.stream(WarningType.values())
        .filter(warning -> source.getWarningTypes().contains(warning.type))
        .collect(Collectors.toSet());
  }

  private static ShipType getShipType(final String shipType) {
    return Arrays.stream(ShipType.values())
        .filter(ship -> ship.type.equals(shipType))
        .findFirst()
        .orElse(null);
  }
}
