package fotcamp.finhub.main.dto.response.column;

import fotcamp.finhub.common.dto.process.PageInfoProcessDto;

import java.util.List;

public record ColumnResponse(List<CommentResponseDto> comments, PageInfoProcessDto pageInfo) {
}
