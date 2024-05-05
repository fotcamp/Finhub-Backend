package fotcamp.finhub.main.repository;

import fotcamp.finhub.common.domain.Member;
import fotcamp.finhub.common.domain.Topic;
import fotcamp.finhub.common.domain.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {


    Optional<Member> findByEmail(String email);

    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);


    List<Member> findByPushYn(boolean pushYn);

    @Query("SELECT COUNT(m) FROM Member m WHERE m.userType = :usertype")
    long countMemberUsingUsertype(@Param("usertype") UserType userType);

}
