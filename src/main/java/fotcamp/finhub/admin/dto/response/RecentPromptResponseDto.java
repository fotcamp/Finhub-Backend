package fotcamp.finhub.admin.dto.response;

import fotcamp.finhub.admin.domain.GptPrompt;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class RecentPromptResponseDto {
    private final String prompt;
    private final LocalDateTime createdTime;
    private final String createdBy;
    private final String category;
    private final String topic;
    private final String usertype;

    public RecentPromptResponseDto(GptPrompt gptPrompt, String category, String topic, String usertype) {
        this.prompt = gptPrompt.getPrompt();
        this.createdTime = gptPrompt.getCreatedTime();
        this.createdBy = gptPrompt.getCreatedBy();
        this.category = category;
        this.topic = topic;
        this.usertype = usertype;
    }
}
