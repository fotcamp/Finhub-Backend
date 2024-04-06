package fotcamp.finhub.main.dto.response.quiz;

import fotcamp.finhub.common.domain.Quiz;
import lombok.Getter;

@Getter
public class QuizInfoProcessDto {
    private final Long id;
    private final String question;

    public QuizInfoProcessDto(Quiz quiz) {
        this.id = quiz.getId();
        this.question = quiz.getQuestion();
    }
}
