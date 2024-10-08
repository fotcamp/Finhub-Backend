package fotcamp.finhub.admin.dto.process;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class VocListProcessDto {

    private Long feedbackId;
    private String email;
    private String context;
    private String reply;
    private LocalDateTime createdTime;

    @Builder
    public VocListProcessDto(Long feedbackId, String email, String context, String reply, LocalDateTime createdTime) {
        this.feedbackId = feedbackId;
        this.email = email;
        this.context = context;
        this.reply = reply;
        this.createdTime = createdTime;
    }
}
