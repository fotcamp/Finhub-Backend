package fotcamp.finhub.admin.dto.response;

import fotcamp.finhub.admin.dto.process.GetUserAvatarProcessDto;

import java.util.List;

public record AllUserAvatarResponseDto(List<GetUserAvatarProcessDto> userAvatars) {
}
