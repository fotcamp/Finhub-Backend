package fotcamp.finhub.main.dto.request;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SelectJobRequestDto {

    private String job;

    public SelectJobRequestDto(String job) {
        this.job = job;
    }
}
