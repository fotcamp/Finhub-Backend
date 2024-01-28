package fotcamp.finhub.admin.dto;

import fotcamp.finhub.common.domain.Category;
import lombok.Getter;

import java.util.List;

@Getter
public class DetailCategoryResponseDto {
    private final Long categoryId;
    private final String name; // 카테고리명
    private final String thumbnailImgPath; // 카테고리 썸네일 경로
    private final String useYN; // 카테고리 useYN
    private final List<DetailCategoryTopicResponseDto> topicList;

    public DetailCategoryResponseDto(Category category, List<DetailCategoryTopicResponseDto> dtoList) {
        this.categoryId = category.getId();
        this.name = category.getName();
        this.thumbnailImgPath = category.getThumbnailImgPath();
        this.useYN = category.getUseYN();
        this.topicList = dtoList;
    }
}
