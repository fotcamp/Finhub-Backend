package fotcamp.finhub.main.dto.response.quiz;

import java.time.LocalDate;

public record QuizInfoDto(Long id, String question, LocalDate targetDate) {
}
