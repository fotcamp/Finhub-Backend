package fotcamp.finhub.main.dto.response.secondTab;


import fotcamp.finhub.main.dto.process.secondTab.DetailTopicProcessDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TopicInfoResponseDto {

    private DetailTopicProcessDto topicInfo;

    public TopicInfoResponseDto(DetailTopicProcessDto topicInfo) {
        this.topicInfo = topicInfo;
    }
}

