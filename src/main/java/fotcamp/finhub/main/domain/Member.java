package fotcamp.finhub.main.domain;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Entity
@Table(name = "MEMBER")
@Getter
@Setter
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_ID")
    private Long memberId;

    @Column(name = "EMAIL", nullable = false, unique = true)
    private String email;

    @Column(name = "PROFILE_NICKNAME", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE", nullable = false)
    private RoleType role;

    @Builder
    public Member(String email, String name) {
        this.email = email;
        this.name = name;
        this.role = RoleType.ROLE_USER;
    }

    public static Member ToEntity(String email, String nickname){
        return Member.builder()
                .email(email)
                .name(nickname)
                .build();
    }

}
