package fotcamp.finhub.common.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CommentsReport extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comments reportedComment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_member_id")
    private Member reporterMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reported_member_id")
    private Member reportedMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_reason_id")
    private ReportReasons reportReasons;

    private String useYn; // 댓글 현재 사용여부 -> Y면 댓글 사용중 (신고처리 안한 것)

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
