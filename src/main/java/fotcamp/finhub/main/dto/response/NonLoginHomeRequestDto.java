package fotcamp.finhub.main.dto.response;


import fotcamp.finhub.common.domain.Category;
import fotcamp.finhub.common.domain.Topic;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NonLoginHomeRequestDto {

    // 카테고리 id 와 이름
    private Long categoryId;
    private String categoryName;

    // 카테고리 id 중 가장 상위 카테고리의 topic list
    private Long topicId;
    private String topicTitle;
    private String topicSummary;

    public NonLoginHomeRequestDto(Category category, Topic topic) {
        this.categoryId = category.getId();
        this.categoryName = category.getName();
        this.topicId = topic.getId();
        this.topicTitle = topic.getTitle();
        this.topicSummary = topic.getSummary();
    }
}
