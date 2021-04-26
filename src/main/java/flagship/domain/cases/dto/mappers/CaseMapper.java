package flagship.domain.cases.dto.mappers;

import flagship.domain.cases.dto.PdaCase;
import flagship.domain.cases.entities.Case;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CaseMapper {
  CaseMapper INSTANCE = Mappers.getMapper(CaseMapper.class);

  Case pdaCaseToCase(PdaCase pdaCase);

  PdaCase caseToPdaCase(Case entity);
}
