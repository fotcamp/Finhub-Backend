package fotcamp.finhub.admin.dto.response;

import fotcamp.finhub.admin.domain.GptPrompt;
import fotcamp.finhub.admin.dto.process.RecentPromptProcessDto;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class RecentPromptResponseDto {
    private final String prompt;
    private final LocalDateTime createdTime;
    private final String createdBy;
    private final List<RecentPromptProcessDto> promiseList;

    public RecentPromptResponseDto(GptPrompt gptPrompt, List<RecentPromptProcessDto> promiseList) {
        this.prompt = gptPrompt.getPrompt();
        this.createdTime = gptPrompt.getCreatedTime();
        this.createdBy = gptPrompt.getCreatedBy();
        this.promiseList = promiseList;
    }
}
