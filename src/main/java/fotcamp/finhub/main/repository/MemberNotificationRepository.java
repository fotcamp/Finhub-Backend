package fotcamp.finhub.main.repository;


import fotcamp.finhub.common.domain.Member;
import fotcamp.finhub.common.domain.MemberNotification;
import fotcamp.finhub.common.domain.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberNotificationRepository extends JpaRepository<MemberNotification, Long> {

    List<MemberNotification> findByMember(Member member);
    Optional<MemberNotification> findByMemberAndNotification(Member member, Notification notification);

    @Query("SELECT m FROM MemberNotification m WHERE m.member = :member AND m.id < :cursorId ORDER BY m.sentAt DESC")
    Slice<MemberNotification> findNotificationsForMember(@Param("member") Member member, @Param("cursorId") Long cursorId, Pageable pageable);
}
