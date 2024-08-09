package fotcamp.finhub.main.dto.process;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
public class MyScrapColumnProcessDto {

    private Long columnId;
    private String title;
    private String summary;
    private String imgUrl;

    public MyScrapColumnProcessDto(Long columnId, String title, String summary, String imgUrl) {
        this.columnId = columnId;
        this.title = title;
        this.summary = summary;
        this.imgUrl = imgUrl;
    }
}
