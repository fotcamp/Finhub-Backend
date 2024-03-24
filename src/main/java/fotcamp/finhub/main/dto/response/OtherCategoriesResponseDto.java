package fotcamp.finhub.main.dto.response;


import fotcamp.finhub.main.dto.process.TopicListProcessDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class OtherCategoriesResponseDto {

    List<TopicListProcessDto> topicList;

    public OtherCategoriesResponseDto(List<TopicListProcessDto> topicList) {
        this.topicList = topicList;
    }
}
