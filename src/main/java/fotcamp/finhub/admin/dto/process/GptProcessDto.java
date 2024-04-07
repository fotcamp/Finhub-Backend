package fotcamp.finhub.admin.dto.process;

import fotcamp.finhub.common.domain.Gpt;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GptProcessDto {
    private Long gptId;
    private Long usertypeId;
    private String content;
    private String useYN;

    public GptProcessDto(Gpt gpt) {
        this.gptId = gpt.getId();
        this.usertypeId = gpt.getUserType().getId();
        this.content = gpt.getContent();
        this.useYN = gpt.getUseYN();
    }
}
