package flagship.domain.caze.repository;

import flagship.domain.caze.entity.Case;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CaseRepository extends JpaRepository<Case, UUID> {}
