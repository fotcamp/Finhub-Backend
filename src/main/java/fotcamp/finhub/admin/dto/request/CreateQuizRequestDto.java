package fotcamp.finhub.admin.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

@Getter
public class CreateQuizRequestDto {
    @NotNull
    private Long year;
    @NotNull
    @Min(1)
    @Max(12)
    private Long month;
    @NotNull
    @Min(1)
    @Max(31)
    private Long day;
    @NotBlank
    private String question; // 질문
    @NotBlank
    private String answer; // 정답 (O, X)
    @NotBlank
    private String comment; // 해설

    private List<Long> topicList; // 관련 토픽들



}
