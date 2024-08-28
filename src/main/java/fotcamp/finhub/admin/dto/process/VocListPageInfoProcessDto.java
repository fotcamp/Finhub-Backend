package fotcamp.finhub.admin.dto.process;

import lombok.Builder;
import lombok.Getter;

@Builder
public record VocListPageInfoProcessDto(
        int currentPage,
        int totalPages,
        int pageSize,
        long totalElements
) {
}
