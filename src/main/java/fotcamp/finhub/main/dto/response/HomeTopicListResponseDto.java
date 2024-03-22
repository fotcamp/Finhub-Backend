package fotcamp.finhub.main.dto.response;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class HomeTopicListResponseDto {

    private Long id;
    private String title;
    private String summary;

    public HomeTopicListResponseDto(Long id, String title, String summary) {
        this.id = id;
        this.title = title;
        this.summary = summary;
    }

}
