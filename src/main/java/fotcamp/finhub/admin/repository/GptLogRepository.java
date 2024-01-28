package fotcamp.finhub.admin.repository;

import fotcamp.finhub.admin.domain.GptLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GptLogRepository extends JpaRepository<GptLog, Long> {
}
