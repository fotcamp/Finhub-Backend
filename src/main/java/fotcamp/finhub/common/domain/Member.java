package fotcamp.finhub.common.domain;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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

    private Long usertype_id;

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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_AVATAR_ID")
    private UserAvatar userAvatar;

    @OneToMany(mappedBy = "member", cascade = CascadeType.PERSIST)
    private List<MemberNotification> memberNotificationList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.PERSIST)
    private final List<MemberQuiz> quizList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.PERSIST)
    private List<MemberScrap> memberScrapList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.PERSIST)
    private List<RecentSearch> recentSearchList = new ArrayList<>();

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
