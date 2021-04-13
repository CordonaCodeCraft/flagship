package flagship.domain.cases.services.implementations;

import flagship.domain.cases.entities.Ship;
import flagship.domain.cases.repositories.ShipRepository;
import flagship.domain.cases.services.ShipService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShipServiceImpl implements ShipService {

  private final ShipRepository shipRepository;

  @Override
  public Ship create(Ship target) {
    return shipRepository.save(target);
  }

  @Override
  public Ship update(Ship target) {
    return null;
  }

  @Override
  public Ship delete(Ship target) {
    return null;
  }

  @Override
  public Optional<Ship> findById(UUID uuid) {
    return Optional.empty();
  }
}
