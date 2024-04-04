package fotcamp.finhub.main.dto.process;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SearchTopicResultListProcessDto {

    private String title;
    private String summary;

    public SearchTopicResultListProcessDto(String title, String summary) {
        this.title = title;
        this.summary = summary;
    }
}
