package fotcamp.finhub.main.dto.process;


import fotcamp.finhub.common.domain.Comments;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@NoArgsConstructor
public class MyCommentsListProcessDto {
    private Long id;
    private String title;
    private String imgUrl;
    private String comment;
    private int totalLike;

    public MyCommentsListProcessDto(Long id, String title, String imgUrl, String comment, int totalLike) {
        this.id = id;
        this.title = title;
        this.imgUrl = imgUrl;
        this.comment = comment;
        this.totalLike = totalLike;
    }

}
