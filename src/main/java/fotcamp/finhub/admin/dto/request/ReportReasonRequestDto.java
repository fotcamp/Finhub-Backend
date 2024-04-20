package fotcamp.finhub.admin.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ReportReasonRequestDto(@NotBlank String reason) {
}
