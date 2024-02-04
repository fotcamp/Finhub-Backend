package fotcamp.finhub.admin.dto.process;

import fotcamp.finhub.common.domain.Topic;
import lombok.Getter;

@Getter
public class DetailCategoryTopicProcessDto {
    private final Long id;
    private final String title;

    public DetailCategoryTopicProcessDto(Topic topic) {
        this.id = topic.getId();
        this.title = topic.getTitle();
    }
}
