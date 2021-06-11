package flagship.domain.caze.service.impl;

import flagship.domain.caze.entity.Case;
import flagship.domain.caze.service.CaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class CaseServiceImpl implements CaseService {
  @Override
  public Case create(final Case target) {
    return null;
  }

  @Override
  public Case update(final Case target) {
    return null;
  }

  @Override
  public Case delete(final Case target) {
    return null;
  }

  @Override
  public Optional<Case> findById(final UUID uuid) {
    return Optional.empty();
  }
}
