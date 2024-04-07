package fotcamp.finhub.admin.repository;

import fotcamp.finhub.common.domain.Quiz;
import fotcamp.finhub.main.dto.response.quiz.QuizInfoDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
    // targetDate를 기준으로 Quiz 데이터의 존재 여부를 확인하는 메서드
    boolean existsByTargetDate(LocalDate targetDate);

    // 해당 날짜 사이에 있는 Quiz 데이터 리스트 형태로 반환
    List<Quiz> findByTargetDateBetween(LocalDate startDate, LocalDate endDate);

    Optional<Quiz> findByTargetDate(LocalDate targetDate);

    @Query("SELECT new fotcamp.finhub.main.dto.response.quiz.QuizInfoDto(q.id, q.question, q.targetDate) FROM Quiz q WHERE q.id NOT IN (SELECT mq.quiz.id FROM MemberQuiz mq WHERE mq.member.memberId = :memberId) AND q.targetDate < :cursorDate ORDER BY q.targetDate DESC")
    List<QuizInfoDto> findMissedQuizInfoByMemberId(@Param("memberId") Long memberId, @Param("cursorDate") LocalDate cursorDate, Pageable pageable);
}
