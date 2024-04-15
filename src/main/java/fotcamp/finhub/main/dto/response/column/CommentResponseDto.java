package fotcamp.finhub.main.dto.response.column;

import fotcamp.finhub.common.domain.Comments;
import fotcamp.finhub.common.domain.Member;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class CommentResponseDto {
    private Long id;
    private String nickname;
    private LocalDate date;
    private String avatarImgPath;
    private String comment;
    private int like;
    private boolean isUserComment;

    public CommentResponseDto(Member member, Comments comment, String url, Boolean isUser) {
        this.id = comment.getId();
        this.nickname = member.getNickname();
        this.date = comment.getCreatedTime().toLocalDate();
        this.avatarImgPath = url;
        this.comment = comment.getContent();
        this.like = comment.getTotalLike();
        this.isUserComment = isUser;
    }

}
