package fotcamp.finhub.admin.dto.response;

import fotcamp.finhub.admin.dto.process.GptColumnTopicProcessDto;
import fotcamp.finhub.common.domain.GptColumn;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class DetailGptColumnResponseDto {
    private final Long id;
    private final String title;
    private final String summary;
    private final String content;
    private final String backgroundUrl;
    private final String createdBy;
    private final String useYN;
    private final LocalDateTime createdTime;
    private final LocalDateTime modifiedTime;
    private final List<GptColumnTopicProcessDto> topicList;

    public DetailGptColumnResponseDto(GptColumn gptColumn, String backgroundUrl) {
        this.id = gptColumn.getId();
        this.title = gptColumn.getTitle();
        this.summary = gptColumn.getSummary();
        this.content = gptColumn.getContent();
        this.backgroundUrl = backgroundUrl;
        this.createdBy = gptColumn.getCreatedBy();
        this.useYN = gptColumn.getUseYN();
        this.createdTime = gptColumn.getCreatedTime();
        this.modifiedTime = gptColumn.getModifiedTime();
        this.topicList = gptColumn.getTopicGptColumnList().stream().map(GptColumnTopicProcessDto::new).toList();
    }
}
