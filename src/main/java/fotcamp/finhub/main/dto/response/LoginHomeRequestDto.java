package fotcamp.finhub.main.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginHomeRequestDto {

    // 카테고리 id 와 이름
    private Long categoryId;
    private String categoryName;

    // 카테고리 id 중 가장 상위 카테고리의 topic list
    private Long topicId;
    private String topicTitle;
    private String topicSummary;
}
