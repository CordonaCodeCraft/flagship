package flagship.domain.caze.mapper;

import flagship.domain.caze.model.PdaCase;
import flagship.domain.caze.model.request.CreateCaseRequest;
import flagship.domain.caze.model.request.resolvers.MooringServiceProviderResolver;
import flagship.domain.caze.model.request.resolvers.PilotageAreaResolver;
import flagship.domain.caze.model.request.resolvers.PortAreaResolver;
import flagship.domain.port.model.PdaPort;
import flagship.domain.ship.model.PdaShip;
import org.mapstruct.AfterMapping;
import org.mapstruct.MappingTarget;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static flagship.domain.caze.entity.Case.CallPurpose;
import static flagship.domain.caze.model.request.resolvers.TugAreaResolver.TugServiceProvider;
import static flagship.domain.caze.model.request.resolvers.TugAreaResolver.resolveTugArea;
import static flagship.domain.ship.entity.Ship.ShipType;
import static flagship.domain.warning.generator.WarningsGenerator.WarningType;

public interface CaseMapperService {

  @AfterMapping
  default void setPdaShip(final CreateCaseRequest source, @MappingTarget final PdaCase target) {

    final PdaShip ship =
        PdaShip.builder()
            .name(source.getShipName())
            .type(getShipType(source.getShipType()))
            .lengthOverall(source.getShipLengthOverall())
            .grossTonnage(source.getShipGrossTonnage())
            .hasIncreasedManeuverability(source.getShipHasIncreasedManeuverability())
            .build();

    target.setShip(ship);
  }

  @AfterMapping
  default void setPdaPort(final CreateCaseRequest source, @MappingTarget final PdaCase target) {

    final String portName = source.getPortName();

    final PdaPort port =
        PdaPort.builder()
            .name(portName)
            .portArea(PortAreaResolver.resolvePortArea(portName))
            .pilotageArea(PilotageAreaResolver.resolvePilotageArea(portName))
            .tugArea(resolveTugArea(source))
            .tugServiceProvider(TugServiceProvider.VTC)
            .mooringServiceProvider(
                MooringServiceProviderResolver.resolveMooringServiceProvider(portName))
            .build();

    target.setPort(port);
  }

  @AfterMapping
  default void setWarningTypes(
      final CreateCaseRequest source, @MappingTarget final PdaCase target) {
    target.setWarningTypes(getWarningTypes(source));
  }

  @AfterMapping
  default void setCallPurpose(final CreateCaseRequest source, @MappingTarget final PdaCase target) {
    target.setCallPurpose(getCallPurpose(source));
  }

  @AfterMapping
  default void setEta(final CreateCaseRequest source, @MappingTarget final PdaCase target) {
    final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    target.setEstimatedDateOfArrival(
        LocalDate.parse(source.getEstimatedDateOfArrival(), formatter));
  }

  @AfterMapping
  default void setEtd(final CreateCaseRequest source, @MappingTarget final PdaCase target) {
    final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    target.setEstimatedDateOfDeparture(
        LocalDate.parse(source.getEstimatedDateOfDeparture(), formatter));
  }

  private ShipType getShipType(final String shipType) {
    return Arrays.stream(ShipType.values())
        .filter(ship -> ship.type.equals(shipType))
        .findFirst()
        .orElse(null);
  }

  private Set<WarningType> getWarningTypes(final CreateCaseRequest source) {
    return Arrays.stream(WarningType.values())
        .filter(warning -> source.getWarningTypes().contains(warning.type))
        .collect(Collectors.toSet());
  }

  private CallPurpose getCallPurpose(final CreateCaseRequest source) {
    return Arrays.stream(CallPurpose.values())
        .filter(purpose -> purpose.type.equals(source.getCallPurpose()))
        .findFirst()
        .orElse(null);
  }
}
