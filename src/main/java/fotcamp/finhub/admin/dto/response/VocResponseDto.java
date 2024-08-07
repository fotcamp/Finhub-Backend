package fotcamp.finhub.admin.dto.response;

import fotcamp.finhub.admin.dto.process.VocProcessDto;

import java.util.List;

public record VocResponseDto(List<VocProcessDto> vocList) {
}
