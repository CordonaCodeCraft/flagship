package flagship.domain.cases.services.implementations;

import flagship.domain.cases.entities.ProformaDisbursementAccount;
import flagship.domain.cases.repositories.PDARepository;
import flagship.domain.cases.services.PdaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PdaServiceImpl implements PdaService {

  private final PDARepository pdaRepository;

  @Override
  public ProformaDisbursementAccount create(ProformaDisbursementAccount target) {
    return null;
  }

  @Override
  public ProformaDisbursementAccount update(ProformaDisbursementAccount target) {
    return null;
  }

  @Override
  public ProformaDisbursementAccount delete(ProformaDisbursementAccount target) {
    return null;
  }

  @Override
  public Optional<ProformaDisbursementAccount> findById(UUID uuid) {
    return Optional.empty();
  }
}
