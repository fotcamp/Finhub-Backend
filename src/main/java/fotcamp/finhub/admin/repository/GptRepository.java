package fotcamp.finhub.admin.repository;

import fotcamp.finhub.common.domain.Gpt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GptRepository extends JpaRepository<Gpt, Long> {
}
