package fotcamp.finhub.main.dto.process;

import fotcamp.finhub.common.domain.CommentsReport;
import fotcamp.finhub.common.domain.TopicRequest;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ReportedCommentsProcessDto {
    private final Long id;
    private final Long commentId;
    private final String comment;
    private final String reason;
    private final String useYn;
    private final String reportedNickname;
    private final String reporterNickname;

    public ReportedCommentsProcessDto(CommentsReport commentsReport) {
        this.id = commentsReport.getId();
        this.commentId = commentsReport.getReportedComment().getId();
        this.comment = commentsReport.getReportedComment().getContent();
        this.reason = commentsReport.getReportReasons().getReason();
        this.useYn = commentsReport.getUseYn();
        this.reportedNickname = commentsReport.getReportedMember().getNickname();
        this.reporterNickname = commentsReport.getReporterMember().getNickname();
    }
}
