package flagship.domain.port.mapper;

import flagship.domain.port.entity.Port;
import flagship.domain.port.model.PdaPort;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PortMapper {
  PortMapper INSTANCE = Mappers.getMapper(PortMapper.class);

  Port pdaPortToPort(PdaPort pdaPort);

  PdaPort portToPdaPort(Port entity);
}
