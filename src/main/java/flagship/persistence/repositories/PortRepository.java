package flagship.persistence.repositories;

import flagship.domain.entities.Port;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PortRepository extends JpaRepository<Port, UUID> {

}
