package fotcamp.finhub.main.dto.process;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SearchResultListDto {

    private String title;
    private String summary;

    public SearchResultListDto(String title, String summary) {
        this.title = title;
        this.summary = summary;
    }
}
