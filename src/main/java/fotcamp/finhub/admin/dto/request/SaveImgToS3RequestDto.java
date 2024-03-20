package fotcamp.finhub.admin.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class SaveImgToS3RequestDto {
    @NotBlank
    private String type;

    @NotNull(message = "file key null")
    private MultipartFile file;
}
