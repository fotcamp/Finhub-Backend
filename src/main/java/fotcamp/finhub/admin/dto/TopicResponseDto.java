package fotcamp.finhub.admin.dto;

import fotcamp.finhub.common.domain.Topic;
import lombok.Getter;

@Getter
public class TopicResponseDto {
    private Long topicId;
    private String categoryName;
    private String topicTitle;
    private String useYN;

    public TopicResponseDto(Topic topic) {
        this.topicId = topic.getId();
        this.categoryName = topic.getCategory().getName();
        this.topicTitle = topic.getTitle();
        this.useYN = topic.getUseYN();
    }
}
