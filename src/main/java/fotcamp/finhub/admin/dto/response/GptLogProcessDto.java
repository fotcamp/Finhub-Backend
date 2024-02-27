package fotcamp.finhub.admin.dto.response;

import fotcamp.finhub.admin.domain.GptLog;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class GptLogProcessDto {
    private final Long id;
    private final String categoryName;
    private final String topicTitle;
    private final String usertypeName;
    private final String question;
    private final String answer;
    private final LocalDateTime createdTime;
    private final String createdBy;

    public GptLogProcessDto(GptLog gptLog, String categoryName, String topicTitle, String usertypeName) {
        this.id = gptLog.getId();
        this.categoryName = categoryName;
        this.topicTitle = topicTitle;
        this.usertypeName = usertypeName;
        this.question = gptLog.getQuestion();
        this.answer = gptLog.getAnswer();
        this.createdTime = gptLog.getCreatedTime();
        this.createdBy = gptLog.getCreatedBy();
    }

}
