package fotcamp.finhub.main.repository;


import fotcamp.finhub.common.domain.Member;
import fotcamp.finhub.common.domain.MemberNotification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberNotificationRepository extends JpaRepository<MemberNotification, Long> {

    List<MemberNotification> findByMember(Member member);

    Slice<MemberNotification> findByMemberAndIdLessThanOrderBySentAtDesc(Member member, Long cursorId, Pageable pageable);
}
