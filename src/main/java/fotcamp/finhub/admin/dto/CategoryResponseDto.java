package fotcamp.finhub.admin.dto;

import fotcamp.finhub.common.domain.Category;
import lombok.Getter;

@Getter
public class CategoryResponseDto {
    private final Long id;
    private final String name;
    private final String thumbnailImgPath;
    private final String useYN;

    public CategoryResponseDto(Category category) {
        this.id = category.getId();
        this.name = category.getName();
        this.thumbnailImgPath = category.getThumbnailImgPath();
        this.useYN = category.getUseYN();
    }
}
