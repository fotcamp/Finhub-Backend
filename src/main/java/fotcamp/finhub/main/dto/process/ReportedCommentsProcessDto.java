package fotcamp.finhub.main.dto.process;

import fotcamp.finhub.common.domain.CommentsReport;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "댓글신고정보 DTO")
public class ReportedCommentsProcessDto {
    @Schema(description = "신고처리id")
    private final Long id;

    @Schema(description = "댓글id")
    private final Long commentId;

    @Schema(description = "댓글내용")
    private final String comment;

    @Schema(description = "신고사유")
    private final String reason;

    @Schema(description = "댓글사용여부", examples = {"Y", "N"})
    private final String useYn;

    @Schema(description = "신고처리여부", examples = {"Y", "N"})
    private final String isProcessed;

    @Schema(description = "신고된유저닉네임")
    private final String reportedNickname;

    @Schema(description = "신고힌유저닉네임")
    private final String reporterNickname;

    public ReportedCommentsProcessDto(CommentsReport commentsReport) {
        this.id = commentsReport.getId();
        this.commentId = commentsReport.getReportedComment().getId();
        this.comment = commentsReport.getReportedComment().getContent();
        this.reason = commentsReport.getReportReasons().getReason();
        this.useYn = commentsReport.getReportedComment().getUseYn();
        this.isProcessed = commentsReport.getIsProcessed();
        this.reportedNickname = commentsReport.getReportedMember().getNickname();
        this.reporterNickname = commentsReport.getReporterMember().getNickname();
    }
}
