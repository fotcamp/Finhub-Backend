package fotcamp.finhub.admin.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateCategoryRequestDto(@NotBlank String name, @NotBlank String thumbnailImgPath) {
}
