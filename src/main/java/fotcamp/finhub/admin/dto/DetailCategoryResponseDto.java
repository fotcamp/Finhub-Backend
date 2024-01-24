package fotcamp.finhub.admin.dto;

import fotcamp.finhub.common.domain.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class DetailCategoryResponseDto {
    private Long categoryId;
    private String name; // 카테고리명
    private String useYN; // 카테고리 useYN
    private List<DetailCategoryTopicResponseDto> topicList = new ArrayList<>();

    public DetailCategoryResponseDto(Category category, List<DetailCategoryTopicResponseDto> dtoList) {
        this.categoryId = category.getId();
        this.name = category.getName();
        this.useYN = category.getUseYN();
        this.topicList = dtoList;
    }

}
