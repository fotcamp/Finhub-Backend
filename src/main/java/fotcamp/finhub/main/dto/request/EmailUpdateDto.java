package fotcamp.finhub.main.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "이메일 변경 dto")
public record EmailUpdateDto(
        @Email
        @NotBlank
        @Schema(description = "이메일")
        String email
) {
}
