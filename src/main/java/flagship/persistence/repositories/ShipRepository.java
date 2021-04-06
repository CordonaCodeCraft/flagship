package flagship.persistence.repositories;

import flagship.domain.entity.Ship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ShipRepository extends JpaRepository<Ship, UUID> {

}
