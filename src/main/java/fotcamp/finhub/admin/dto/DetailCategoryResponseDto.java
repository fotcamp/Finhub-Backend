package fotcamp.finhub.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class DetailCategoryResponseDto {
    private Long categoryId;
    private List<DetailCategoryTopicResponseDto> topicList = new ArrayList<>();
}
