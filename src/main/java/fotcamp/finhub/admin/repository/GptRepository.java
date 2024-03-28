package fotcamp.finhub.admin.repository;

import fotcamp.finhub.common.domain.Gpt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

@Repository
public interface GptRepository extends JpaRepository<Gpt, Long> {

    @Query("SELECT g FROM Gpt g WHERE g.userType.id = :userTypeId AND g.topic.id = :topicId")
    Gpt findByUserTypeIdAndTopicId(@Param("userTypeId") Long userTypeId,@Param("topicId") Long topicId);
}
