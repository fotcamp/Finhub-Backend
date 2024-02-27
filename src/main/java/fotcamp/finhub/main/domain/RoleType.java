package fotcamp.finhub.main.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** ROLE_을 접두에 붙여 사용
 * EX) hasRole("USER") -> ROLE은 명시적으로 필요하지 X
 * */

@AllArgsConstructor
@Getter
public enum RoleType {
    ROLE_USER, ROLE_SUPER, ROLE_BE, ROLE_FE;

    public String getAuthority(){
        return name();
    }
}


