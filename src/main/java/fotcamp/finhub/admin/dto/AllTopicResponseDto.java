package fotcamp.finhub.admin.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class AllTopicResponseDto {
    private final List<TopicResponseDto> topicList;

    public AllTopicResponseDto(List<TopicResponseDto> topicList) {
        this.topicList = topicList;
    }

}
