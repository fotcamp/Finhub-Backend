package fotcamp.finhub.main.repository;

import fotcamp.finhub.common.domain.Member;
import fotcamp.finhub.common.domain.MemberGptColumn;
import fotcamp.finhub.common.domain.MemberScrap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberGptColumnRepository extends JpaRepository<MemberGptColumn, Long> {

    List<MemberGptColumn> findByMember(Member member);
}
