package fotcamp.finhub.main.dto.process;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MyScrapProcessDto {

    private Long categoryId;
    private Long topicId;
    private String title;
    private String definition;

    public MyScrapProcessDto(Long categoryId, Long topicId, String title, String definition) {
        this.categoryId = categoryId;
        this.topicId = topicId;
        this.title = title;
        this.definition = definition;
    }

}
