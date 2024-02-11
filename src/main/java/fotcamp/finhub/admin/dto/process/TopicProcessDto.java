package fotcamp.finhub.admin.dto.process;

import fotcamp.finhub.common.domain.Topic;
import lombok.Getter;

@Getter
public class TopicProcessDto {
    private final Long topicId;
    private final String categoryName;
    private final String title;
    private final String useYN;

    public TopicProcessDto(Topic topic) {
        this.topicId = topic.getId();
        this.categoryName = topic.getCategory().getName();
        this.title = topic.getTitle();
        this.useYN = topic.getUseYN();
    }
}
