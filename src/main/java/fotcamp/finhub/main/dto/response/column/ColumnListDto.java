package fotcamp.finhub.main.dto.response.column;

import fotcamp.finhub.admin.dto.process.TopicIdTitleDto;

import java.time.LocalDate;
import java.util.List;

public record ColumnListDto(Long id, String title, LocalDate date, String backgroundImgUrl, List<TopicIdTitleDto> topicList) {
}
