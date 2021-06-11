package flagship.domain.caze.mapper;

import flagship.domain.caze.entity.Case;
import flagship.domain.caze.model.PdaCase;
import flagship.domain.caze.model.request.CreateCaseRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CaseMapper extends CaseMapperService {

  CaseMapper INSTANCE = Mappers.getMapper(CaseMapper.class);

  @Mapping(target = "warningTypes", ignore = true)
  @Mapping(target = "callPurpose", ignore = true)
  PdaCase createCaseRequestToPdaCase(CreateCaseRequest source);

  Case pdaCaseToCase(PdaCase pdaCase);

  PdaCase caseToPdaCase(Case entity);
}
