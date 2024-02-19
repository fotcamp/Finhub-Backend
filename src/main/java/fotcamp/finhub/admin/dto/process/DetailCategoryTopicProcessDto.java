package fotcamp.finhub.admin.dto.process;

import fotcamp.finhub.common.domain.Topic;
import lombok.Getter;

@Getter
public class DetailCategoryTopicProcessDto {
    private final Long topicId;
    private final String title;
    private final Long categoryId;

    public DetailCategoryTopicProcessDto(Topic topic) {
        this.topicId = topic.getId();
        this.title = topic.getTitle();
        this.categoryId = topic.getCategory().getId();
    }
}
