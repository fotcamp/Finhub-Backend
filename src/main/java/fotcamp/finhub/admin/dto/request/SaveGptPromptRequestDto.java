package fotcamp.finhub.admin.dto.request;

import jakarta.validation.constraints.NotBlank;

public record SaveGptPromptRequestDto(@NotBlank String prompt) {
}
