package fotcamp.finhub.main.dto.response;


import fotcamp.finhub.main.dto.process.CategoryListProcessDto;
import fotcamp.finhub.main.dto.process.FirstTopicListProcessDto;
import fotcamp.finhub.main.dto.process.TopicListProcessDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ListTabResponseDto {

    private List<CategoryListProcessDto> categoryList;
    private List<FirstTopicListProcessDto> topicList;

    public ListTabResponseDto(List<CategoryListProcessDto> categoryList, List<FirstTopicListProcessDto> topicList) {
        this.categoryList = categoryList;
        this.topicList = topicList;
    }
}
