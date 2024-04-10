package fotcamp.finhub.main.dto.process.thirdTab;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
public class SearchColumnResultListProcessDto {

    private Long id;
    private String title;
    private String content;

    public SearchColumnResultListProcessDto(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }
}
