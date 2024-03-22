package fotcamp.finhub.main.dto.process;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TopicListProcessDto {

    private Long topicId;
    private String title;
    private String summary;

    public TopicListProcessDto(Long id, String title, String summary) {
        this.topicId = id;
        this.title = title;
        this.summary = summary;
    }
}
