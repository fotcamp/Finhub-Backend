package fotcamp.finhub.main.dto.response.popularSearch;

import java.time.LocalDate;
import java.util.List;

public record PopularSearchResponseDto(LocalDate date, List<PopularSearchDto> popularSearchList) {
}
