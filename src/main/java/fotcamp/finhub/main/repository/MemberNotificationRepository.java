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

    @Query("SELECT mn FROM MemberNotification mn LEFT JOIN FETCH mn.notification " +
            "WHERE mn.member = :member " +
            "AND (mn.notification.id < :cursorId OR (mn.receivedAt IS NOT NULL AND mn.notification.id >= :cursorId)) " +
            "ORDER BY (CASE WHEN mn.receivedAt IS NULL THEN 0 ELSE 1 END), " +
            "mn.sentAt DESC, mn.notification.id DESC")
    Slice<MemberNotification> findNotificationsForMember(@Param("member") Member member, @Param("cursorId") Long cursorId, Pageable pageable);

}
