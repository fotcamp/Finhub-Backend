package fotcamp.finhub.main.dto.response.secondTab;


import fotcamp.finhub.main.dto.process.TopicListOnlyNameProcessDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ListResponseDto {

    private Long categoryId;
    private List<TopicListOnlyNameProcessDto> topicList;

    public ListResponseDto(Long categoryId, List<TopicListOnlyNameProcessDto> topicList) {
        this.categoryId = categoryId;
        this.topicList = topicList;
    }
}
