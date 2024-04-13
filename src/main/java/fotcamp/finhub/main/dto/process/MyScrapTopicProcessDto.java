package fotcamp.finhub.main.dto.process;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
public class MyScrapTopicProcessDto {

    private Long categoryId;
    private Long topicId;
    private String title;
    private String definition;

    public MyScrapTopicProcessDto(Long categoryId, Long topicId, String title, String definition) {
        this.categoryId = categoryId;
        this.topicId = topicId;
        this.title = title;
        this.definition = definition;
    }

}
