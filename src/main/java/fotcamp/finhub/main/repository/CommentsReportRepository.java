package fotcamp.finhub.main.repository;

import fotcamp.finhub.common.domain.Comments;
import fotcamp.finhub.common.domain.CommentsReport;
import fotcamp.finhub.common.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentsReportRepository extends JpaRepository<CommentsReport, Long> {
    Optional<CommentsReport> findByReportedCommentAndReporterMember(Comments reportedComment, Member reporterMember);
    Optional<CommentsReport> findByReportedComment(Comments reportedComment);
    List<CommentsReport> findByReporterMember(Member member);
    List<CommentsReport> findByReportedMember(Member member);

}
