package fotcamp.finhub.admin.dto.response;

import fotcamp.finhub.admin.dto.process.GetCalendarEmoticonProcessDto;

import java.util.List;

public record AllCalendarEmoticonResponseDto(List<GetCalendarEmoticonProcessDto> calendarEmoticons) {
}
