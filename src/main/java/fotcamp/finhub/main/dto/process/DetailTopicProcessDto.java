package fotcamp.finhub.main.dto.process;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DetailTopicProcessDto {

    private Long id;
    private String title;
    private String definition;


    @Builder
    public DetailTopicProcessDto(Long topicId, String title, String definition) {
        this.id = topicId;
        this.title = title;
        this.definition = definition;
    }
}
