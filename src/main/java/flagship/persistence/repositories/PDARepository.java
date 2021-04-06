package flagship.persistence.repositories;

import flagship.domain.entity.ProformaDisbursementAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PDARepository extends JpaRepository<ProformaDisbursementAccount, UUID> {

}
