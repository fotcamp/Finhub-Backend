package fotcamp.finhub.admin.dto.process;

import fotcamp.finhub.common.domain.Quiz;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuizProcessDto {
    private LocalDate targetDate;
    private Long id;
    private String question;
    private String answer;
    private String comment;
    private String createdBy;
    private List<QuizTopicProcessDto> topicList;

    public QuizProcessDto(Quiz quiz) {
        this.id = quiz.getId();
        this.question = quiz.getQuestion();
        this.answer = quiz.getAnswer();
        this.comment = quiz.getComment();
        this.targetDate = quiz.getTargetDate();
        this.createdBy = quiz.getCreatedBy();
        this.topicList = quiz.getTopicList().stream().map(QuizTopicProcessDto::new).toList();

    }
}
