package fotcamp.finhub.main.dto.response.quiz;

import java.util.List;

public record CalendarQuizResponseDto(String userEmoticon, List<EmoticonDto> emoticonData, List<QuizDayStatusDto> quizData) {
}
