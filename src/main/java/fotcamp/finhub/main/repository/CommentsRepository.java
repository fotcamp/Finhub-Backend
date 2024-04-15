package fotcamp.finhub.main.repository;

import fotcamp.finhub.common.domain.Comments;
import fotcamp.finhub.common.domain.GptColumn;
import fotcamp.finhub.common.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentsRepository extends JpaRepository<Comments, Long> {
    List<Comments> findByGptColumnAndUseYnOrderByCreatedTimeDesc(GptColumn gptColumn, String useYn); // 최신순
    List<Comments> findByGptColumnAndUseYnOrderByTotalLikeDescCreatedTimeDesc(GptColumn gptColumn, String useYn); // 인기순
    Long countByGptColumnAndMember(GptColumn gptColumn, Member member);
}
