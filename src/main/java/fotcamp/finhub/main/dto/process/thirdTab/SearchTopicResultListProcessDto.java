package fotcamp.finhub.main.dto.process.thirdTab;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
public class SearchTopicResultListProcessDto {

    private Long topicId;
    private Long categoryId;
    private String title;
    private String summary;

    public SearchTopicResultListProcessDto(Long topicId, Long categoryId, String title, String summary) {
        this.topicId = topicId;
        this.categoryId = categoryId;
        this.title = title;
        this.summary = summary;
    }
}
