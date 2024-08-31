package fotcamp.finhub.common.domain;


import jakarta.persistence.*;
import lombok.AccessLevel;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Feedback extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userAgent;
    private String appVersion;
    private String email;
    private String fileUrl1;
    private String fileUrl2;
    private String fileUrl3;
    private String fileUrl4;
    @Lob
    private String feedback;
    @Lob
    private String adminResponse;
    private String reply;

    @Builder
    public Feedback(String userAgent, String appVersion, String email, String fileUrl1, String fileUrl2, String fileUrl3, String fileUrl4, String feedback) {
        this.userAgent = userAgent;
        this.appVersion = appVersion;
        this.email = email;
        this.fileUrl1 = fileUrl1;
        this.fileUrl2 = fileUrl2;
        this.fileUrl3 = fileUrl3;
        this.fileUrl4 = fileUrl4;
        this.feedback = feedback;
        this.adminResponse = null;
        this.reply = "F";
    }

    public void updateFeedback(String reply, String text){
        this.adminResponse = text;
        this.reply = reply;
    }

}
