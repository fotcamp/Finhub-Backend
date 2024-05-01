package fotcamp.finhub.admin.dto.process;


import fotcamp.finhub.common.domain.RoleType;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AdminAutoLoginProcessDto {

    private RoleType role;
    private String email;


    public AdminAutoLoginProcessDto(RoleType role, String email) {
        this.role = role;
        this.email = email;
    }
}
