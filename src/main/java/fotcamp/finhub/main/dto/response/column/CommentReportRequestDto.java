package fotcamp.finhub.main.dto.response.column;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "댓글신고 DTO")
public record CommentReportRequestDto(
        @Schema(description = "댓글고유아이디")
        Long commentId,
        @Schema(description = "신고사유번호")
        Long reportId) {
}
