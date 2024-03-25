package fotcamp.finhub.admin.repository;

import fotcamp.finhub.common.domain.TopicRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TopicRequestRepository extends JpaRepository<TopicRequest, Long> {

    boolean existsByTerm(String keyword);
}
