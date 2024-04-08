package fotcamp.finhub.main.dto.process.thirdTab;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SearchColumnResultListProcessDto {

    private String title;
    private String content;
    private String url;

    public SearchColumnResultListProcessDto(String title, String content, String url) {
        this.title = title;
        this.content = content;
        this.url = url;
    }
}
