package fotcamp.finhub.main.dto.process;

import fotcamp.finhub.common.domain.CommentsReport;
import fotcamp.finhub.common.domain.TopicRequest;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ReportedCommentsProcessDto {
    private final Long id;
    private final String comment;
    private final String reason;
    private final String useYn;
    private final String writerNickname;

    public ReportedCommentsProcessDto(CommentsReport commentsReport) {
        this.id = commentsReport.getId();
        this.comment = commentsReport.getReportedComment().getContent();
        this.reason = commentsReport.getReportReasons().getReason();
        this.useYn = commentsReport.getReportedComment().getUseYn();
        this.writerNickname = commentsReport.getReportedMember().getNickname();
    }
}
