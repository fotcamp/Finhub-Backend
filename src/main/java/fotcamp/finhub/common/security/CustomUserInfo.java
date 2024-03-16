package fotcamp.finhub.common.security;

import fotcamp.finhub.common.domain.Member;
import fotcamp.finhub.common.domain.RoleType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** 로직 내부에서 유저 정보를 저장해 둘 DTO */
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CustomUserInfo {

    private Long memberId;
    private String email;
    private String name;
    private RoleType role;


    public CustomUserInfo(Long memberId, String email, String name, RoleType role) {
        this.memberId = memberId;
        this.email = email;
        this.name = name;
        this.role = role;
    }

    public static CustomUserInfo fromEntity(Member member){
        return CustomUserInfo.builder()
                .email(member.getEmail())
                .name(member.getName())
                .role(member.getRole())
                .build();
    }
}
