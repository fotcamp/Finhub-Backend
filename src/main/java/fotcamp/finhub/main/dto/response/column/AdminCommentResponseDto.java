package fotcamp.finhub.main.dto.response.column;

import fotcamp.finhub.common.domain.Comments;
import fotcamp.finhub.common.domain.Member;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class AdminCommentResponseDto {
    private Long id;
    private String nickname;
    private LocalDate date;
    private String avatarImgPath;
    private String comment;
    private int like;
    private String reportedYn;
    private String useYn;

    public AdminCommentResponseDto(Member member, Comments comment, String url, String reportedYn) {
        this.id = comment.getId();
        this.nickname = member.getNickname();
        this.date = comment.getCreatedTime().toLocalDate();
        this.avatarImgPath = url;
        this.comment = comment.getContent();
        this.like = comment.getTotalLike();
        this.reportedYn = reportedYn;
        this.useYn = comment.getUseYn();
    }

}
