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
    private boolean isScrapped;

    public DetailTopicProcessDto(Long id, String title, String definition, boolean isScrapped) {
        this.id = id;
        this.title = title;
        this.definition = definition;
        this.isScrapped = isScrapped;
    }
}
