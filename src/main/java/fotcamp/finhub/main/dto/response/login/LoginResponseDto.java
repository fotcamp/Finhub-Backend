package fotcamp.finhub.main.dto.response.login;

import fotcamp.finhub.common.security.TokenDto;
import fotcamp.finhub.main.dto.process.login.UserInfoProcessDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginResponseDto {

    private TokenDto token;
    private UserInfoProcessDto info;

    public LoginResponseDto(TokenDto token, UserInfoProcessDto info) {
        this.token = token;
        this.info = info;
    }
}
