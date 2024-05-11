package fotcamp.finhub.main.dto.response;

import fotcamp.finhub.main.dto.process.QuitReasonsProcessDto;

import java.util.List;

public record QuitReasonsResponseDto(List<QuitReasonsProcessDto> quitReasons) {
}
