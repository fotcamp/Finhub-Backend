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

    public TopicListProcessDto(Long id, String title, String summary) {
        this.topicId = id;
        this.title = title;
        this.summary = summary;
    }

    public TopicListProcessDto(Long topicId, String title, String summary, boolean isScrapped) {
        this.topicId = topicId;
        this.title = title;
        this.summary = summary;
        this.isScrapped = isScrapped;
    }

    public void add(TopicListProcessDto dto, boolean isScrapped){

    }
}
