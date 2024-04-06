package fotcamp.finhub.main.dto.response.quiz;

import fotcamp.finhub.admin.dto.process.QuizTopicProcessDto;

import java.util.List;

public record SolveQuizProcessDto(String correctYN, String comment, List<QuizTopicProcessDto> topicList) {
}
