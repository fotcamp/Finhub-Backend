package fotcamp.finhub.main.dto.process;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TopicListProcessDto {

    private Long topicId;
    private String title;
    private String summary;

    private boolean isScrapped;
    private String categoryName;

    public TopicListProcessDto(Long topicId, String title, String summary, boolean isScrapped, String categoryName) {
        this.topicId = topicId;
        this.title = title;
        this.summary = summary;
        this.isScrapped = isScrapped;
        this.categoryName = categoryName;
    }

    public TopicListProcessDto(Long topicId, String title) {
        this.topicId = topicId;
        this.title = title;
    }
}
