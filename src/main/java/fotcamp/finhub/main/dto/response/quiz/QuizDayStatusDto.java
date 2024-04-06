package fotcamp.finhub.main.dto.response.quiz;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class QuizDayStatusDto {
    private final LocalDate date;
    private final String solvedYn;

    public QuizDayStatusDto(LocalDate date, String solved) {
        this.date = date;
        this.solvedYn = solved;
    }
}
