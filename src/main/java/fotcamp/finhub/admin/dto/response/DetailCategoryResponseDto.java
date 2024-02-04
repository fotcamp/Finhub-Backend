package fotcamp.finhub.admin.dto.response;

import fotcamp.finhub.admin.dto.process.DetailCategoryTopicProcessDto;
import fotcamp.finhub.common.domain.Category;
import lombok.Getter;

import java.util.List;

@Getter
public class DetailCategoryResponseDto {
    private final Long categoryId;
    private final String name; // 카테고리명
    private final String thumbnailImgPath; // 카테고리 썸네일 경로
    private final String useYN; // 카테고리 useYN
    private final List<DetailCategoryTopicProcessDto> topicList;

    public DetailCategoryResponseDto(Category category, List<DetailCategoryTopicProcessDto> dtoList) {
        this.categoryId = category.getId();
        this.name = category.getName();
        this.thumbnailImgPath = category.getThumbnailImgPath();
        this.useYN = category.getUseYN();
        this.topicList = dtoList;
    }
}
