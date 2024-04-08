package fotcamp.finhub.main.dto.response.firstTab;

import fotcamp.finhub.main.dto.process.secondTab.TopicListProcessDto;
import lombok.Getter;

import java.util.List;

@Getter
public class TopicListResponseDto {

    private List<TopicListProcessDto> topicList;

    public TopicListResponseDto(List<TopicListProcessDto> topicList) {
        this.topicList = topicList;
    }
}
