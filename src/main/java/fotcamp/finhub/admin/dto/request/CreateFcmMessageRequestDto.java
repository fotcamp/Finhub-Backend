package fotcamp.finhub.admin.dto.request;


import fotcamp.finhub.main.dto.process.FcmMessageProcessDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class CreateFcmMessageRequestDto {

    private Long type; // 0 ~ 4번
    private List<String> target; // uuid 리스트, 혹은 빈 리스트
    private String title;
    private String content;
    private String view;
    private FcmMessageProcessDto.Action action;

    @Builder
    public CreateFcmMessageRequestDto(Long type, List<String> target, String title, String content, String view, FcmMessageProcessDto.Action action) {
        this.type = type;
        this.target = target;
        this.title = title;
        this.content = content;
        this.view = view;
        this.action = action;
    }

}
