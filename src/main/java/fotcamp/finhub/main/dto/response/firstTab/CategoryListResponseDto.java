package fotcamp.finhub.main.dto.response.firstTab;


import fotcamp.finhub.main.dto.process.CategoryListProcessDto;
import lombok.Getter;

import java.util.List;

@Getter
public class CategoryListResponseDto {

    private List<CategoryListProcessDto> categoryList;

    public CategoryListResponseDto(List<CategoryListProcessDto> categoryList) {
        this.categoryList = categoryList;
    }
}
