package fotcamp.finhub.admin.dto.request;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateFcmMessageRequestDto {

    private String type; // admin, all
    private String title;
    private String content;
    private String view;

    public CreateFcmMessageRequestDto(String type, String title, String content, String view) {
        this.type = type;
        this.title = title;
        this.content = content;
        this.view = view;
    }
}
