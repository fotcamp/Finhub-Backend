package fotcamp.finhub.admin.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateGptColumnRequestDto(@NotBlank String subject) {
}
