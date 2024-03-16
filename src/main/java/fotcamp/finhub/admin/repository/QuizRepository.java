package fotcamp.finhub.admin.repository;

import fotcamp.finhub.common.domain.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
    // targetDate를 기준으로 Quiz 데이터의 존재 여부를 확인하는 메서드
    boolean existsByTargetDate(LocalDate targetDate);
}
