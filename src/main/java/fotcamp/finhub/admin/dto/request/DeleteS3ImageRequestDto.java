package fotcamp.finhub.admin.dto.request;

import jakarta.validation.constraints.NotBlank;

public record DeleteS3ImageRequestDto(@NotBlank String s3ImgUrl) {
}
