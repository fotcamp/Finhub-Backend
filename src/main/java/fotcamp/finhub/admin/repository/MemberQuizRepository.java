package fotcamp.finhub.admin.repository;

import fotcamp.finhub.common.domain.Member;
import fotcamp.finhub.common.domain.MemberQuiz;
import fotcamp.finhub.common.domain.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberQuizRepository extends JpaRepository<MemberQuiz, Long> {
    Optional<MemberQuiz> findByMemberAndQuiz(Member member, Quiz quiz);
}
