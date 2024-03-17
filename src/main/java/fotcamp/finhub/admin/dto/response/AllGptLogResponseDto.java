package fotcamp.finhub.admin.dto.response;

import fotcamp.finhub.admin.dto.process.GptLogProcessDto;
import fotcamp.finhub.common.dto.process.PageInfoProcessDto;

import java.util.List;

public record AllGptLogResponseDto(List<GptLogProcessDto> gptLogList, PageInfoProcessDto pageInfo) {
}
