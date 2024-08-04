package fotcamp.finhub.main.repository;

import fotcamp.finhub.common.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentsLikeRepository extends JpaRepository<CommentsLike, Long> {
    Optional<CommentsLike> findFirstByCommentAndMember(Comments comment, Member member);
    List<CommentsLike> findByMember(Member member);

    @Modifying
    @Query("DELETE FROM CommentsLike cl WHERE cl.comment = :comment")
    void deleteByComments(@Param("comment") Comments comment);

}
