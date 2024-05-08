package fotcamp.finhub.main.dto.process;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
public class MyCommentsListProcessDto {

    private String title;
    private String imgUrl;
    private String comment;
    private int totalLike;

    public MyCommentsListProcessDto(String title, String imgUrl, String comment, int totalLike) {
        this.title = title;
        this.imgUrl = imgUrl;
        this.comment = comment;
        this.totalLike = totalLike;
    }
}
