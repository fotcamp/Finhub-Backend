package fotcamp.finhub.admin.dto.process;

import lombok.Builder;
import lombok.Getter;

@Getter
public class VocListProcessDto {

    private Long feedbackId;
    private String email;
    private String context;
    private String reply;

    @Builder
    public VocListProcessDto(Long feedbackId, String email, String context, String reply) {
        this.feedbackId = feedbackId;
        this.email = email;
        this.context = context;
        this.reply = reply;
    }
}
