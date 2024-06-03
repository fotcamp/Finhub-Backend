package fotcamp.finhub.admin.dto.request;


import fotcamp.finhub.main.dto.process.FcmMessageProcessDto;
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
    private FcmMessageProcessDto.Action action;
    private Long mutableContent;

    @Builder
    public CreateFcmMessageRequestDto(String target, String title, String content, String view, FcmMessageProcessDto.Action action, Long mutableContent) {
        this.target = target;
        this.title = title;
        this.content = content;
        this.view = view;
        this.action = action;
        this.mutableContent = mutableContent;
    }

}
