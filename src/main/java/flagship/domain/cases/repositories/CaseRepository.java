package flagship.domain.cases.repositories;

import flagship.domain.cases.entities.Case;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CaseRepository extends JpaRepository<Case, UUID> {}
