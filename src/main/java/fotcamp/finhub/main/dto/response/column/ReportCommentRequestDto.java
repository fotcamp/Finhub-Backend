package fotcamp.finhub.main.dto.response.column;

import fotcamp.finhub.common.domain.ApprovalStatus;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "댓글신고처리 DTO")
public record ReportCommentRequestDto(
        @Schema(description = "댓글아이디")
        Long id,
        @Schema(description = "신고내역처리(승인: APPROVED, 반려: REJECTED)")
        ApprovalStatus approvalStatus
) {
}
