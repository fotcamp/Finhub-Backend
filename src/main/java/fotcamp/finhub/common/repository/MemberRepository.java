package fotcamp.finhub.common.repository;

import fotcamp.finhub.common.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByMemberByEmail(String email);
}
