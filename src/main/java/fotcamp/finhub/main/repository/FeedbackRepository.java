package fotcamp.finhub.main.repository;

import fotcamp.finhub.common.domain.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

}
