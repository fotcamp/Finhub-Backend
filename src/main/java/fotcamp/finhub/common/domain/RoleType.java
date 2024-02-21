package fotcamp.finhub.common.domain;

/** 관례적으로 ROLE_을 접두에 붙여 사용
 * Spring Security는 USER, ADMIN으로만 인식
 * EX) hasRole("USER") -> ROLE은 명시적으로 필요하지 X */

public enum RoleType {
    ROLE_USER, ROLE_ADMIN
}


