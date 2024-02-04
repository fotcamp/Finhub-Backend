package fotcamp.finhub.admin.dto.response;

import fotcamp.finhub.admin.dto.process.UserTypeProcessDto;

import java.util.List;

public record AllUserTypeResponseDto(List<UserTypeProcessDto> usertypeList) {
}
