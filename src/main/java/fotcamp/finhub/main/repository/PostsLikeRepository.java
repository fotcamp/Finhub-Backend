package fotcamp.finhub.main.repository;

import fotcamp.finhub.common.domain.PostsLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostsLikeRepository extends JpaRepository<PostsLike, Long> {
}
