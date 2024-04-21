package fotcamp.finhub.admin.repository;

import fotcamp.finhub.common.domain.Topic;
import fotcamp.finhub.common.domain.TopicQuiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TopicQuizRepository extends JpaRepository<TopicQuiz, Long> {

    @Modifying
    @Query("DELETE FROM TopicQuiz tq WHERE tq.quiz.id = :quizId")
    void deleteAllByQuizId(@Param("quizId") Long quizId);

    List<TopicQuiz> findByTopic(Topic topic);
}
