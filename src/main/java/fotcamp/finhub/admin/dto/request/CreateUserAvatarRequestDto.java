package fotcamp.finhub.admin.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateUserAvatarRequestDto(@NotBlank String s3ImgUrl) {
}
