package fotcamp.finhub.admin.repository;

import fotcamp.finhub.common.domain.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
    // targetDate를 기준으로 Quiz 데이터의 존재 여부를 확인하는 메서드
    boolean existsByTargetDate(LocalDate targetDate);

    // 해당 날짜 사이에 있는 Quiz 데이터 리스트 형태로 반환
    List<Quiz> findByTargetDateBetween(LocalDate startDate, LocalDate endDate);

    Optional<Quiz> findByTargetDate(LocalDate targetDate);
}
