package fotcamp.finhub.main.dto.process;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FirstTopicListProcessDto {

    private Long topicId;
    private String title;

    public FirstTopicListProcessDto(Long topicId, String title) {
        this.topicId = topicId;
        this.title = title;
    }
}
