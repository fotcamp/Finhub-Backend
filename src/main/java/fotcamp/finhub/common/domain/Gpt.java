package fotcamp.finhub.common.domain;

import fotcamp.finhub.admin.dto.process.GptProcessDto;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Gpt extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long categoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id")
    private Topic topic;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usertype_id")
    private UserType userType;

    private String content;

    @Builder.Default
    private String useYN = "N";

    private String createdBy;

    public void modifyContentUseYN(GptProcessDto gptProcessDto, String role) {
        this.content = gptProcessDto.getContent();
        this.useYN = gptProcessDto.getUseYN();
        this.createdBy = role;
    }

    // 연관관계 편의 메서드
    public void setTopic(Topic topic) {
        this.topic = topic;
        topic.addGpt(this);
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }
}
