package fotcamp.finhub.admin.dto.request;

import fotcamp.finhub.admin.dto.process.GptProcessDto;
import fotcamp.finhub.common.domain.Topic;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ModifyTopicRequestDto {
    @NotNull
    private Long topicId;
    @NotNull
    private Long categoryId;
    @NotBlank
    private String title;
    @NotBlank
    private String definition;
    @NotBlank
    private String summary;

    private String s3ImgUrl;

    private List<GptProcessDto> gptList;

    public ModifyTopicRequestDto(Topic topic, List<GptProcessDto> gptList) {
        this.topicId = topic.getId();
        this.categoryId = topic.getCategory().getId();
        this.title = topic.getTitle();
        this.definition = topic.getDefinition();
        this.summary = topic.getSummary();
        this.gptList = gptList;
    }
}
