package fotcamp.finhub.admin.dto.process;

import fotcamp.finhub.common.domain.Topic;
import fotcamp.finhub.common.domain.TopicQuiz;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuizTopicProcessDto {
    private Long id;
    private String title;

    public QuizTopicProcessDto(TopicQuiz topicQuiz) {
        this.id = topicQuiz.getTopic().getId();
        this.title = topicQuiz.getTopic().getTitle();
    }
}
