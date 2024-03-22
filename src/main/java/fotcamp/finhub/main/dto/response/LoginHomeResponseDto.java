package fotcamp.finhub.main.dto.response;



import fotcamp.finhub.main.dto.process.CategoryListProcessDto;
import fotcamp.finhub.main.dto.process.TopicListProcessDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginHomeResponseDto {

    // 카테고리 id 와 이름
    List<CategoryListProcessDto> categoryList;

    // 카테고리 id 중 가장 상위 카테고리의 topic list
    List<TopicListProcessDto> topicList;
}