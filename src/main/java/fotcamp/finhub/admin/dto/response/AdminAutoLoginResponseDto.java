package fotcamp.finhub.admin.dto.response;

import fotcamp.finhub.admin.dto.process.AdminAutoLoginProcessDto;
import fotcamp.finhub.common.security.TokenDto;
import fotcamp.finhub.main.dto.process.login.UserInfoProcessDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AdminAutoLoginResponseDto {

    private TokenDto token;
    private AdminAutoLoginProcessDto info;

    public AdminAutoLoginResponseDto(TokenDto token, AdminAutoLoginProcessDto info) {
        this.token = token;
        this.info = info;
    }
}
