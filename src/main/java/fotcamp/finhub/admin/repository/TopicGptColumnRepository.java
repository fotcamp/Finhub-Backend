package fotcamp.finhub.admin.repository;

import fotcamp.finhub.common.domain.GptColumn;
import fotcamp.finhub.common.domain.Topic;
import fotcamp.finhub.common.domain.TopicGptColumn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicGptColumnRepository extends JpaRepository<TopicGptColumn, Long> {
    @Modifying
    @Query("DELETE FROM TopicGptColumn tq WHERE tq.gptColumn.id = :gptColumnId")
    void deleteAllByGptColumnId(@Param("gptColumnId") Long gptColumnId);

    List<TopicGptColumn> findByTopic(Topic topic);
    List<TopicGptColumn> findByGptColumn(GptColumn gptColumn);

    @Query("SELECT COUNT(t) FROM TopicGptColumn t WHERE t.topic.id = :topicId")
    Long countTopicGptColumnByTopicId(@Param("topicId")Long id);

    @Modifying
    @Query("DELETE FROM TopicGptColumn t WHERE t.gptColumn = :gptColumn")
    void deleteByGptColumn(@Param("gptColumn") GptColumn gptColumn);

}
