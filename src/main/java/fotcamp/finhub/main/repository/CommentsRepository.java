package fotcamp.finhub.main.repository;

import fotcamp.finhub.common.domain.Comments;
import fotcamp.finhub.common.domain.GptColumn;
import fotcamp.finhub.common.domain.Member;
import fotcamp.finhub.common.domain.QuitReasons;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


@Repository
public interface CommentsRepository extends JpaRepository<Comments, Long> {

    Page<Comments> findByGptColumnAndUseYnOrderByCreatedTimeDesc
            (GptColumn gptColumn, String useYn,Pageable pageable);
    Page<Comments> findByGptColumnAndUseYnOrderByTotalLikeDescCreatedTimeDesc
            (GptColumn gptColumn, String useYn, Pageable pageable);
    Page<Comments> findByGptColumnAndUseYnAndMemberNotInOrderByCreatedTimeDesc
            (GptColumn gptColumn, String useYn, List<Member> blockedMemberIds,Pageable pageable); // 최신순
    Page<Comments> findByGptColumnAndUseYnAndMemberNotInOrderByTotalLikeDescCreatedTimeDesc
            (GptColumn gptColumn, String useYn, List<Member> blockedMemberIds, Pageable pageable); // 인기순

    Long countByGptColumnAndMember(GptColumn gptColumn, Member member);
    List<Comments> findByMemberAndUseYn(Member member, String useYn);

    List<Comments> findByGptColumn(GptColumn gptColumn);
    List<Comments> findByMember(Member member);

}
