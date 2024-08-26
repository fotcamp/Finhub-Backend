package fotcamp.finhub.common.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Comments extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gpt_column_id")
    private GptColumn gptColumn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String content; // 댓글 내용

    @Builder.Default
    private int totalLike = 0;

    @Builder.Default
    private String useYn = "Y";

    public void upLike() {
        this.totalLike += 1;
    }

    public void downLike() {
        if (this.totalLike > 0) {
            this.totalLike -= 1;
        }
    }

    public void modifyContent(String content) {
        this.content = content;
    }

    public void modifyUseYn() {
        this.useYn = "N";
    }

    public void useYnUpdate() {
        if ("Y".equals(this.useYn)) {
            this.useYn = "N";
        } else if ("N".equals(this.useYn)) {
            this.useYn = "Y";
        }
    }
}
