package flagship.domain.cases.dto.mappers;

import flagship.domain.cases.dto.PdaPort;
import flagship.domain.cases.entities.Port;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PortMapper {
  PortMapper INSTANCE = Mappers.getMapper(PortMapper.class);

  Port pdaPortToPort(PdaPort pdaPort);

  PdaPort portToPdaPort(Port entity);
}
