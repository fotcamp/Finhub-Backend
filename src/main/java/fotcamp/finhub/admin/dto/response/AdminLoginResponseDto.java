package fotcamp.finhub.admin.dto.response;


import fotcamp.finhub.common.domain.RoleType;
import fotcamp.finhub.common.security.TokenDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AdminLoginResponseDto {

    private RoleType roleType;
    private TokenDto token;

    public AdminLoginResponseDto(RoleType roleType, TokenDto token) {
        this.roleType = roleType;
        this.token = token;
    }
}
