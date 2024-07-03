package fotcamp.finhub.main.dto.process;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
public class MyCommentsListProcessDto {
    private Long commentId;
    private Long columnId;
    private String title;
    private String comment;
    private int totalLike;
    private String date;

    public MyCommentsListProcessDto(Long commentId, Long columnId, String title, String comment, int totalLike, String date) {
        this.commentId = commentId;
        this.columnId = columnId;
        this.title = title;
        this.comment = comment;
        this.totalLike = totalLike;
        this.date = date;
    }


}
