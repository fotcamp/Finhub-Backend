package fotcamp.finhub.admin.repository;

import fotcamp.finhub.admin.domain.GptPrompt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GptPromptRepository extends JpaRepository<GptPrompt, Long> {
    Optional<GptPrompt> findFirstByOrderByIdDesc();
}
