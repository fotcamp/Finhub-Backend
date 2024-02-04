package fotcamp.finhub.admin.dto.process;

import fotcamp.finhub.common.domain.Gpt;
import lombok.Getter;

@Getter
public class GptProcessDto {
    private final Long gptId;
    private final String content;
    private final String useYN;

    public GptProcessDto(Gpt gpt) {
        this.gptId = gpt.getId();
        this.content = gpt.getContent();
        this.useYN = gpt.getUseYN();
    }
}
