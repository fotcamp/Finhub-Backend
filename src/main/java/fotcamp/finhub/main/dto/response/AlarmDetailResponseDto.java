package fotcamp.finhub.main.dto.response;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AlarmDetailResponseDto {

    private String url;

    public AlarmDetailResponseDto(String url) {
        this.url = url;
    }
}
