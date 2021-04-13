package flagship.domain.cases.repositories;

import flagship.domain.cases.entities.Port;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PortRepository extends JpaRepository<Port, UUID> {}
