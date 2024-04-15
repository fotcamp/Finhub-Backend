package fotcamp.finhub.admin.dto.request;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateAnnounceRequestDto {

    private String title;
    private String content;

    public CreateAnnounceRequestDto(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
