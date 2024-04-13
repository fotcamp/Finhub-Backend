package fotcamp.finhub.admin.dto.response;

import fotcamp.finhub.admin.dto.process.TopicIdTitleDto;
import fotcamp.finhub.common.domain.Quiz;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
public class GetDailyQuizResponseDto {
    private final Long id;
    private final String question;
    private final String answer;
    private final String comment;
    private final LocalDate targetDate;
    private final String createdBy;
    private final LocalDateTime createdTime;
    private final LocalDateTime modifiedTime;
    private final List<TopicIdTitleDto> topicList;

    public GetDailyQuizResponseDto(Quiz quiz) {
        this.id = quiz.getId();
        this.question = quiz.getQuestion();
        this.answer = quiz.getAnswer();
        this.comment = quiz.getComment();
        this.targetDate = quiz.getTargetDate();
        this.createdBy = quiz.getCreatedBy();
        this.createdTime = quiz.getCreatedTime();
        this.modifiedTime = quiz.getModifiedTime();
        this.topicList = quiz.getTopicList().stream().map(TopicIdTitleDto::new).toList();
    }
}
