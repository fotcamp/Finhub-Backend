package fotcamp.finhub.main.dto.response.secondTab;


import fotcamp.finhub.main.dto.process.secondTab.DetailNextTopicProcessDto;
import lombok.Getter;

@Getter
public class NextTopicResponseDto {

    private DetailNextTopicProcessDto nextTopic;

    public NextTopicResponseDto(DetailNextTopicProcessDto nextTopic) {
        this.nextTopic = nextTopic;
    }
}
