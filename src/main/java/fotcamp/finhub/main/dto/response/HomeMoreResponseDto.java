package fotcamp.finhub.main.dto.response;


import fotcamp.finhub.main.dto.process.TopicListProcessDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class HomeMoreResponseDto {

    List<TopicListProcessDto> topicList;

    public HomeMoreResponseDto(List<TopicListProcessDto> topicList) {
        this.topicList = topicList;
    }
}
