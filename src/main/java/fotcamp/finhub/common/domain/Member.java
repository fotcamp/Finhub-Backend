package fotcamp.finhub.common.domain;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "MEMBER")
@Getter
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_ID")
    private Long memberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USERTYPE_ID")
    private UserType userType;

    @Column(name = "EMAIL", nullable = false, unique = true)
    private String email;

    @Column(name = "PROFILE_NICKNAME", nullable = false)
    private String name;

    private String nickname;
    private boolean push_yn;
    private String fcmToken;

    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE", nullable = false)
    private RoleType role;

    // userAvatar 1대1 연관관계 ( 주인은 member : 유저 아바타를 등록하지 않은 경우 null로 처리 )
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_AVATAR_ID")
    private UserAvatar userAvatar;

    // calendarEmoticon 1대1 연관관계 ( 주인은 member : 캘린더 이모티콘을 등록하지 않은 경우 null로 처리 )
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CALENDAR_EMOTICON_ID")
    private CalendarEmoticon calendarEmoticon;

    @OneToMany(mappedBy = "member", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<MemberNotification> memberNotificationList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private final List<MemberQuiz> quizList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<MemberScrap> memberScrapList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<RecentSearch> recentSearchList = new ArrayList<>();

    @OneToOne(mappedBy = "member", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private RefreshToken refreshToken;

    public Member(String email, String name){
        this.email = email;
        this.name = name;
        this.role = RoleType.ROLE_USER;
    }

    @Builder
    public Member(UserType userType, String email, String name, String nickname, boolean push_yn, String fcmToken, RoleType role, UserAvatar userAvatar, List<MemberNotification> memberNotificationList, List<MemberScrap> memberScrapList, List<RecentSearch> recentSearchList) {
        this.userType = userType;
        this.email = email;
        this.name = name;
        this.nickname = nickname;
        this.push_yn = push_yn;
        this.fcmToken = fcmToken;
        this.role = RoleType.ROLE_USER;
        this.userAvatar = userAvatar;
        this.memberNotificationList = memberNotificationList;
        this.memberScrapList = memberScrapList;
        this.recentSearchList = recentSearchList;
    }

    public void updateNickname(String newNickname){
        this.nickname = newNickname;
    }

    public void updateJob(UserType userType){
        this.userType = userType;
    }

    public void updateAvatar(UserAvatar userAvatar){
        this.userAvatar = userAvatar;
    }

    public void removeScrap(MemberScrap memberScrap){
        this.memberScrapList.remove(memberScrap);
    }

    public void removeUserAvatar(UserAvatar userAvatar){
        this.userAvatar = null;
    }

    public void addMemberQuiz(MemberQuiz memberQuiz) {
        quizList.add(memberQuiz);
    }
}
