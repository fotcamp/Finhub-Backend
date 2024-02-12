package fotcamp.finhub.admin.dto.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class SaveImgToS3RequestDto {
    private String type;
    private Long id;
    private MultipartFile file;
}
