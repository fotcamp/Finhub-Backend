package fotcamp.finhub.admin.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ReportReasonModifyRequestDto(@NotNull Long id, @NotBlank String reason, @NotBlank String useYN) {
}
