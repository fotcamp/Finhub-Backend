package fotcamp.finhub.main.repository;

import fotcamp.finhub.common.domain.Block;
import fotcamp.finhub.common.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlockRepository extends JpaRepository<Block, Long> {

    @Query("SELECT b.blockMember FROM Block b WHERE b.member.id = :memberId")
    List<Member> findBlockMemberByMemberId(@Param("memberId") Long memberId);
}
