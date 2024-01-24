package fotcamp.finhub.admin.dto;

import fotcamp.finhub.common.domain.Topic;
import lombok.Data;

@Data
public class DetailCategoryTopicResponseDto {
    private Long id;
    private String title;

    public DetailCategoryTopicResponseDto(Topic topic) {
        this.id = topic.getId();
        this.title = topic.getTitle();
    }
}
