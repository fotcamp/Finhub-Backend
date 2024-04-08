package fotcamp.finhub.main.dto.process.thirdTab;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
public class SearchTopicResultListProcessDto {

    private Long id;
    private String title;
    private String summary;

    public SearchTopicResultListProcessDto(Long id, String title, String summary) {
        this.id = id;
        this.title = title;
        this.summary = summary;
    }
}
