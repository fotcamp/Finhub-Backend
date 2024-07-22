package fotcamp.finhub.main.repository;

import fotcamp.finhub.common.domain.GptColumn;
import fotcamp.finhub.common.domain.Member;
import fotcamp.finhub.common.domain.PostsLike;
import fotcamp.finhub.common.domain.PostsScrap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostsLikeRepository extends JpaRepository<PostsLike, Long> {
    Optional<PostsLike> findFirstByGptColumnAndMember(GptColumn gptColumn, Member member);
    Long countByGptColumn(GptColumn gptColumn);
    List<PostsLike> findByMember(Member member);

}
