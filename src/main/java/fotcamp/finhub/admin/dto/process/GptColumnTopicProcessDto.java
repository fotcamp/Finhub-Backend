package fotcamp.finhub.admin.dto.process;

import fotcamp.finhub.common.domain.TopicGptColumn;
import fotcamp.finhub.common.domain.TopicQuiz;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GptColumnTopicProcessDto {
    private Long id;
    private String title;

    public GptColumnTopicProcessDto(TopicGptColumn topicGptColumn) {
        this.id = topicGptColumn.getTopic().getId();
        this.title = topicGptColumn.getTopic().getTitle();
    }
}
