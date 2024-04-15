package fotcamp.finhub.main.repository;

import fotcamp.finhub.common.domain.Comments;
import fotcamp.finhub.common.domain.GptColumn;
import fotcamp.finhub.common.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


@Repository
public interface CommentsRepository extends JpaRepository<Comments, Long> {
    Page<Comments> findByGptColumnAndUseYnOrderByCreatedTimeDesc(GptColumn gptColumn, String useYn, Pageable pageable); // 최신순
    Page<Comments> findByGptColumnAndUseYnOrderByTotalLikeDescCreatedTimeDesc(GptColumn gptColumn, String useYn, Pageable pageable); // 인기순
    Long countByGptColumnAndMember(GptColumn gptColumn, Member member);
}
