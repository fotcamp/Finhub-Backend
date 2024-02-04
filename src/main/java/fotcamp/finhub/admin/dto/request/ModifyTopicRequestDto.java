package fotcamp.finhub.admin.dto.request;

import fotcamp.finhub.admin.dto.process.GptProcessDto;
import fotcamp.finhub.common.domain.Topic;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ModifyTopicRequestDto {
    private Long topicId;
    private Long categoryId;
    private String title;
    private String definition;
    private String shortDefinition;
    private String thumbnailImgPath;
    private List<GptProcessDto> gptList;

    public ModifyTopicRequestDto(Topic topic, List<GptProcessDto> gptList) {
        this.topicId = topic.getId();
        this.categoryId = topic.getCategory().getId();
        this.title = topic.getTitle();
        this.definition = topic.getDefinition();
        this.shortDefinition = topic.getShortDefinition();
        this.thumbnailImgPath = topic.getThumbnailImgPath();
        this.gptList = gptList;
    }
}
