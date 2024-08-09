package fotcamp.finhub.main.repository;



import fotcamp.finhub.common.domain.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {


    @Query("SELECT f FROM Feedback f WHERE f.reply = :reply")
    Page<Feedback> findFeedbacksByReply(@Param("reply") String reply, Pageable pageable);
}