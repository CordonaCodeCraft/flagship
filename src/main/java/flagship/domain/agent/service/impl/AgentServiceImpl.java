package flagship.domain.agent.service.impl;

import flagship.domain.agent.entity.Agent;
import flagship.domain.agent.repository.AgentRepository;
import flagship.domain.agent.service.AgentService;
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
