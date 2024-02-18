package fotcamp.finhub.common.dto;

import fotcamp.finhub.common.domain.Member;
import fotcamp.finhub.common.domain.RoleType;

/** 로직 내부에서 유저 정보를 저장해 둘 DTO */
public class CustomUserInfoDto extends Member {

    private Long memberId;
    private String email;
    private String name;
    private String password;
    private RoleType role;

}
