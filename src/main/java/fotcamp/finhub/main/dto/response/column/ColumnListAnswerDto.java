package fotcamp.finhub.main.dto.response.column;

import fotcamp.finhub.common.dto.process.PageInfoProcessDto;

import java.util.List;

public record ColumnListAnswerDto(List<ColumnListDto> columnInfo, PageInfoProcessDto pageInfo) {
}


