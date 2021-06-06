package flagship.domain.ship.repository;

import flagship.domain.ship.entity.Ship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ShipRepository extends JpaRepository<Ship, UUID> {}
