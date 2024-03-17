package fotcamp.finhub.admin.repository;

import fotcamp.finhub.common.domain.TopicQuiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TopicQuizRepository extends JpaRepository<TopicQuiz, Long> {

    @Modifying
    @Query("DELETE FROM TopicQuiz tq WHERE tq.quiz.id = :quizId")
    void deleteAllByQuizId(@Param("quizId") Long quizId);

}
