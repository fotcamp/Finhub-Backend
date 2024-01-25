package fotcamp.finhub.admin.dto;

import fotcamp.finhub.common.domain.Category;
import lombok.Data;

import java.util.List;

@Data
public class AllCategoryResponseDto {
    private List<CategoryResponseDto> categoryList;

    public AllCategoryResponseDto(List<CategoryResponseDto> categoryList) {
        this.categoryList = categoryList;
    }
}
