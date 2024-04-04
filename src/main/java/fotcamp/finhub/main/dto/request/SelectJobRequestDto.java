package fotcamp.finhub.main.dto.request;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SelectJobRequestDto {

    private Long jobId;

    public SelectJobRequestDto(Long jobId) {
        this.jobId = jobId;
    }
}
