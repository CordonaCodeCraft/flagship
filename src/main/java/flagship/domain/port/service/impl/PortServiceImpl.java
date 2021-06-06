package flagship.domain.port.service.impl;

import flagship.domain.port.entity.Port;
import flagship.domain.port.repository.PortRepository;
import flagship.domain.port.service.PortService;
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
