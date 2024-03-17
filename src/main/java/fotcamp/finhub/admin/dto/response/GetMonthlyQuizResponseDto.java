package fotcamp.finhub.admin.dto.response;

import fotcamp.finhub.admin.dto.process.QuizProcessDto;

import java.util.List;

public record GetMonthlyQuizResponseDto(List<QuizProcessDto> quizList) {
}
