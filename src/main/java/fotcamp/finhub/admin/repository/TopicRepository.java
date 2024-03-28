package fotcamp.finhub.admin.repository;

import fotcamp.finhub.common.domain.Category;
import fotcamp.finhub.common.domain.Topic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {

    Page<Topic> findByTitleContaining(String keyword, Pageable pageable);
    Page<Topic> findBySummaryContaining(String keyword, Pageable pageable);
    Page<Topic> findByTitleContainingOrSummaryContaining(String titleKeyword, String summaryKeyword, Pageable pageable);

    // 첫 번째 카테고리의 토픽을 cursor 기반으로 조회
    @Query("SELECT t FROM Topic t WHERE t.category = :category AND t.id >= :cursorId ORDER BY t.id ASC")
    List<Topic> findByCategoryAndIdGreaterThan(@Param("category") Category category, @Param("cursorId") Long cursorId, Pageable pageable);


    @Query("SELECT t FROM Topic t WHERE t.category.id = :categoryId AND t.id > :topicId ORDER BY t.id ASC")
    Page<Topic> findNextTopicInSameCategory(@Param("categoryId") Long categoryId, @Param("topicId") Long topicId, Pageable pageable);
}
