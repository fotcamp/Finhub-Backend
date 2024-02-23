package fotcamp.finhub.common.domain;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE", nullable = false)
    private RoleType role;

    @Builder
    public Member(Long memberId, String email, String name, String password) {
        this.memberId = memberId;
        this.email = email;
        this.name = name;
        this.password = password;
        this.role = RoleType.ROLE_USER;
    }
}
