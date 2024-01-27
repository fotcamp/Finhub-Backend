package fotcamp.finhub.admin.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class AllCategoryResponseDto {
    private final List<CategoryResponseDto> categoryList;

    public AllCategoryResponseDto(List<CategoryResponseDto> categoryList) {
        this.categoryList = categoryList;
    }
}
