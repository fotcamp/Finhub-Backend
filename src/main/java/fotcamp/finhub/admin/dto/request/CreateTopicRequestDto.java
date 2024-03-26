package fotcamp.finhub.admin.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateTopicRequestDto(@NotNull Long categoryId, @NotBlank String title, @NotBlank String definition, String summary, String s3ImgUrl, String shortDefinition) {
}
