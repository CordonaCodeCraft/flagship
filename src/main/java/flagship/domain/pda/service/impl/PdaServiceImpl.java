package flagship.domain.pda.service.impl;

import flagship.domain.pda.entity.ProformaDisbursementAccount;
import flagship.domain.pda.repository.PDARepository;
import flagship.domain.pda.service.PdaService;
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
