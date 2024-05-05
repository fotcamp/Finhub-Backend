package fotcamp.finhub.main.dto.response.column;

import fotcamp.finhub.common.dto.process.PageInfoProcessDto;
import fotcamp.finhub.main.dto.process.ReportedCommentsProcessDto;

import java.util.List;

public record ReportedCommentsResponseDto(List<ReportedCommentsProcessDto> reportedCommentsList, PageInfoProcessDto pageInfo) {
}
