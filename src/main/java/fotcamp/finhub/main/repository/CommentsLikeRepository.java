package fotcamp.finhub.main.repository;

import fotcamp.finhub.common.domain.Comments;
import fotcamp.finhub.common.domain.CommentsLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentsLikeRepository extends JpaRepository<CommentsLike, Long> {
}
