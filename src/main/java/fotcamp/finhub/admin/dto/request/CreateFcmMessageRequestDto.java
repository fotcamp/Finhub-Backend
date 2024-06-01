package fotcamp.finhub.admin.dto.request;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateFcmMessageRequestDto {

    private String target; // admin, all, email
    private String title;
    private String content;
    private String view;
    private String action;

    @Builder
    public CreateFcmMessageRequestDto(String target, String title, String content, String view, String action) {
        this.target = target;
        this.title = title;
        this.content = content;
        this.view = view;
        this.action = action;
    }
}
