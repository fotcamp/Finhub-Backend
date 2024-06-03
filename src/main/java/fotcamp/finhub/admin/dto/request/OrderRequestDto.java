package fotcamp.finhub.admin.dto.request;

import java.util.Map;

public record OrderRequestDto(Map<Long, Long> orders) {
}
