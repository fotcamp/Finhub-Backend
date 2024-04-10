package fotcamp.finhub.main.dto.request;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FcmTokenRequestDto {

    private String token;

    public FcmTokenRequestDto(String token) {
        this.token = token;
    }
}
