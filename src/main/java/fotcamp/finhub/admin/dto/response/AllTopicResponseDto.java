package fotcamp.finhub.admin.dto.response;

import fotcamp.finhub.admin.dto.process.TopicProcessDto;
import fotcamp.finhub.common.dto.process.PageInfoProcessDto;

import java.util.List;

public record AllTopicResponseDto(List<TopicProcessDto> topicList, PageInfoProcessDto pageInfo) {

}
