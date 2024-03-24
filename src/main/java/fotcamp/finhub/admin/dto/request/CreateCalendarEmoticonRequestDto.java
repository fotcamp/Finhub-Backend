package fotcamp.finhub.admin.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateCalendarEmoticonRequestDto(@NotBlank String s3ImgUrl) {
}
