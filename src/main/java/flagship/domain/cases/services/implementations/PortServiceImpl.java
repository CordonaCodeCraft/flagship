package flagship.domain.cases.services.implementations;

import flagship.domain.cases.entities.Port;
import flagship.domain.cases.repositories.PortRepository;
import flagship.domain.cases.services.PortService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PortServiceImpl implements PortService {

  private final PortRepository portRepository;

  @Override
  public Port create(Port target) {
    return null;
  }

  @Override
  public Port update(Port target) {
    return null;
  }

  @Override
  public Port delete(Port target) {
    return null;
  }

  @Override
  public Optional<Port> findById(UUID uuid) {
    return Optional.empty();
  }
}
