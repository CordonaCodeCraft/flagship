package flagship.domain.caze.mapper;

import flagship.domain.caze.entity.Case;
import flagship.domain.pda.model.PdaCase;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CaseMapper {
  CaseMapper INSTANCE = Mappers.getMapper(CaseMapper.class);

  Case pdaCaseToCase(PdaCase pdaCase);

  PdaCase caseToPdaCase(Case entity);
}
