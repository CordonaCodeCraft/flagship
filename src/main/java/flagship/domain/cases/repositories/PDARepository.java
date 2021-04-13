package flagship.domain.cases.repositories;

import flagship.domain.cases.entities.ProformaDisbursementAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PDARepository extends JpaRepository<ProformaDisbursementAccount, UUID> {}
