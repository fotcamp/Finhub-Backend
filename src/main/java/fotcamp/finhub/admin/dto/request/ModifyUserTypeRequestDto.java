package fotcamp.finhub.admin.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ModifyUserTypeRequestDto(@NotNull Long id, @NotBlank String name, @NotBlank String s3ImgUrl, String useYN) {
}
