package fotcamp.finhub.main.repository;

import fotcamp.finhub.common.domain.Agreement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SignUpAgreementRepository extends JpaRepository<Agreement, Long> {
}
