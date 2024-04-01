package fotcamp.finhub.admin.dto.response;

import fotcamp.finhub.admin.dto.process.GetGptColumnProcessDto;

import java.util.List;

public record AllGptColumnResponseDto(List<GetGptColumnProcessDto> gptColumns) {
}
