package fotcamp.finhub.common.dto.common;

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
public class CustomUserInfoDto {

    private Long memberId;
    private String email;
    private String name;
    private String password;
    private RoleType role;


    public CustomUserInfoDto(Long memberId, String email, String name, String password, RoleType role) {
        this.memberId = memberId;
        this.email = email;
        this.name = name;
        this.password = password;
        this.role = role;
    }

    public static CustomUserInfoDto fromEntity(Member member){
        return CustomUserInfoDto.builder()
                .email(member.getEmail())
                .name(member.getName())
                .password(member.getPassword())
                .role(member.getRole())
                .build();
    }
}
