package fotcamp.finhub.main.dto.response.login;


import lombok.Getter;

@Getter
public class UpdateAccessTokenResponseDto {

    private String token;

    public UpdateAccessTokenResponseDto(String token) {
        this.token = token;
    }
}
