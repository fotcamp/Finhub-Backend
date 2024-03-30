package fotcamp.finhub.admin.repository;

import fotcamp.finhub.common.domain.GptColumn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GptColumnRepository extends JpaRepository<GptColumn, Long> {
}
