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
    @Column(name = "ID")
    private Long memberId;

    private Long usertype_id;
    private Long user_avatar_id;

    @Column(name = "EMAIL", nullable = false, unique = true)
    private String email;

    @Column(name = "PROFILE_NICKNAME", nullable = false)
    private String name;

    private boolean push_yn;
    private String fcmToken;

    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE", nullable = false)
    private RoleType role;

    // userAvatar 1대1 연관관계 ( 주인은 member : 유저 아바타를 등록하지 않은 경우 null로 처리 )
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "AVATAR_ID", referencedColumnName = "userAvatarId")
    private UserAvatar userAvatar;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private MemberNotification memberNotification;

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
