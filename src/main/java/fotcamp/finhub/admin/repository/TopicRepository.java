package fotcamp.finhub.admin.repository;

import fotcamp.finhub.common.domain.Category;
import fotcamp.finhub.common.domain.Topic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {

    Page<Topic> findByUseYNAndTitleContaining(String useYn, String keyword, Pageable pageable);
    Page<Topic> findByUseYNAndSummaryContaining(String useYn, String keyword, Pageable pageable);
    Page<Topic> findByUseYNAndTitleContainingOrSummaryContaining(String useYn, String titleKeyword, String summaryKeyword, Pageable pageable);


    // 첫 번째 카테고리의 토픽을 cursor 기반으로 조회
    @Query("SELECT t FROM Topic t WHERE t.category = :category AND t.id >= :cursorId AND t.useYN = 'Y' AND t.position IS NOT NULL ORDER BY t.position ASC")
    List<Topic> findByCategoryAndIdGreaterThan(@Param("category") Category category, @Param("cursorId") Long cursorId, Pageable pageable);

    @Query("SELECT t FROM Topic t WHERE t.category.id = :categoryId AND t.position > :position AND t.useYN = 'Y' ORDER BY t.position ASC")
    Page<Topic> findNextTopicInSameCategory(@Param("categoryId") Long categoryId, @Param("position") Long position, Pageable pageable);

    List<Topic> findByUseYNAndCategoryAndPositionIsNotNullOrderByPositionAsc(String useYN, Category category);

    @Query("SELECT COUNT(t) FROM Topic t WHERE t.category.id = :categoryId")
    long countTopicsById(@Param("categoryId") Long id);

    @Modifying
    @Query("UPDATE Topic t SET t.position = :position WHERE t.id = :id")
    void updatePosition(@Param("id") Long id, @Param("position") Long position);

    @Query("SELECT COALESCE(MAX(t.position), 0) FROM Topic t")
    Long findMaxPosition();
}
