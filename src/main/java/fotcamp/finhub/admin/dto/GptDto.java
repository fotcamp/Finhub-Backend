package fotcamp.finhub.admin.dto;

import fotcamp.finhub.common.domain.Gpt;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GptDto {
    private Long gptId;
    private String content;
    private String useYN;

    public GptDto(Gpt gpt) {
        this.gptId = gpt.getId();
        this.content = gpt.getContent();
        this.useYN = gpt.getUseYN();
    }
}
