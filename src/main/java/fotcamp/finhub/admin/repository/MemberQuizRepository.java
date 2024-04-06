package fotcamp.finhub.admin.repository;

import fotcamp.finhub.common.domain.Member;
import fotcamp.finhub.common.domain.MemberQuiz;
import fotcamp.finhub.common.domain.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MemberQuizRepository extends JpaRepository<MemberQuiz, Long> {
    Optional<MemberQuiz> findByMemberAndQuiz(Member member, Quiz quiz);

    @Query("SELECT mq FROM MemberQuiz mq WHERE mq.member.memberId = :memberId " +
            "AND mq.solvedTime BETWEEN :startDateTime AND :endDateTime")
    List<MemberQuiz> findAllByMemberAndSolvedTimeBetween(
            @Param("memberId") Long memberId,
            @Param("startDateTime") LocalDateTime startDateTime,
            @Param("endDateTime") LocalDateTime endDateTime
    );
}
