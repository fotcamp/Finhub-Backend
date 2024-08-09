package fotcamp.finhub.admin.dto.response;

import fotcamp.finhub.admin.dto.process.VocListProcessDto;

import java.util.List;

public record VocListResponseDto(List<VocListProcessDto> vocList) {
}
