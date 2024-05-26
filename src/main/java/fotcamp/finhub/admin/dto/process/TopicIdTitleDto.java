package fotcamp.finhub.admin.dto.process;

import fotcamp.finhub.common.domain.TopicGptColumn;
import fotcamp.finhub.common.domain.TopicQuiz;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TopicIdTitleDto {
    private Long categoryId;
    private Long topicId;
    private String title;

    public TopicIdTitleDto(TopicQuiz topicQuiz) {
        this.categoryId = topicQuiz.getTopic().getCategory().getId();
        this.topicId = topicQuiz.getTopic().getId();
        this.title = topicQuiz.getTopic().getTitle();
    }

    public TopicIdTitleDto(TopicGptColumn topicGptColumn) {
        this.categoryId = topicGptColumn.getTopic().getCategory().getId();
        this.topicId = topicGptColumn.getTopic().getId();
        this.title = topicGptColumn.getTopic().getTitle();
    }
}
