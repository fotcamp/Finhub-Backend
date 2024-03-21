package fotcamp.finhub.admin.repository;

import fotcamp.finhub.common.domain.Topic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {

    Page<Topic> findByTitleContaining(String keyword, Pageable pageable);
    Page<Topic> findBySummaryContaining(String keyword, Pageable pageable);
    Page<Topic> findByTitleContainingOrSummaryContaining(String titleKeyword, String summaryKeyword, Pageable pageable);
}
