package fotcamp.finhub.admin.dto;

import fotcamp.finhub.common.domain.Topic;
import lombok.Getter;

import java.util.List;

@Getter
public class DetailTopicResponseDto {
    private final Long categoryId;
    private final String title;
    private final String definition;
    private final String shortDefinition;
    private final String thumbnailImgPath;
    private final String useYN;
    private final List<DetailTopicGptResponseDto> gptList;

    public DetailTopicResponseDto(Topic topic, List<DetailTopicGptResponseDto> gptList) {
        this.categoryId = topic.getCategory().getId();
        this.title = topic.getTitle();
        this.definition = topic.getDefinition();
        this.shortDefinition = topic.getShortDefinition();
        this.thumbnailImgPath = topic.getThumbnailImgPath();
        this.useYN = topic.getUseYN();
        this.gptList = gptList;
    }

}
