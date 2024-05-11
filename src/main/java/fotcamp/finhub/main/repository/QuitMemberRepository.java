package fotcamp.finhub.main.repository;

import fotcamp.finhub.common.domain.QuitMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuitMemberRepository extends JpaRepository<QuitMember, Long> {
}
