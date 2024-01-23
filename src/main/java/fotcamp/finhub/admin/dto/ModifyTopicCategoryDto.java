package fotcamp.finhub.admin.dto;

import lombok.Getter;

@Getter
public class ModifyTopicCategoryDto {
    private Long topicId;
    private String title;
    private Long categoryId;
}
