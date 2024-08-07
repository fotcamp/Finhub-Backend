package fotcamp.finhub.main.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class FeedbackRequestDto {

    @NotBlank(message = "이메일은 필수 항목입니다.")
    private String email;

    @NotBlank(message = "피드백 내용은 필수 항목입니다.")
    private String text;

    @Size(max = 4, message = "최대 4개의 파일만 업로드할 수 있습니다.")
    private MultipartFile[] files;

    public FeedbackRequestDto() {
    }
}
