package fotcamp.finhub.admin.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CreateCategoryDto {
    @NotBlank
    private String name;

    @NotBlank
    private String thumbnailImgPath;
}
