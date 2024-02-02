package fotcamp.finhub.admin.dto;

import fotcamp.finhub.common.domain.Topic;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ModifyTopicDto {
    private Long topicId;
    private Long categoryId;
    private String title;
    private String definition;
    private String shortDefinition;
    private String thumbnailImgPath;
    private List<GptDto> gptList;

    public ModifyTopicDto(Topic topic, List<GptDto> gptList) {
        this.topicId = topic.getId();
        this.categoryId = topic.getCategory().getId();
        this.title = topic.getTitle();
        this.definition = topic.getDefinition();
        this.shortDefinition = topic.getShortDefinition();
        this.thumbnailImgPath = topic.getThumbnailImgPath();
        this.gptList = gptList;
    }

}
