package fotcamp.finhub.main.dto.response;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class AutoLoginResponseDto {

    private String accessToken;
    private String name;
    private String email;

    public AutoLoginResponseDto(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
