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
    private Long category_id;
    private String category_name;

    // 카테고리 id 중 가장 상위 카테고리의 topic list
    private Long topic_id;
    private String topic_title;
    private String topic_summary;

    public NonLoginHomeRequestDto(Category category, Topic topic) {
        this.category_id = category.getId();
        this.category_name = category.getName();
        this.topic_id = topic.getId();
        this.topic_title = topic.getTitle();
        this.topic_summary = topic.getSummary();
    }
}
