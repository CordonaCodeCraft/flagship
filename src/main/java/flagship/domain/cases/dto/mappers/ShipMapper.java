package flagship.domain.cases.dto.mappers;

import flagship.domain.cases.dto.PdaShip;
import flagship.domain.cases.entities.Ship;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ShipMapper {
  ShipMapper INSTANCE = Mappers.getMapper(ShipMapper.class);

  Ship pdaShipToShip(PdaShip pdaShip);

  PdaShip shipToPdaShip(Ship entity);
}
