package fotcamp.finhub.admin.dto.process;

import lombok.Builder;
import lombok.Getter;

@Getter
public class VocDetailProcessDto {

    private Long feedbackId;
    private String userAgent;
    private String appVersion;
    private String email;
    private String context;
    private String fileUrl1;
    private String fileUrl2;
    private String fileUrl3;
    private String fileUrl4;
    private String adminResponse;
    private String reply;


    @Builder
    public VocDetailProcessDto(Long feedbackId, String userAgent, String appVersion, String email, String context, String fileUrl1, String fileUrl2, String fileUrl3, String fileUrl4, String adminResponse, String reply) {
        this.feedbackId = feedbackId;
        this.userAgent = userAgent;
        this.appVersion = appVersion;
        this.email = email;
        this.context = context;
        this.fileUrl1 = fileUrl1;
        this.fileUrl2 = fileUrl2;
        this.fileUrl3 = fileUrl3;
        this.fileUrl4 = fileUrl4;
        this.adminResponse = adminResponse;
        this.reply = reply;
    }
}
