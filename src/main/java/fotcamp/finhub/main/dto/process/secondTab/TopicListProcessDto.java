package fotcamp.finhub.main.dto.process.secondTab;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
public class TopicListProcessDto {

    private Long topicId;
    private String title;
    private String summary;

    private boolean isScrapped;
    private String categoryName;
    private String img_path;

    public TopicListProcessDto(Long topicId, String title, String summary, boolean isScrapped, String categoryName, String img_path) {
        this.topicId = topicId;
        this.title = title;
        this.summary = summary;
        this.isScrapped = isScrapped;
        this.categoryName = categoryName;
        this.img_path = img_path;
    }
}
