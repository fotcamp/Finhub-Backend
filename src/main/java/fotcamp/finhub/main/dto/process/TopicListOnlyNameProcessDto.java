package fotcamp.finhub.main.dto.process;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TopicListOnlyNameProcessDto {

    private Long topicId;
    private String title;

    public TopicListOnlyNameProcessDto(Long topicId, String title) {
        this.topicId = topicId;
        this.title = title;
    }
}
