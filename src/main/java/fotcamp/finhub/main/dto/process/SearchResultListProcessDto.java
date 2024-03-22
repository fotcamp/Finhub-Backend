package fotcamp.finhub.main.dto.process;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SearchResultListProcessDto {

    private String title;
    private String summary;

    public SearchResultListProcessDto(String title, String summary) {
        this.title = title;
        this.summary = summary;
    }
}
