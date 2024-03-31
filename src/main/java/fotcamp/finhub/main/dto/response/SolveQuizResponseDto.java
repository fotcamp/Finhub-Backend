package fotcamp.finhub.main.dto.response;

import fotcamp.finhub.admin.dto.process.QuizTopicProcessDto;

import java.util.List;

public record SolveQuizResponseDto(String correctYN, String comment, List<QuizTopicProcessDto> topicList) {
}
