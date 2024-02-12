package fotcamp.finhub.admin.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record CreateCategoryRequestDto(@NotBlank String name, @NotNull(message = "file key null") MultipartFile file) {
}
