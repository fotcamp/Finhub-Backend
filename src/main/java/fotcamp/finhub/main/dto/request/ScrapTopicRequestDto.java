package fotcamp.finhub.main.dto.request;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ScrapTopicRequestDto {

    private Long topicId;

    public ScrapTopicRequestDto(Long topicId) {
        this.topicId = topicId;
    }
}
