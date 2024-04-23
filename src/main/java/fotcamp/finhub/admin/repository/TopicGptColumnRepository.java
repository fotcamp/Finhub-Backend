package fotcamp.finhub.admin.repository;

import fotcamp.finhub.common.domain.Topic;
import fotcamp.finhub.common.domain.TopicGptColumn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicGptColumnRepository extends JpaRepository<TopicGptColumn, Long> {
    @Modifying
    @Query("DELETE FROM TopicGptColumn tq WHERE tq.gptColumn.id = :gptColumnId")
    void deleteAllByGptColumnId(@Param("gptColumnId") Long gptColumnId);

    List<TopicGptColumn> findByTopic(Topic topic);
}
