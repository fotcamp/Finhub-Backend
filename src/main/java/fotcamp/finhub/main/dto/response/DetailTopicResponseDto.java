package fotcamp.finhub.main.dto.response;


import fotcamp.finhub.main.dto.process.DetailNextTopicProcessDto;
import fotcamp.finhub.main.dto.process.DetailTopicProcessDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor

public class DetailTopicResponseDto {

    private DetailTopicProcessDto topic;
    private DetailNextTopicProcessDto nextTopic;
    private boolean isScrapped;

    public DetailTopicResponseDto(DetailTopicProcessDto topic, DetailNextTopicProcessDto nextTopic, boolean isScrapped) {
        this.topic = topic;
        this.nextTopic = nextTopic;
        this.isScrapped = isScrapped;
    }
}
