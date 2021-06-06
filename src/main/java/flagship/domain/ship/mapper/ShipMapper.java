package flagship.domain.ship.mapper;

import flagship.domain.ship.entity.Ship;
import flagship.domain.ship.model.PdaShip;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ShipMapper {
  ShipMapper INSTANCE = Mappers.getMapper(ShipMapper.class);

  Ship pdaShipToShip(PdaShip pdaShip);

  PdaShip shipToPdaShip(Ship entity);
}
