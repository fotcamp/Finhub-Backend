package fotcamp.finhub.admin.dto.request;


import jakarta.validation.constraints.NotBlank;

public record CreateUserTypeRequestDto(@NotBlank String name) {
}
