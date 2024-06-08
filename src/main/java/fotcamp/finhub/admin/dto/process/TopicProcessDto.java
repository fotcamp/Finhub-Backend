package fotcamp.finhub.admin.dto.process;

import fotcamp.finhub.common.domain.Topic;
import lombok.Getter;

@Getter
public class TopicProcessDto {
    private final Long id;
    private final String categoryName;
    private final String title;
    private final String useYN;
    private final Long position;

    public TopicProcessDto(Topic topic) {
        this.id = topic.getId();
        this.categoryName = topic.getCategory().getName();
        this.title = topic.getTitle();
        this.useYN = topic.getUseYN();
        this.position = topic.getPosition();
    }
}
