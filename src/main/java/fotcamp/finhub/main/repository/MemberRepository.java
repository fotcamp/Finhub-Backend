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


    Optional<Member> findByEmailAndProvider(String email, String provider);
    List<Member> findByCalendarEmoticon(CalendarEmoticon calendarEmoticon);
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);

    List<Member> findByPushYn(boolean pushYn);

    @Query("SELECT COUNT(m) FROM Member m WHERE m.userType = :usertype")
    long countMemberUsingUsertype(@Param("usertype") UserType userType);

    @Query("SELECT m FROM Member m WHERE m.userType = :usertype")
    List<Member> findMemberListUsingUsertype(@Param("usertype") UserType userType);

    @Query("SELECT COUNT(m) FROM Member m WHERE m.userAvatar = :userAvatar")
    long countMemberUsingUserAvatar(@Param("userAvatar") UserAvatar userAvatar);

    @Query("SELECT m FROM Member m WHERE m.userAvatar = :userAvatar")
    List<Member> findMemberListUsingUserAvatar(@Param("userAvatar") UserAvatar userAvatar);

    @Query("SELECT m FROM Member m WHERE m.pushYn = true AND m.email IN :emails")
    List<Member> findByPushYnAndEmails(@Param("emails") List<String> emails);
}
