package flagship.domain.cases.services.implementations;

import flagship.domain.cases.entities.Agent;
import flagship.domain.cases.repositories.AgentRepository;
import flagship.domain.cases.services.AgentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AgentServiceImpl implements AgentService {

  private final AgentRepository agentRepository;

  @Override
  public Agent create(Agent target) {
    return null;
  }

  @Override
  public Agent update(Agent target) {
    return null;
  }

  @Override
  public Agent delete(Agent target) {
    return null;
  }

  @Override
  public Optional<Agent> findById(UUID uuid) {
    return Optional.empty();
  }
}
