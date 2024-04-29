package fotcamp.finhub.admin.dto.request;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateFcmMessageRequestDto {

    private String target; // admin, all, email
    private String title;
    private String content;
    private String view;

    public CreateFcmMessageRequestDto(String target, String title, String content, String view) {
        this.target = target;
        this.title = title;
        this.content = content;
        this.view = view;
    }
}
