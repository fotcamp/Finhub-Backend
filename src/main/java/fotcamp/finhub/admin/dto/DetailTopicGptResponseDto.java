package fotcamp.finhub.admin.dto;

import fotcamp.finhub.common.domain.Gpt;
import lombok.Getter;

@Getter
public class DetailTopicGptResponseDto {
    private final Long usertypeId;
    private final String usertypeName;
    private final String avatarImgPath;
    private final Long gptId;
    private final String content;
    private final String useYN;

    public DetailTopicGptResponseDto(Gpt gpt) {
        this.usertypeId = gpt.getUserType().getId();
        this.usertypeName = gpt.getUserType().getName();
        this.avatarImgPath = gpt.getUserType().getAvatarImgPath();
        this.gptId = gpt.getId();
        this.content = gpt.getContent();
        this.useYN = gpt.getUseYN();
    }

}
