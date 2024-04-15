package fotcamp.finhub.main.repository;

import fotcamp.finhub.common.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentsLikeRepository extends JpaRepository<CommentsLike, Long> {
    Optional<CommentsLike> findFirstByCommentAndMember(Comments comment, Member member);

}
