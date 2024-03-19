package fotcamp.finhub.admin.dto.request;


import jakarta.validation.constraints.NotBlank;

public record CreateUserTypeRequestDto(@NotBlank String name, @NotBlank String s3ImgUrl) {
}
