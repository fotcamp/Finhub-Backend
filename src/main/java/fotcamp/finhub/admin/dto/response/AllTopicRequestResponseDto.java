package fotcamp.finhub.admin.dto.response;

import fotcamp.finhub.admin.dto.process.AllTopicRequestProcessDto;
import fotcamp.finhub.common.dto.process.PageInfoProcessDto;

import java.util.List;

public record AllTopicRequestResponseDto(List<AllTopicRequestProcessDto> topicRequestList, PageInfoProcessDto pageInfo) {
}
