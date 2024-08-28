package fotcamp.finhub.main.repository;

import fotcamp.finhub.common.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByMemberUuid(String uuid);

    List<Member> findByCalendarEmoticon(CalendarEmoticon calendarEmoticon);
    boolean existsByNickname(String nickname);

    @Query("SELECT COUNT(m) FROM Member m WHERE m.userType = :usertype")
    long countMemberUsingUsertype(@Param("usertype") UserType userType);

    @Query("SELECT m FROM Member m WHERE m.userType = :usertype")
    List<Member> findMemberListUsingUsertype(@Param("usertype") UserType userType);

    @Query("SELECT COUNT(m) FROM Member m WHERE m.userAvatar = :userAvatar")
    long countMemberUsingUserAvatar(@Param("userAvatar") UserAvatar userAvatar);

    @Query("SELECT m FROM Member m WHERE m.userAvatar = :userAvatar")
    List<Member> findMemberListUsingUserAvatar(@Param("userAvatar") UserAvatar userAvatar);

    @Query("SELECT m FROM Member m LEFT JOIN FETCH m.memberAgreement WHERE m.memberId = :memberId")
    Optional<Member> findMemberWithAgreement(@Param("memberId") Long memberId);


}
