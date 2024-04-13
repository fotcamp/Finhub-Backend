package fotcamp.finhub.main.dto.process.secondTab;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
public class DetailTopicProcessDto {

    private Long id;
    private String title;
    private String summary;
    private String definition;
    private String img_path;
    private boolean isScrapped;


    public DetailTopicProcessDto(Long id, String title, String summary, String definition, String img_path, boolean isScrapped) {
        this.id = id;
        this.title = title;
        this.summary = summary;
        this.definition = definition;
        this.img_path = img_path;
        this.isScrapped = isScrapped;
    }

}
