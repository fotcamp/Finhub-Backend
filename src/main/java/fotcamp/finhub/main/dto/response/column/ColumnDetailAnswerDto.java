package fotcamp.finhub.main.dto.response.column;

import fotcamp.finhub.admin.dto.process.TopicIdTitleDto;
import fotcamp.finhub.common.domain.GptColumn;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class ColumnDetailAnswerDto {
    private final Long id;
    private final String title;
    private final String content;
    private final String summary;
    private final LocalDate date;
    private final String backgroundImgUrl;
    private final List<TopicIdTitleDto> topicList;
    private final boolean isScrapped;
    private final boolean isLiked;
    private final Long totalLike;

    @Builder
    public ColumnDetailAnswerDto(GptColumn gptColumn, String url, List<TopicIdTitleDto> topicList, boolean isScrapped, boolean isLiked, Long totalLke) {
        this.id = gptColumn.getId();
        this.title = gptColumn.getTitle();
        this.content = gptColumn.getContent();
        this.summary = gptColumn.getSummary();
        this.date = gptColumn.getCreatedTime().toLocalDate();
        this.backgroundImgUrl = url;
        this.topicList = topicList;
        this.isScrapped = isScrapped;
        this.isLiked = isLiked;
        this.totalLike = totalLke;
    }

}
