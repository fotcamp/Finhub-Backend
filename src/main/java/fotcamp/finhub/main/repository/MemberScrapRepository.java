package fotcamp.finhub.main.repository;

import fotcamp.finhub.common.domain.Member;
import fotcamp.finhub.common.domain.MemberScrap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberScrapRepository extends JpaRepository<MemberScrap, Long> {

    @Query("SELECT m FROM MemberScrap m WHERE m.member.memberId = :memberId AND m.topic.id = :topicId")
    Optional<MemberScrap> findByMemberIdAndTopicId(@Param("memberId") Long memberId, @Param("topicId") Long topicId);

    // List<MemberScrap> findByMember_memberId(Long memberId);
    List<MemberScrap> findByMember(Member member);
}
