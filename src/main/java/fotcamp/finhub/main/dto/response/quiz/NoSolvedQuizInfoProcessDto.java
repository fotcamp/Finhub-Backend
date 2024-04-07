package fotcamp.finhub.main.dto.response.quiz;

import fotcamp.finhub.common.domain.Quiz;
import lombok.Getter;

@Getter
public class NoSolvedQuizInfoProcessDto {
    private final Long id;
    private final String question;

    public NoSolvedQuizInfoProcessDto(Quiz quiz) {
        this.id = quiz.getId();
        this.question = quiz.getQuestion();
    }
}
