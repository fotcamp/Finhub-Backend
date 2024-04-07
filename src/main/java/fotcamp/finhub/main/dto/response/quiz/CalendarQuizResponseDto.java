package fotcamp.finhub.main.dto.response.quiz;

import java.util.List;

public record CalendarQuizResponseDto(String emoticonImgUrl, List<QuizDayStatusDto> quizData) {
}
